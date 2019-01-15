package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration.ServerSchedulerCoreConfigurationProperties;

public class StandaloneExecutionPollerService extends AbstractExecutionPollerService implements ExecutionPollerService {


    public StandaloneExecutionPollerService(final ExecutionRunnerService executionRunnerService,
                                            final SchedulerExecutionService schedulerExecutionService,
                                            final ServerSchedulerCoreConfigurationProperties properties) {
        super(executionRunnerService, schedulerExecutionService, properties);
    }

    @Override
    @Scheduled(
            initialDelay = 1000,
            fixedDelayString = "${spring.batch.lightmin.server.scheduler.poller-period:1000}")
    public void fireExecutions() {
        this.triggerScheduledExecutions();
    }

    @Override
    @Scheduled(
            initialDelay = 1000,
            fixedDelayString = "${spring.batch.lightmin.server.scheduler.poller-period-retry:10000}")
    public void handleFailedExecutions() {
        this.triggerRetryExecutions();
    }

}
