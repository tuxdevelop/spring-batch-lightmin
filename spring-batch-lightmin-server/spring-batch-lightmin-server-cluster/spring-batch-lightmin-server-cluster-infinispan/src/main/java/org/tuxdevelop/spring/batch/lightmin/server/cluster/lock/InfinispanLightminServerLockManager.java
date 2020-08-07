package org.tuxdevelop.spring.batch.lightmin.server.cluster.lock;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.Cache;
import org.infinispan.CacheSet;
import org.infinispan.lock.EmbeddedClusteredLockManagerFactory;
import org.infinispan.lock.api.ClusteredLock;
import org.infinispan.lock.api.ClusteredLockManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.partitionhandling.AvailabilityException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@Slf4j
public class InfinispanLightminServerLockManager implements LightminServerLockManager {

    public static final String VERIFICATION_CACHE_NAME = "lightminLockVerificationCache";
    private static final String LOCKED_VALUE = "locked";
    private static final Long RETRY_TIME_IN_MILLIS = 500L;

    private final ClusteredLockManager clusteredLockManager;
    private final Cache<String, String> verificationCache;

    public InfinispanLightminServerLockManager(final EmbeddedCacheManager embeddedCacheManager) {
        this.clusteredLockManager = EmbeddedClusteredLockManagerFactory.from(embeddedCacheManager);
        this.verificationCache = embeddedCacheManager.getCache(VERIFICATION_CACHE_NAME);
    }


    @Override
    public void acquireLock(final String id, final Long timeout, final TimeUnit timeUnit) {
        final TryLockCommand tryLockCommand = new TryLockCommand(timeout, timeUnit);
        tryLockCommand.tryLock(id);
    }

    @Override
    public void acquireLock(final String id) {
        this.acquireLock(id, null, null);
    }

    @Override
    public void releaseLock(final String id, final Boolean forceRelease) {
        this.unlock(id, forceRelease);
    }

    @Override
    public List<String> getAcquiredLocks() {
        final CacheSet<String> keys = this.verificationCache.keySet();
        return new ArrayList<>(keys);
    }

    private void unlock(final String id, final Boolean forceRelease) {

        final ClusteredLock clusteredLock = this.getClusteredLock(id);

        if (forceRelease) {
            //forced unlock
            final CompletableFuture<Boolean> unlocked =
                    this.clusteredLockManager.forceRelease(id);
            while (!unlocked.isDone()) {
                log.debug("waiting to unlock {}", id);
            }
            this.removeLock(id);
            try {
                if (unlocked.get()) {
                    log.debug("forced unlock of {} successful", id);
                } else {
                    throw new SpringBatchLightminApplicationException("Could not execute a forced unlock for " + id);
                }
            } catch (final InterruptedException | ExecutionException e) {
                throw new SpringBatchLightminApplicationException(e, "Unexpected Exception during force unlock");
            }
        } else {
            //Regular unlock by the owner
            try {
                //only the owner should ne able to unlock
                final CompletableFuture<Boolean> lockedByMe = clusteredLock.isLockedByMe();
                if (lockedByMe.get()) {
                    final CompletableFuture<Void> unlocked = clusteredLock.unlock();
                    while (!unlocked.isDone()) {
                        log.debug("waiting to unlock {}", id);
                    }
                    this.removeLock(id);
                } else {
                    throw new SpringBatchLightminApplicationException("Tried to unlock a ClusterLock not owned by me!");
                }
            } catch (final InterruptedException | ExecutionException e) {
                throw new SpringBatchLightminApplicationException(e, "Unexpected Exception ");
            }
        }
    }

    /**
     * removes the given lockId from the verification verificationCache and the local set
     *
     * @param lockId the lock id to remove
     */
    private void removeLock(final String lockId) {
        this.verificationCache.remove(lockId);
    }

    private ClusteredLock getClusteredLock(final String lockId) {
        if (this.clusteredLockManager.isDefined(lockId)) {
            log.debug("Lock {}  already defined", lockId);
        } else {
            this.clusteredLockManager.defineLock(lockId);
        }
        return this.clusteredLockManager.get(lockId);
    }


