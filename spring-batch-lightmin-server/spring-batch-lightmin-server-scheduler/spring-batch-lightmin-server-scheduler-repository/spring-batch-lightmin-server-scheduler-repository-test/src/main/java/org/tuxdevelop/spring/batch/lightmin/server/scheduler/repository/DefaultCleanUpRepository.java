package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;

import java.util.ArrayList;
import java.util.List;

public class DefaultCleanUpRepository implements CleanUpRepository {

    private final SchedulerConfigurationRepository schedulerConfigurationRepository;

    private final SchedulerExecutionRepository schedulerExecutionRepository;

    public DefaultCleanUpRepository(final SchedulerConfigurationRepository schedulerConfigurationRepository,
                                    final SchedulerExecutionRepository schedulerExecutionRepository) {
        this.schedulerConfigurationRepository = schedulerConfigurationRepository;
        this.schedulerExecutionRepository = schedulerExecutionRepository;
    }

    @Override
    public void cleanUp() {
        final List<Long> ids = new ArrayList<>();
        for (final SchedulerConfiguration schedulerConfiguration : this.schedulerConfigurationRepository.findAll()) {
            ids.add(schedulerConfiguration.getId());
        }
        for (final Long id : ids) {
            this.schedulerExecutionRepository.deleteBySchedulerConfigurationId(id);
            this.schedulerConfigurationRepository.delete(id);
        }
    }


}
