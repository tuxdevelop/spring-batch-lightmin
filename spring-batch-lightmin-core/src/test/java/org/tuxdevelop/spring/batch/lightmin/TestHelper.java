package org.tuxdevelop.spring.batch.lightmin;

import org.springframework.batch.core.*;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TestHelper {

    public static Job createJob(final String jobName) {
        return new Job() {
            @Override
            public boolean isRestartable() {
                return false;
            }

            @Override
            public String getName() {
                return jobName;
            }

            @Override
            public JobParametersValidator getJobParametersValidator() {
                return null;
            }

            @Override
            public JobParametersIncrementer getJobParametersIncrementer() {
                return null;
            }

            @Override
            public void execute(final JobExecution execution) {
            }
        };
    }

    public static List<JobInstance> createJobInstances(final long count, final String jobName) {
        final List<JobInstance> jobInstances = new LinkedList<JobInstance>();
        for (long i = 1; i <= count; i++) {
            final JobInstance jobInstance = new JobInstance(i, jobName);
            jobInstances.add(jobInstance);
        }
        return jobInstances;
    }

    public static JobInstance createJobInstance(final Long id, final String jobName) {
        return new JobInstance(id, jobName);
    }

    public static List<JobExecution> createJobExecutions(final long count) {
        final List<JobExecution> jobExecutions = new LinkedList<JobExecution>();
        for (long i = 1; i <= count; i++) {
            final JobExecution jobExecution = createJobExecution(i);
            jobExecutions.add(jobExecution);
        }
        return jobExecutions;
    }

    public static JobExecution createJobExecution(final Long jobExecutionId) {
        return new JobExecution(jobExecutionId);
    }

    public static StepExecution createStepExecution(final String stepName, final JobExecution jobExecution) {
        return new StepExecution(stepName, jobExecution);
    }

    public static JobConfiguration createJobConfiguration(final JobSchedulerConfiguration jobSchedulerConfiguration) {
        final JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setJobName("sampleJob");
        jobConfiguration.setJobIncrementer(JobIncrementer.DATE);
        jobConfiguration.setJobParameters(new HashMap<String, Object>());
        jobConfiguration.setJobSchedulerConfiguration(jobSchedulerConfiguration);
        return jobConfiguration;
    }

    public static JobSchedulerConfiguration createJobSchedulerConfiguration(final String cronExpression,
                                                                            final Long fixedDelay, final Long initialDelay, final JobSchedulerType jobSchedulerType) {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression(cronExpression);
        jobSchedulerConfiguration.setFixedDelay(fixedDelay);
        jobSchedulerConfiguration.setInitialDelay(initialDelay);
        jobSchedulerConfiguration.setJobSchedulerType(jobSchedulerType);
        jobSchedulerConfiguration.setTaskExecutorType(TaskExecutorType.SYNCHRONOUS);
        jobSchedulerConfiguration.setSchedulerStatus(SchedulerStatus.INITIALIZED);
        return jobSchedulerConfiguration;
    }

    public static org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration createJobConfiguration
            (final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerConfiguration jobSchedulerConfiguration) {
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration jobConfiguration = new org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration();
        jobConfiguration.setJobName("sampleJob");
        jobConfiguration.setJobIncrementer(org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer.DATE);
        jobConfiguration.setJobParameters(new JobParameters());
        jobConfiguration.setJobSchedulerConfiguration(jobSchedulerConfiguration);
        return jobConfiguration;
    }

    public static org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerConfiguration createJobSchedulerConfiguration(final String cronExpression,
                                                                                                                                    final Long fixedDelay,
                                                                                                                                    final Long initialDelay,
                                                                                                                                    final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType jobSchedulerType) {
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerConfiguration jobSchedulerConfiguration = new org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression(cronExpression);
        jobSchedulerConfiguration.setFixedDelay(fixedDelay);
        jobSchedulerConfiguration.setInitialDelay(initialDelay);
        jobSchedulerConfiguration.setJobSchedulerType(jobSchedulerType);
        jobSchedulerConfiguration.setTaskExecutorType(org.tuxdevelop.spring.batch.lightmin.api.resource.admin.TaskExecutorType.SYNCHRONOUS);
        jobSchedulerConfiguration.setSchedulerStatus(org.tuxdevelop.spring.batch.lightmin.api.resource.admin.SchedulerStatus.INITIALIZED);
        return jobSchedulerConfiguration;
    }
}
