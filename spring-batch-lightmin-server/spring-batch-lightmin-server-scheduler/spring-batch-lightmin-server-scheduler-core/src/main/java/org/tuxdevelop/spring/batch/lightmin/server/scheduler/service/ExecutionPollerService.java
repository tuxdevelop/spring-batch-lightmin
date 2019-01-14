package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

public interface ExecutionPollerService {
    void fireExecutions();

    void handleFailedExecutions();
}
