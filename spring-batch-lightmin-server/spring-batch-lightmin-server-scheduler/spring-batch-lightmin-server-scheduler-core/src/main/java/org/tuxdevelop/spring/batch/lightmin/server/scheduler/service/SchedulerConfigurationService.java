package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.*;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerConfigurationNotFoundException;

import java.util.List;

@Slf4j
public class SchedulerConfigurationService {

    private final SchedulerConfigurationRepository schedulerConfigurationRepository;
    private final SchedulerExecutionRepository schedulerExecutionRepository;

    public SchedulerConfigurationService(final SchedulerConfigurationRepository schedulerConfigurationRepository, SchedulerExecutionRepository schedulerExecutionRepository) {
        this.schedulerConfigurationRepository = schedulerConfigurationRepository;
        this.schedulerExecutionRepository = schedulerExecutionRepository;
    }

    @Transactional(transactionManager = "lightminServerSchedulerTransactionManager", propagation = Propagation.REQUIRED)
    public SchedulerConfiguration save(final SchedulerConfiguration schedulerConfiguration) {
        if (schedulerConfiguration == null) {
            throw new SchedulerValidationException("schedulerConfiguration must not be null");
        } else {
            schedulerConfiguration.validate();
            return this.schedulerConfigurationRepository.save(schedulerConfiguration);
        }
    }

    @Transactional(transactionManager = "lightminServerSchedulerTransactionManager", propagation = Propagation.REQUIRED)
    public void delete(final SchedulerConfiguration schedulerConfiguration) {
        if (schedulerConfiguration == null) {
            throw new SchedulerValidationException("schedulerConfiguration must not be null");
        } else if (schedulerConfiguration.getStatus().equals(ServerSchedulerStatus.ACTIVE)) {
            throw new SchedulerValidationException("schedulerConfiguration must be STOPPED before it can be deleted");
        } else {
            List<SchedulerExecution> bySchedulerConfigurationId = this.schedulerExecutionRepository.findBySchedulerConfigurationId(schedulerConfiguration.getId());
            boolean isRunningExecution = bySchedulerConfigurationId.stream().anyMatch(schedulerExecution -> schedulerExecution.getState().equals(ExecutionStatus.RUNNING));
            if (isRunningExecution) {
                throw new SchedulerValidationException("schedulerExecution is RUNNING. Please stop the execution before it can be deleted");
            }
            this.schedulerExecutionRepository.deleteBySchedulerConfigurationId(schedulerConfiguration.getId());
            this.schedulerConfigurationRepository.delete(schedulerConfiguration.getId());
        }
    }

    @Transactional(transactionManager = "lightminServerSchedulerTransactionManager", readOnly = true)
    public List<SchedulerConfiguration> findAll() {
        return this.schedulerConfigurationRepository.findAll();
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
