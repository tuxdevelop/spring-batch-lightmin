package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import org.springframework.scheduling.annotation.Scheduled;

public class ServerPollerService {

    private final ExecutionPollerService executionPollerService;
    private final ExecutionCleanUpService executionCleanUpService;

    public ServerPollerService(final ExecutionPollerService executionPollerService, final ExecutionCleanUpService executionCleanUpService) {
        this.executionPollerService = executionPollerService;
        this.executionCleanUpService = executionCleanUpService;
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

    @Scheduled(
            initialDelayString = "${spring.batch.lightmin.server.scheduler.repository.delete-poller-period:PT10M}",
            fixedDelayString = "${spring.batch.lightmin.server.scheduler.repository.delete-poller-period:PT10M}"
    )
    public void triggerCleanUp() {
        this.executionCleanUpService.triggerCleanUp();
    }
}
