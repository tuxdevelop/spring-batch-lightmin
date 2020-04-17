package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import org.tuxdevelop.spring.batch.lightmin.server.cluster.annotation.ServerClusterLock;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration.ServerSchedulerCoreConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;

public class ClusterExecutionCleanUpService extends AbstractExecutionCleanUpService {

    private static final String TRIGGER_CLEAN_UP_LOCK_ID = "TRIGGER_CLEAN_UP_LOCK";

    public ClusterExecutionCleanUpService(final SchedulerExecutionRepository schedulerExecutionRepository,
                                          final ServerSchedulerCoreConfigurationProperties properties) {
        super(schedulerExecutionRepository, properties);
    }

    @Override
    @ServerClusterLock(id = TRIGGER_CLEAN_UP_LOCK_ID)
    public void triggerCleanUp() {
        this.cleanRepository();
    }

}
