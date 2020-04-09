package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

public class MapSchedulerConfigurationRepositoryTest extends SchedulerConfigurationRepositoryTest {

    final MapSchedulerConfigurationRepository schedulerConfigurationRepository = new MapSchedulerConfigurationRepository();
    final MapSchedulerExecutionRepository schedulerExecutionRepository = new MapSchedulerExecutionRepository();
    final CleanUpRepository cleanUpRepository =
            new DefaultCleanUpRepository(this.schedulerConfigurationRepository, this.schedulerExecutionRepository);


    @Override
    public SchedulerConfigurationRepository getSchedulerConfigurationRepository() {
        return this.schedulerConfigurationRepository;
    }

    @Override
    public CleanUpRepository getCleanUpRepository() {
        return this.cleanUpRepository;
    }
}
