package org.tuxdevelop.spring.batch.lightmin.test.api;

import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;

public class ApiTestHelper {


    public static org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration createJobConfiguration
            (final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerConfiguration jobSchedulerConfiguration) {
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration jobConfiguration =
                createJobConfigurationApi();
        jobConfiguration.setJobSchedulerConfiguration(jobSchedulerConfiguration);
        return jobConfiguration;
    }

    public static org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration createJobConfiguration
            (final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerConfiguration jobListenerConfiguration) {
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration jobConfiguration =
                createJobConfigurationApi();
        jobConfiguration.setJobListenerConfiguration(jobListenerConfiguration);
        return jobConfiguration;
    }

    private static org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration createJobConfigurationApi
            () {
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration jobConfiguration = new org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration();
        jobConfiguration.setJobName("sampleJob");
        jobConfiguration.setJobIncrementer(org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer.DATE);
        jobConfiguration.setJobParameters(new JobParameters());
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

    public static org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerConfiguration createJobListenerConfiguration(final String sourceFolder,
                                                                                                                                  final String pattern,
                                                                                                                                  final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerType jobListenerType) {
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerConfiguration jobListenerConfiguration
                = new org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerConfiguration();
        jobListenerConfiguration.setJobListenerType(jobListenerType);
        jobListenerConfiguration.setTaskExecutorType(org.tuxdevelop.spring.batch.lightmin.api.resource.admin.TaskExecutorType.SYNCHRONOUS);
        jobListenerConfiguration.setSourceFolder(sourceFolder);
        jobListenerConfiguration.setFilePattern(pattern);
        jobListenerConfiguration.setPollerPeriod(1000L);
        jobListenerConfiguration.setListenerStatus(org.tuxdevelop.spring.batch.lightmin.api.resource.admin.ListenerStatus.STOPPED);
        return jobListenerConfiguration;
    }
}
