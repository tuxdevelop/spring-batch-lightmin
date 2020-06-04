package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobLaunch;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.api.resource.util.ApiParameterParser;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration.ServerSchedulerCoreConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.exception.SchedulerRuntimException;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ExecutionStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.util.DomainParameterParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExecutionRunner implements Runnable {

    private final SchedulerExecution schedulerExecution;
    private final ServerSchedulerService serverSchedulerService;
    private final ServerSchedulerCoreConfigurationProperties properties;
    private final SchedulerConfiguration schedulerConfiguration;


    public ExecutionRunner(
            final SchedulerExecution schedulerExecution,
            final ServerSchedulerService serverSchedulerService,
            final ServerSchedulerCoreConfigurationProperties properties) {
        this.schedulerExecution = schedulerExecution;
        this.serverSchedulerService = serverSchedulerService;
        this.properties = properties;
        this.schedulerConfiguration =
                this.serverSchedulerService.findSchedulerConfigurationById(this.schedulerExecution.getSchedulerConfigurationId());
    }

    @Override
    public void run() {
        try {
            this.updateExecution(ExecutionStatus.RUNNING, Boolean.TRUE);
            Integer finalStatus;
            try {
                this.fireJobLaunch(this.schedulerConfiguration);
                this.updateExecution(ExecutionStatus.FINISHED, Boolean.FALSE);
                finalStatus = ExecutionStatus.FINISHED;
            } catch (final Exception e) {
                log.error("Execution for {} failed ", this.schedulerExecution, e);
                if (this.schedulerConfiguration.getRetryable() &&
                        this.schedulerExecution.getExecutionCount() <= this.schedulerConfiguration.getMaxRetries()) {
                    this.updateExecution(ExecutionStatus.FAILED, Boolean.FALSE, Boolean.TRUE);
                    finalStatus = ExecutionStatus.FAILED;
                } else {
                    this.updateExecution(ExecutionStatus.LOST, Boolean.FALSE);
                    finalStatus = ExecutionStatus.LOST;
                }
            }
            if (ExecutionStatus.FINISHED.equals(finalStatus)) {
                this.createNextExecution(this.schedulerConfiguration.getCronExpression());
            } else if (ExecutionStatus.FAILED.equals(finalStatus)) {
                if (this.properties.getCreateNewExecutionsOnFailure()) {
                    this.createNextExecution(this.schedulerConfiguration.getCronExpression());
                } else {
                    log.info("Not creating new execution for SchedulerConfiguration with the id " + this.schedulerConfiguration.getId() + " ! Reason: Execution status is FAILED!");
                }
            } else {
                if (this.properties.getCreateNewExecutionsOnLost()) {
                    this.createNextExecution(this.schedulerConfiguration.getCronExpression());
                } else {
                    log.info("Not creating new execution for SchedulerConfiguration with the id " + this.schedulerConfiguration.getId() + " ! Reason: Execution status is LOST!");
                }
            }
        } catch (final Exception e) {
            log.error("Error while processing scheduled job state {} ", this.schedulerExecution, e);
            this.updateExecution(ExecutionStatus.FAILED, Boolean.TRUE);
        }
    }

    /*
     * HANDLE EXECUTION
     */

    private void updateExecution(final Integer status, final Boolean incrementCount, final Boolean determineNextRetry) {

        if (incrementCount) {
            this.schedulerExecution.incrementExecutionCount();
        } else {
            log.trace("Count of the execution will not be increased");
        }
        this.schedulerExecution.setState(status);
        this.schedulerExecution.setLastUpdate(new Date());
        if (determineNextRetry) {
            this.determineNextRetryDate(this.schedulerExecution);
        } else {
            log.trace("No next retry will be determined");
        }
        this.serverSchedulerService.saveSchedulerExecution(this.schedulerExecution);

    }

    private void determineNextRetryDate(final SchedulerExecution schedulerExecution) {
        if (this.schedulerConfiguration.getRetryable()) {
            final Long retryInterval = this.schedulerConfiguration.getRetryInterval();
            if (retryInterval != null) {
                //TODO: check if this is a good idea
                final Date nextRetry = new Date(System.currentTimeMillis() + retryInterval);
                schedulerExecution.setNextRetry(nextRetry);
            } else {
                log.debug("No retry interval defined, nothing todo");
            }
        } else {
            log.debug("SchedulerConfiguration {} is not retryable", this.schedulerConfiguration);
        }
    }

    private void updateExecution(final Integer status, final Boolean incrementCount) {
        this.updateExecution(status, incrementCount, Boolean.FALSE);
    }

    private void createNextExecution(final String cronExpression) {
        try {
            this.serverSchedulerService.createNextExecution(this.schedulerExecution, cronExpression);
        } catch (final Exception e) {
            log.error("Error while creating next execution for {}", this.schedulerExecution, e);
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
        final JobParameters jobParameters = this.getJobParameters(schedulerConfiguration);
        //2. get JobLaunch
        final JobLaunch jobLaunch = this.getJobLaunch(jobName, jobParameters);
        //3. get lightmin client instances
        final List<LightminClientApplication> lightminClientApplications =
                this.getLightminClientApplications(instanceExecutionCount, applicationName);
        //4. launch jobs on instances
        this.launchJobForInstances(jobLaunch, lightminClientApplications);
    }

    private void launchJobForInstances(final JobLaunch jobLaunch,
                                       final List<LightminClientApplication> lightminClientApplications) {

        if (lightminClientApplications != null) {
            lightminClientApplications.forEach(
                    application -> {
                        this.launchJob(jobLaunch, application);
                    }
            );
        } else {
            log.warn("No lightmin applications availabe, skipping joblauch {}", jobLaunch);
        }
    }

    private List<LightminClientApplication> getLightminClientApplications(final Integer count,
                                                                          final String applicationName) {
        final List<LightminClientApplication> lightminClientApplications = new ArrayList<>();
        final List<LightminClientApplication> foundInstances =
                new ArrayList<>(this.serverSchedulerService.findLightminApplicationsByName(applicationName));
        if (count > foundInstances.size()) {
            if (this.properties.getFailOnInstanceExecutionCount()) {
                throw new SchedulerRuntimException("Desired Instance Execution Count: " + count + " Found: "
                        + foundInstances.size() + "! spring.batch.lightmin.server.scheduler.failOnInstanceExecutionCount is set to TRUE. Failing!");
            } else {
                log.warn("Desired Instance Execution Count: " + count + " Found: "
                        + foundInstances.size() + "! Executing on available instances");
            }
        } else {
            for (int i = 0; i < count; i++) {
                lightminClientApplications.add(foundInstances.get(i));
            }
        }
        return lightminClientApplications;
    }


    private void launchJob(final JobLaunch jobLaunch, final LightminClientApplication lightminClientApplication) {
        log.debug("Launching {} for client instance {}", jobLaunch, lightminClientApplication);
        this.serverSchedulerService.launchJob(jobLaunch, lightminClientApplication);
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
        this.attachIncrementer(schedulerConfiguration.getJobIncrementer(), jobParameters);
        return jobParameters;
    }

    private void attachIncrementer(final JobIncrementer jobIncrementer, final JobParameters jobParameters) {
        if (JobIncrementer.DATE.equals(jobIncrementer)) {
            final JobParameter jobParameter = new JobParameter();
            jobParameter.setParameter(System.currentTimeMillis());
            jobParameter.setParameterType(ParameterType.LONG);
            jobParameters.getParameters().put(jobIncrementer.getIncrementerIdentifier(), jobParameter);
        } else {
            log.debug("nothing to map for job incrementer: {}", jobIncrementer);
        }
    }

}
