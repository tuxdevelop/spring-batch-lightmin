package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration.ServerSchedulerCoreConfigurationProperties;

public class StandaloneExecutionPollerService extends AbstractExecutionPollerService implements ExecutionPollerService {


    public StandaloneExecutionPollerService(final ServerSchedulerService serverSchedulerService,
                                            final SchedulerExecutionService schedulerExecutionService,
                                            final ServerSchedulerCoreConfigurationProperties properties) {
        super(serverSchedulerService, schedulerExecutionService, properties);
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
