package org.tuxdevelop.spring.batch.lightmin.server.cluster.actuator;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.http.MediaType;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.lock.LightminServerLockManager;

import java.util.List;

@WebEndpoint(
        id = "lightminserverlocks")
public class LockEndpoint {

    private final LightminServerLockManager lightminServerLockManager;

    public LockEndpoint(final LightminServerLockManager lightminServerLockManager) {
        this.lightminServerLockManager = lightminServerLockManager;
    }

    @ReadOperation(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getAllAcquiredLocks() {
        return this.lightminServerLockManager.getAcquiredLocks();
    }

    @WriteOperation
    public void releaseLock(@Selector final String id) {
        this.lightminServerLockManager.releaseLock(id, Boolean.TRUE);
    }

}
