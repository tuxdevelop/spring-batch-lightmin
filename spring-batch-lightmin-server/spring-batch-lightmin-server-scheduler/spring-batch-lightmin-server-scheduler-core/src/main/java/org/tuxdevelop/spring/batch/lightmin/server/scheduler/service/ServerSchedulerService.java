package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobLaunch;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.exception.SchedulerRuntimException;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ExecutionStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ServerSchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerConfigurationNotFoundException;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;

import java.util.Collection;
import java.util.Date;

@Slf4j
public class ServerSchedulerService {

    private final SchedulerConfigurationService schedulerConfigurationService;
    private final SchedulerExecutionService schedulerExecutionService;
    private final JobServerService jobServerService;
    private final LightminApplicationRepository lightminApplicationRepository;

    public ServerSchedulerService(final SchedulerConfigurationService schedulerConfigurationService,
                                  final SchedulerExecutionService schedulerExecutionService,
                                  final JobServerService jobServerService,
                                  final LightminApplicationRepository lightminApplicationRepository) {
        this.schedulerConfigurationService = schedulerConfigurationService;
        this.schedulerExecutionService = schedulerExecutionService;
        this.jobServerService = jobServerService;
        this.lightminApplicationRepository = lightminApplicationRepository;
    }

    @Transactional(
            transactionManager = "lightminServerSchedulerTransactionManager",
            propagation = Propagation.REQUIRED)
    public void initSchedulerExecution(final SchedulerConfiguration schedulerConfiguration) {
        final SchedulerConfiguration savedConfiguration =
                this.schedulerConfigurationService.save(schedulerConfiguration);
        if (ServerSchedulerStatus.ACTIVE.equals(schedulerConfiguration.getStatus())) {
            final SchedulerExecution execution = new SchedulerExecution();
            execution.setState(ExecutionStatus.NEW);
            execution.setNextFireTime(this.schedulerExecutionService.getNextFireTime(savedConfiguration.getCronExpression()));
            execution.setSchedulerConfigurationId(savedConfiguration.getId());
            execution.setExecutionCount(0);
            execution.setLastUpdate(new Date());
            this.schedulerExecutionService.save(execution);
        } else {
            log.info("Status of ServerSchedulerConfiguration with id {}, is STOPPED, nothing to initiate!", schedulerConfiguration.getId());
        }
    }

    @Transactional(
            transactionManager = "lightminServerSchedulerTransactionManager",
            propagation = Propagation.REQUIRED)
    public void saveSchedulerConfiguration(final SchedulerConfiguration schedulerConfiguration) {
        if (schedulerConfiguration.getId() != null) {
            this.updateSchedulerConfiguration(schedulerConfiguration);
        } else {
            this.initSchedulerExecution(schedulerConfiguration);
        }
    }

    @Transactional(
            transactionManager = "lightminServerSchedulerTransactionManager",
            propagation = Propagation.REQUIRED)
    public void updateSchedulerConfiguration(final SchedulerConfiguration schedulerConfiguration) {
        //deleting all planned executions for scheduler configuration
        this.schedulerExecutionService.deleteByConfigurationIdAndState(schedulerConfiguration.getId(), ExecutionStatus.NEW);
        //init a new scheduler execution
        this.initSchedulerExecution(schedulerConfiguration);
    }

    @Transactional(
            transactionManager = "lightminServerSchedulerTransactionManager",
            propagation = Propagation.REQUIRED)
    public void saveSchedulerExecution(final SchedulerExecution schedulerExecution) {
        this.schedulerExecutionService.save(schedulerExecution);
    }

    @Transactional(
            transactionManager = "lightminServerSchedulerTransactionManager",
            propagation = Propagation.REQUIRED)
    public void createNextExecution(final SchedulerExecution schedulerExecution,
                                    final String cronExpression) {
        if (this.schedulerConfigurationService.schedulerConfigurationExists(schedulerExecution.getSchedulerConfigurationId())) {
            this.schedulerExecutionService.createNextExecution(schedulerExecution, cronExpression);
        } else {
            log.warn("Could not create next execution for SchedulerExecution: {} , SchedulerConfiguration not present", schedulerExecution);
        }
    }

    @Transactional(
            transactionManager = "lightminServerSchedulerTransactionManager",
            propagation = Propagation.REQUIRED,
            readOnly = true)
    public Collection<LightminClientApplication> findLightminApplicationsByName(final String name) {
        return this.lightminApplicationRepository.findByApplicationName(name);
    }

    @Transactional(
            transactionManager = "lightminServerSchedulerTransactionManager",
            propagation = Propagation.REQUIRED,
            readOnly = true)
    public SchedulerConfiguration findSchedulerConfigurationById(final Long id) {
        try {
            return this.schedulerConfigurationService.findById(id);
        } catch (final SchedulerConfigurationNotFoundException e) {
            throw new SchedulerRuntimException(e);
        }
    }

    @Transactional(
            transactionManager = "lightminServerSchedulerTransactionManager",
            propagation = Propagation.REQUIRED)
    public void stopExecution(final Long executionId) {
        final SchedulerExecution execution = this.schedulerExecutionService.findById(executionId);
        final Long configurationId = execution.getSchedulerConfigurationId();
        this.schedulerExecutionService.deleteByConfigurationIdAndState(configurationId, ExecutionStatus.NEW);
        log.warn("Stop execution with the id {} will remove all upcoming executions for the configuration with the id {}", executionId, configurationId);
    }

    @Transactional(
            transactionManager = "lightminServerSchedulerTransactionManager",
            propagation = Propagation.REQUIRED)
    public SchedulerConfiguration disableServerSchedulerConfiguration(final Long configurationId) {
        final SchedulerConfiguration configuration = this.findSchedulerConfigurationById(configurationId);
        if (ServerSchedulerStatus.ACTIVE.equals(configuration.getStatus())) {
            configuration.setStatus(ServerSchedulerStatus.STOPPED);
            this.updateSchedulerConfiguration(configuration);
        } else {
            log.info("ServerSchedulerStatus is already STOPPED, nothing to do!");
        }
        return configuration;
    }

    @Transactional(
            transactionManager = "lightminServerSchedulerTransactionManager",
            propagation = Propagation.REQUIRED)
    public void startServerSchedulerConfiguration(final Long configurationId) {
        final SchedulerConfiguration configuration = this.findSchedulerConfigurationById(configurationId);
        if (ServerSchedulerStatus.STOPPED.equals(configuration.getStatus())) {
            configuration.setStatus(ServerSchedulerStatus.ACTIVE);
            this.initSchedulerExecution(configuration);
        } else {
            log.info("ServerSchedulerStatus is already STOPPED, nothing to do!");
        }
    }

    @Transactional(
            transactionManager = "lightminServerSchedulerTransactionManager",
            propagation = Propagation.REQUIRED)
    public void deleteServerSchedulerConfiguration(final Long configurationId) {
        final SchedulerConfiguration configuration = this.disableServerSchedulerConfiguration(configurationId);
        this.schedulerConfigurationService.delete(configuration);
    }

    public void launchJob(final JobLaunch jobLaunch, final LightminClientApplication lightminClientApplication) {
        this.jobServerService.launchJob(jobLaunch, lightminClientApplication);
    }

}
