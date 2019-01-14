package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;

public class SchedulerConfigurationService {

    private final SchedulerConfigurationRepository schedulerConfigurationRepository;

    public SchedulerConfigurationService(final SchedulerConfigurationRepository schedulerConfigurationRepository) {
        this.schedulerConfigurationRepository = schedulerConfigurationRepository;
    }

    public SchedulerConfiguration save(final SchedulerConfiguration schedulerConfiguration) {

        //TODO: validate
        return this.schedulerConfigurationRepository.save(schedulerConfiguration);
    }

    public void delete(final SchedulerConfiguration schedulerConfiguration) {
        //TODO: validate
        this.schedulerConfigurationRepository.delete(schedulerConfiguration.getId());
    }

    public SchedulerConfiguration findById(final Long id) {
        return this.schedulerConfigurationRepository.findById(id);
    }
}
