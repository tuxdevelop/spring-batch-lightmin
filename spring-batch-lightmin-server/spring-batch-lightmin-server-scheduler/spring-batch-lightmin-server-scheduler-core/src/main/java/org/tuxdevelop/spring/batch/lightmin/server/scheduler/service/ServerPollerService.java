package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

@Slf4j
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
        try {
            this.executionPollerService.fireExecutions();
        } catch (SpringBatchLightminApplicationException e) {
            log.debug(e.getMessage());
        }
    }


    @Scheduled(
            initialDelay = 1000,
            fixedDelayString = "${spring.batch.lightmin.server.scheduler.poller-period-retry:10000}")
    public void triggerRetries() {
        try {
            this.executionPollerService.handleFailedExecutions();
        } catch (SpringBatchLightminApplicationException e) {
            log.debug(e.getMessage());
        }
    }

    @Scheduled(
            initialDelayString = "${spring.batch.lightmin.server.scheduler.repository.delete-poller-period:PT10M}",
            fixedDelayString = "${spring.batch.lightmin.server.scheduler.repository.delete-poller-period:PT10M}"
    )
    public void triggerCleanUp() {
        try {
            this.executionCleanUpService.triggerCleanUp();
        } catch (SpringBatchLightminApplicationException e) {
            log.debug(e.getMessage());
        }
    }
}
