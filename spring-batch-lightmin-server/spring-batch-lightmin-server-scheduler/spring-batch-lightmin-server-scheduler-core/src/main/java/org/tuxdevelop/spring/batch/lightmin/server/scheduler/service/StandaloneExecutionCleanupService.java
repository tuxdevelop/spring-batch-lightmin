package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration.ServerSchedulerCoreConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;

public class StandaloneExecutionCleanupService extends AbstractExecutionCleanUpService {

    public StandaloneExecutionCleanupService(final SchedulerExecutionRepository schedulerExecutionRepository,
                                             final ServerSchedulerCoreConfigurationProperties properties) {
        super(schedulerExecutionRepository, properties);
    }

    @Override
    public void triggerCleanUp() {
        super.cleanRepository();
    }
}
