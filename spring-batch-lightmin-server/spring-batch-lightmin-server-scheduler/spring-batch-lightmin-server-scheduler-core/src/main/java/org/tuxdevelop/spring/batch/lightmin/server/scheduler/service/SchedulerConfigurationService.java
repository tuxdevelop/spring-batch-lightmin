package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerValidationException;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerConfigurationNotFoundException;

@Slf4j
public class SchedulerConfigurationService {

    private final SchedulerConfigurationRepository schedulerConfigurationRepository;

    public SchedulerConfigurationService(final SchedulerConfigurationRepository schedulerConfigurationRepository) {
        this.schedulerConfigurationRepository = schedulerConfigurationRepository;
    }

    @Transactional(transactionManager = "lightminServerSchedulerTransactionManager", propagation = Propagation.REQUIRED)
    public SchedulerConfiguration save(final SchedulerConfiguration schedulerConfiguration) {
        if (schedulerConfiguration == null) {
            throw new SchedulerValidationException("schedulerConfiguration must not be null");
        } else {
            return this.schedulerConfigurationRepository.save(schedulerConfiguration);
        }
    }

    @Transactional(transactionManager = "lightminServerSchedulerTransactionManager", propagation = Propagation.REQUIRED)
    public void delete(final SchedulerConfiguration schedulerConfiguration) {
        if (schedulerConfiguration == null) {
            throw new SchedulerValidationException("schedulerConfiguration must not be null");
        } else {
            this.schedulerConfigurationRepository.delete(schedulerConfiguration.getId());
        }
    }

    @Transactional(transactionManager = "lightminServerSchedulerTransactionManager", readOnly = true)
    public SchedulerConfiguration findById(final Long id) throws SchedulerConfigurationNotFoundException {
        return this.schedulerConfigurationRepository.findById(id);
    }

    public Boolean schedulerConfigurationExists(final Long id) {
        Boolean exists;
        try {
            this.findById(id);
            exists = Boolean.TRUE;
        } catch (final SchedulerConfigurationNotFoundException e) {
            exists = Boolean.FALSE;
            log.info("SchedulerConfiguration for id {} does not exist", id);
        }
        return exists;
    }
}
