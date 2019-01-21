package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobLaunch;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.api.resource.util.ApiParameterParser;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerConfigurationNotFoundException;
import org.tuxdevelop.spring.batch.lightmin.util.DomainParameterParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExecutionRunner implements Runnable {

    private final SchedulerExecution schedulerExecution;
    private final ExecutionRunnerService executionRunnerService;

    public ExecutionRunner(
            final SchedulerExecution schedulerExecution,
            final ExecutionRunnerService executionRunnerService) {
        this.schedulerExecution = schedulerExecution;
        this.executionRunnerService = executionRunnerService;
    }

    @Override
    public void run() {
        try {
            final SchedulerConfiguration schedulerConfiguration =
                    this.executionRunnerService.findSchedulerConfigurationById(
                            this.schedulerExecution.getSchedulerConfigurationId());
            updateExecution(ExecutionStatus.RUNNING, Boolean.TRUE);
            try {
                fireJobLaunch(schedulerConfiguration);
                updateExecution(ExecutionStatus.FINISHED, Boolean.FALSE);
            } catch (final Exception e) {
                log.error("Execution for {} failed ", this.schedulerExecution, e);
                if (schedulerConfiguration.getRetriable() &&
                        this.schedulerExecution.getExecutionCount() <= schedulerConfiguration.getMaxRetries()) {
                    updateExecution(ExecutionStatus.FAILED, Boolean.FALSE);
                } else {
                    updateExecution(ExecutionStatus.LOST, Boolean.FALSE);
                }
            }
            createNextExecution(schedulerConfiguration.getCronExpression());
        } catch (final Exception e) {
            log.error("Error while processing scheduled job state {} ", this.schedulerExecution, e);
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
        this.executionRunnerService.saveSchedulerExecution(this.schedulerExecution);
    }

    private void createNextExecution(final String cronExpression) {
        try {
            this.executionRunnerService.createNextExecution(this.schedulerExecution, cronExpression);
        } catch (final Exception e) {
            //TODO: log and what to do?
        }
    }



    /*
     * EXECUTE JOB
     */

    private void fireJobLaunch(final SchedulerConfiguration schedulerConfiguration) {
        final String applicationName = schedulerConfiguration.getApplication();
        final String jobName = schedulerConfiguration.getJobName();
        final Integer instanceExecutionCount = schedulerConfiguration.getInstanceExecutionCount();
        //1. get job parameters
        final JobParameters jobParameters = getJobParameters(schedulerConfiguration);
        //2. get JobLaunch
        final JobLaunch jobLaunch = getJobLaunch(jobName, jobParameters);
        //3. get lightmin client instances
        final List<LightminClientApplication> lightminClientApplications =
                getLightminClientApplications(instanceExecutionCount, applicationName);
        //4. launch jobs on instances
        launchJobForInstances(jobLaunch, lightminClientApplications);
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
                this.executionRunnerService.findLightminApplicationsByName(applicationName);
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
        this.executionRunnerService.launchJob(jobLaunch, lightminClientApplication);
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

    private SchedulerConfiguration getSchedulerConfiguration(final Long id) throws SchedulerConfigurationNotFoundException {
        return this.executionRunnerService.findSchedulerConfigurationById(id);
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
