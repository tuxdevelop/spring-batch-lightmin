package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration.ServerSchedulerCoreConfigurationProperties;

public class StandaloneExecutionPollerService extends AbstractExecutionPollerService implements ExecutionPollerService {


    public StandaloneExecutionPollerService(final ExecutionRunnerService executionRunnerService,
                                            final SchedulerExecutionService schedulerExecutionService,
                                            final ServerSchedulerCoreConfigurationProperties properties) {
        super(executionRunnerService, schedulerExecutionService, properties);
    }

    @Override
    public void fireExecutions() {
        this.triggerScheduledExecutions();
    }

    @Override
    public void handleFailedExecutions() {
        this.triggerRetryExecutions();
    }

}
