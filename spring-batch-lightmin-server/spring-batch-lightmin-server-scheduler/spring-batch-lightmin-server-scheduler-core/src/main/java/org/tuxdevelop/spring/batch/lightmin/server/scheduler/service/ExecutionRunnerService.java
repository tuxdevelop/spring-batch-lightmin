package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobLaunch;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ExecutionStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerConfigurationNotFoundException;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;

import java.util.Collection;

@Slf4j
public class ExecutionRunnerService {

    private final SchedulerConfigurationService schedulerConfigurationService;
    private final SchedulerExecutionService schedulerExecutionService;
    private final JobServerService jobServerService;
    private final LightminApplicationRepository lightminApplicationRepository;

    public ExecutionRunnerService(final SchedulerConfigurationService schedulerConfigurationService,
                                  final SchedulerExecutionService schedulerExecutionService,
                                  final JobServerService jobServerService,
                                  final LightminApplicationRepository lightminApplicationRepository) {
        this.schedulerConfigurationService = schedulerConfigurationService;
        this.schedulerExecutionService = schedulerExecutionService;
        this.jobServerService = jobServerService;
        this.lightminApplicationRepository = lightminApplicationRepository;
    }

    @Transactional(transactionManager = "lightminServerSchedulerTransactionManager", propagation = Propagation.REQUIRED)
    public void initSchedulerExecution(final SchedulerConfiguration schedulerConfiguration) {
        final SchedulerConfiguration savedConfiguration =
                this.schedulerConfigurationService.save(schedulerConfiguration);
        final SchedulerExecution execution = new SchedulerExecution();
        execution.setState(ExecutionStatus.NEW);
        execution.setNextFireTime(this.schedulerExecutionService.getNextFireTime(savedConfiguration.getCronExpression()));
        execution.setSchedulerConfigurationId(savedConfiguration.getId());
        execution.setExecutionCount(0);
        this.schedulerExecutionService.save(execution);
    }

    public void saveSchedulerExecution(final SchedulerExecution schedulerExecution) {
        this.schedulerExecutionService.save(schedulerExecution);
    }

    public void createNextExecution(final SchedulerExecution schedulerExecution,
                                    final String cronExpression) {
        if (this.schedulerConfigurationService.schedulerConfigurationExists(schedulerExecution.getSchedulerConfigurationId())) {
            this.schedulerExecutionService.createNextExecution(schedulerExecution, cronExpression);
        } else {
            log.warn("Could not create next execution for SchedulerExecution: {} , SchedulerConfiguration not present", schedulerExecution);
        }
    }

    public Collection<LightminClientApplication> findLightminApplicationsByName(final String name) {
        return this.lightminApplicationRepository.findByApplicationName(name);
    }

    public void launchJob(final JobLaunch jobLaunch, final LightminClientApplication lightminClientApplication) {
        this.jobServerService.launchJob(jobLaunch, lightminClientApplication);
    }

    public SchedulerConfiguration findSchedulerConfigurationById(final Long id) throws SchedulerConfigurationNotFoundException {
        return this.schedulerConfigurationService.findById(id);
    }

}
