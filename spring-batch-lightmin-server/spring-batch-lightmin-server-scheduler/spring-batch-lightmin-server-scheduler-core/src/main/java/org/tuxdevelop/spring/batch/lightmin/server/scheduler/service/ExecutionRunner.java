package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobLaunch;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.api.resource.util.ApiParameterParser;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.util.DomainParameterParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExecutionRunner implements Runnable {

    private final SchedulerExecution schedulerExecution;
    private final SchedulerConfigurationService schedulerConfigurationService;
    private final SchedulerExecutionService schedulerExecutionService;
    private final JobServerService jobServerService;
    private final LightminApplicationRepository lightminApplicationRepository;

    public ExecutionRunner(
            final SchedulerExecution schedulerExecution,
            final SchedulerConfigurationService schedulerConfigurationService,
            final SchedulerExecutionService schedulerExecutionService,
            final JobServerService jobServerService,
            final LightminApplicationRepository lightminApplicationRepository) {
        this.schedulerExecution = schedulerExecution;
        this.schedulerConfigurationService = schedulerConfigurationService;
        this.schedulerExecutionService = schedulerExecutionService;
        this.jobServerService = jobServerService;
        this.lightminApplicationRepository = lightminApplicationRepository;
    }

    @Override
    public void run() {
        SchedulerConfiguration schedulerConfiguration = null;
        try {
            updateExecution(ExecutionStatus.RUNNING, Boolean.TRUE);
            schedulerConfiguration = fireJobLaunch(this.schedulerExecution);
            updateExecution(ExecutionStatus.FINISHED, Boolean.FALSE);
        } catch (final Exception e) {
            //TODO: log
            updateExecution(ExecutionStatus.FAILED, Boolean.FALSE);
        }
        if (schedulerConfiguration != null) {
            createNextExecution(schedulerConfiguration.getCronExpression());
        } else {
            //TODO: what should be done if something went wrong?
        }
    }

    /*
     * HANDLE EXECUTION
     */

    private void updateExecution(final Integer status, final Boolean incrementCount) {
        if (incrementCount) {
            this.schedulerExecution.incrementExecutionCount();
        } else {
            log.trace("Count of the execution will not be increased");
        }
        this.schedulerExecution.setState(status);
        this.schedulerExecutionService.save(this.schedulerExecution);
    }

    private void createNextExecution(final String cronExpression) {
        try {
            this.schedulerExecutionService.createNextExecution(this.schedulerExecution, cronExpression);
        } catch (final Exception e) {
            //TODO: log and what to do?
        }
    }



    /*
     * EXECUTE JOB
     */

    private SchedulerConfiguration fireJobLaunch(final SchedulerExecution schedulerExecution) {
        final Long schedulerConfigurationId = schedulerExecution.getSchedulerConfigurationId();
        //1. get scheduler configuration
        final SchedulerConfiguration schedulerConfiguration = getSchedulerConfiguration(schedulerConfigurationId);
        final String applicationName = schedulerConfiguration.getApplication();
        final String jobName = schedulerConfiguration.getJobName();
        final Integer instanceExecutionCount = schedulerConfiguration.getInstanceExecutionCount();
        //2. get job parameters
        final JobParameters jobParameters = getJobParameters(schedulerConfiguration);
        //3. get JobLaunch
        final JobLaunch jobLaunch = getJobLaunch(jobName, jobParameters);
        //4. get lightmin client instances
        final List<LightminClientApplication> lightminClientApplications =
                getLightminClientApplications(instanceExecutionCount, applicationName);
        //5. launch jobs on instances
        launchJobForInstances(jobLaunch, lightminClientApplications);

        return schedulerConfiguration;
    }

    private void launchJobForInstances(final JobLaunch jobLaunch,
                                       final List<LightminClientApplication> lightminClientApplications) {
        for (final LightminClientApplication lightminClientApplication : lightminClientApplications) {
            this.launchJob(jobLaunch, lightminClientApplication);
        }
    }

    private List<LightminClientApplication> getLightminClientApplications(final Integer count,
                                                                          final String applicationName) {
        final List<LightminClientApplication> lightminClientApplications = new ArrayList<>();
        final Collection<LightminClientApplication> foundInstances =
                this.lightminApplicationRepository.findByApplicationName(applicationName);
        if (count > foundInstances.size()) {
            //TODO: decide how to handle
        } else {
            for (int i = 0; i < count; i++) {
                final LightminClientApplication clientApplication = foundInstances.iterator().next();
                lightminClientApplications.add(clientApplication);
            }
        }
        return lightminClientApplications;
    }


    private void launchJob(final JobLaunch jobLaunch, final LightminClientApplication lightminClientApplication) {
        this.jobServerService.launchJob(jobLaunch, lightminClientApplication);
    }

    private JobLaunch getJobLaunch(final String jobName, final JobParameters jobParameters) {
        final JobLaunch jobLaunch = new JobLaunch();
        jobLaunch.setJobName(jobName);
        jobLaunch.setJobParameters(jobParameters);
        return jobLaunch;
    }

    private JobParameters getJobParameters(final SchedulerConfiguration schedulerConfiguration) {
        final Map<String, Object> parametersMap = schedulerConfiguration.getJobParameters();
        final String parametersString = DomainParameterParser.parseParameterMapToString(parametersMap);
        final JobParameters jobParameters = ApiParameterParser.parseParametersToJobParameters(parametersString);
        attachIncremeter(schedulerConfiguration.getJobIncrementer(), jobParameters);
        return jobParameters;
    }

    private SchedulerConfiguration getSchedulerConfiguration(final Long id) {
        return this.schedulerConfigurationService.findById(id);
    }

    private void attachIncremeter(final JobIncrementer jobIncrementer, final JobParameters jobParameters) {
        if (JobIncrementer.DATE.equals(jobIncrementer)) {
            final JobParameter jobParameter = new JobParameter();
            jobParameter.setParameter(System.currentTimeMillis());
            jobParameter.setParameterType(ParameterType.LONG);
            jobParameters.getParameters().put(jobIncrementer.getIncrementerIdentifier(), jobParameter);
        } else {
            log.debug("nothing to map for job incremeter: {}", jobIncrementer);
        }
    }

}
