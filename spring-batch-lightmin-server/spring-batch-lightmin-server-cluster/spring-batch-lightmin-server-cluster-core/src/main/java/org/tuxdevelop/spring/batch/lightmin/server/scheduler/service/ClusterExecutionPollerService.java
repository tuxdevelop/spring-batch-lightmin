package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import org.tuxdevelop.spring.batch.lightmin.server.cluster.annotation.ServerClusterLock;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration.ServerSchedulerCoreConfigurationProperties;

public class ClusterExecutionPollerService extends AbstractExecutionPollerService {

    private static final String FIRE_EXECUTIONS_LOCK_ID = "FIRE_EXECUTIONS_LOCK";
    private static final String HANDLE_FAILED_EXECUTIONS_LOCK_ID = "HANDLE_FAILED_EXECUTIONS_LOCK";

    public ClusterExecutionPollerService(final ServerSchedulerService serverSchedulerService,
                                         final SchedulerExecutionService schedulerExecutionService,
                                         final ServerSchedulerCoreConfigurationProperties properties) {
        super(serverSchedulerService, schedulerExecutionService, properties);
    }

    @Override
    @ServerClusterLock(id = FIRE_EXECUTIONS_LOCK_ID)
    public void fireExecutions() {
        this.triggerScheduledExecutions();
    }

    @Override
    @ServerClusterLock(id = HANDLE_FAILED_EXECUTIONS_LOCK_ID)
    public void handleFailedExecutions() {
        this.triggerRetryExecutions();
    }
}
