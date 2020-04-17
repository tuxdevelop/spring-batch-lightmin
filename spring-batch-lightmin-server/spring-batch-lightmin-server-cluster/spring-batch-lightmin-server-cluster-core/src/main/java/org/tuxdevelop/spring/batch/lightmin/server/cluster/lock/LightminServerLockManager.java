package org.tuxdevelop.spring.batch.lightmin.server.cluster.lock;

import java.util.concurrent.TimeUnit;

public interface LightminServerLockManager {

    void acquireLock(String id, Long timeout, TimeUnit timeUnit);

    void acquireLock(String id);

    void releaseLock(String id, Boolean forceRelease);
}
