package org.tuxdevelop.spring.batch.lightmin.server.cluster.lock;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.annotation.ServerClusterLock;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
public class ServerLockAspect {

    private final LightminServerLockManager lightminServerLockManager;

    public ServerLockAspect(final LightminServerLockManager lightminServerLockManager) {
        this.lightminServerLockManager = lightminServerLockManager;
    }

    @Around(value = "anyPublicMethod() && @annotation(serverClusterLock)")
    public Object handleLock(final ProceedingJoinPoint proceedingJoinPoint,
                             final ServerClusterLock serverClusterLock) throws Throwable {

        final String id = serverClusterLock.id();
        final boolean waitForLock = serverClusterLock.waitForLock();
        final long timeOut = serverClusterLock.timeout();
        final TimeUnit timeUnit = serverClusterLock.timeUnit();

        if (waitForLock) {
            this.checkForTimeout(timeOut, timeUnit);
            this.lightminServerLockManager.acquireLock(id, timeOut, timeUnit);
        } else {
            this.lightminServerLockManager.acquireLock(id);
        }

        final Object result;

        try {
            result = proceedingJoinPoint.proceed();
        } finally {
            //unlock
            log.debug("releasing lock for id {}", id);
            this.lightminServerLockManager.releaseLock(id, Boolean.FALSE);
        }

        return result;
    }

    @Pointcut(value = "execution(public * *(..))")
    public void anyPublicMethod() {
    }

    private void checkForTimeout(final long timeout, final TimeUnit timeUnit) {
        if (timeout <= 0 || timeUnit == null) {
            throw new SpringBatchLightminConfigurationException("timeout must be greater 0 and timeUnit must not be null");
        } else {
            log.debug("timeout and timeunit set correctly");
        }
    }
}
