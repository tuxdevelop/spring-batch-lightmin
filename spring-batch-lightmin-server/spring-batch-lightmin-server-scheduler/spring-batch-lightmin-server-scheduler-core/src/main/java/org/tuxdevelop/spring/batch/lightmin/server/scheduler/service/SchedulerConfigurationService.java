package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerValidationException;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerConfigurationNotFoundException;

public class SchedulerConfigurationService {

    private final SchedulerConfigurationRepository schedulerConfigurationRepository;

    public SchedulerConfigurationService(final SchedulerConfigurationRepository schedulerConfigurationRepository) {
        this.schedulerConfigurationRepository = schedulerConfigurationRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SchedulerConfiguration save(final SchedulerConfiguration schedulerConfiguration) {
        if (schedulerConfiguration == null) {
            throw new SchedulerValidationException("schedulerConfiguration must not be null");
        } else {
            return this.schedulerConfigurationRepository.save(schedulerConfiguration);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(final SchedulerConfiguration schedulerConfiguration) {
        if (schedulerConfiguration == null) {
            throw new SchedulerValidationException("schedulerConfiguration must not be null");
        } else {
            this.schedulerConfigurationRepository.delete(schedulerConfiguration.getId());
        }
    }

    @Transactional(readOnly = true)
    public SchedulerConfiguration findById(final Long id) throws SchedulerConfigurationNotFoundException {
        return this.schedulerConfigurationRepository.findById(id);
    }
}
