package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MapCleanUpRepository implements CleanUpRepository {

    private final MapSchedulerConfigurationRepository mapSchedulerConfigurationRepository;

    private final MapSchedulerExecutionRepository mapSchedulerExecutionRepository;

    public MapCleanUpRepository(final MapSchedulerConfigurationRepository mapSchedulerConfigurationRepository,
                                final MapSchedulerExecutionRepository mapSchedulerExecutionRepository) {
        this.mapSchedulerConfigurationRepository = mapSchedulerConfigurationRepository;
        this.mapSchedulerExecutionRepository = mapSchedulerExecutionRepository;
    }

    @Override
    public void cleanUp() {
        final List<Long> ids = new ArrayList<>();
        for (final SchedulerConfiguration schedulerConfiguration : this.mapSchedulerConfigurationRepository.findAll()) {
            ids.add(schedulerConfiguration.getId());
        }
        for (final Long id : ids) {
            this.mapSchedulerExecutionRepository.deleteBySchedulerConfigurationId(id);
            this.mapSchedulerConfigurationRepository.delete(id);
        }
    }


}
