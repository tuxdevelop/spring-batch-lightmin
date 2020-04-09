package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

public class MapSchedulerExecutionRepositoryTest extends SchedulerExecutionRepositoryTest {

    final MapSchedulerConfigurationRepository schedulerConfigurationRepository = new MapSchedulerConfigurationRepository();
    final MapSchedulerExecutionRepository schedulerExecutionRepository = new MapSchedulerExecutionRepository();
    final CleanUpRepository cleanUpRepository =
            new DefaultCleanUpRepository(this.schedulerConfigurationRepository, this.schedulerExecutionRepository);

    @Override
    public SchedulerExecutionRepository getSchedulerExecutionRepository() {
        return this.schedulerExecutionRepository;
    }

    @Override
    public SchedulerConfigurationRepository getSchedulerConfigurationRepository() {
        return this.schedulerConfigurationRepository;
    }

    @Override
    public CleanUpRepository getCleanUpRepository() {
        return this.cleanUpRepository;
    }
}
