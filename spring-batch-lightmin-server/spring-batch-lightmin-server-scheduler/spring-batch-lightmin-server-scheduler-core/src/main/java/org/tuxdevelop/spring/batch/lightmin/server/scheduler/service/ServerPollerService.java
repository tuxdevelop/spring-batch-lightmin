package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import org.springframework.scheduling.annotation.Scheduled;

public class ServerPollerService {

    private final ExecutionPollerService executionPollerService;

    public ServerPollerService(final ExecutionPollerService executionPollerService) {
        this.executionPollerService = executionPollerService;
    }

    @Scheduled(
            initialDelay = 1000,
            fixedDelayString = "${spring.batch.lightmin.server.scheduler.poller-period-retry:1000}")
    public void triggerExecutions() {
        this.executionPollerService.fireExecutions();
    }


    @Scheduled(
            initialDelay = 1000,
            fixedDelayString = "${spring.batch.lightmin.server.scheduler.poller-period-retry:10000}")
    public void triggerRetries() {
        this.executionPollerService.handleFailedExecutions();
    }
}