    /**
     * Command to handle the locking for infinispan
     */
    private class TryLockCommand {

        private final Long timeoutInMillis;

        /**
         * Creates a new TryLockCommand.
         * If timeout and timeUnit is set, a retry to acquire the lock is possible
         *
         * @param timeout  the time out value
         * @param timeUnit the unit of the time out
         */
        TryLockCommand(final Long timeout, final TimeUnit timeUnit) {
            if (timeout != null && timeUnit != null) {
                final long timeUnitMillis = TimeUnit.MILLISECONDS.convert(timeout, timeUnit);
                this.timeoutInMillis = System.currentTimeMillis() + timeUnitMillis;
            } else {
                this.timeoutInMillis = null;
            }
        }

        void tryLock(final String lockId) {
            // the verification verificationCache contains the key ?
            if (InfinispanLightminServerLockManager.this.verificationCache.containsKey(lockId)) {
                log.warn("active lock available for lockid {}", lockId);
                //if a time out is set, retry is possible
                if (this.timeOutSet()) {
                    this.retry(lockId);
                } else {
                    //no timeout set, fail directly
                    throw new SpringBatchLightminApplicationException("Could not acquire lock for lockId " + lockId);
                }
            } else {
                try {
                    //try to get the infinispan clustered lock
                    final ClusteredLock clusteredLock = InfinispanLightminServerLockManager.this.getClusteredLock(lockId);
                    final CompletableFuture<Boolean> result = clusteredLock.tryLock();
                    //lock if the lock was acquired
                    this.checkLockAcquiredResult(clusteredLock, result);
                    //put the lock in the verification cash and the local hashset
                    this.putLock(lockId);
                } catch (final AvailabilityException availabilityException) {
                    log.warn("One of the members split from the cluster. Lock with the Id {} is not available.", lockId);
                    throw new SpringBatchLightminApplicationException("Could not acquire lock for lockId " + lockId);
                } catch (final Exception e) {
                    log.debug("Could not get lock for lockId {} retry", lockId);
                    //lock could not get acquired, if time out is set, retry is possible
                    if (this.timeOutSet()) {
                        this.retry(lockId);
                    } else {
                        //if no time out is set, fail
                        throw new SpringBatchLightminApplicationException("Could not acquire lock for lockId " + lockId);
                    }
                }
            }
        }

        private void retry(final String lockId) {
            try {
                //Wait the configured amount of time, until the next retry should be done
                Thread.sleep(InfinispanLightminServerLockManager.RETRY_TIME_IN_MILLIS);
            } catch (final InterruptedException e) {
                log.error("", e);
            }
            //if timeout period is still valid, try to get the lock
            if (System.currentTimeMillis() < this.timeoutInMillis) {
                this.tryLock(lockId);
            } else {
                //if time out period is not valid anymore, fail directly
                throw new SpringBatchLightminApplicationException("Timeout reached to acquire lock for lockId " + lockId);
            }
        }

        /**
         * stores the given lockId in the verification verificationCache and the local lock set
         *
         * @param lockId
         */
        private void putLock(final String lockId) {
            InfinispanLightminServerLockManager.this.verificationCache.put(lockId, LOCKED_VALUE);
        }

        /**
         * checks the result of the future
         *
         * @param clusteredLock acquired lock
         * @param result        the result of the lock
         */
        private void checkLockAcquiredResult(final ClusteredLock clusteredLock,
                                             final CompletableFuture<Boolean> result) {
            try {
                if (result.get()) {
                    log.debug("Lock acquired");
                } else {
                    throw new SpringBatchLightminApplicationException("Could not get exclusive lock for " + clusteredLock);
                }
            } catch (final InterruptedException | ExecutionException e) {
                throw new SpringBatchLightminApplicationException(e, "Error while acquiring exclusive lock");
            }
        }

        private Boolean timeOutSet() {
            return this.timeoutInMillis != null;
        }

    }
}
