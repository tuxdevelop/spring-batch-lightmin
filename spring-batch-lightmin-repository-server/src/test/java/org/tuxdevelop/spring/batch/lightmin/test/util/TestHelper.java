package org.tuxdevelop.spring.batch.lightmin.test.util;

import org.tuxdevelop.spring.batch.lightmin.admin.domain.*;

import java.util.HashMap;

public class TestHelper {

    public static JobConfiguration createJobConfiguration(final JobListenerConfiguration jobListenerConfiguration) {
        final JobConfiguration jobConfiguration = createJobConfiguration();
        jobConfiguration.setJobListenerConfiguration(jobListenerConfiguration);
        return jobConfiguration;
    }

    public static JobConfiguration createJobConfiguration(final JobSchedulerConfiguration jobSchedulerConfiguration) {
        final JobConfiguration jobConfiguration = createJobConfiguration();
        jobConfiguration.setJobSchedulerConfiguration(jobSchedulerConfiguration);
        return jobConfiguration;
    }

    private static JobConfiguration createJobConfiguration() {
        final JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setJobName("sampleJob");
        jobConfiguration.setJobIncrementer(JobIncrementer.DATE);
        jobConfiguration.setJobParameters(new HashMap<>());
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

    public static JobListenerConfiguration createJobListenerConfiguration(final String sourceFolder,
                                                                          final String pattern,
                                                                          final JobListenerType jobListenerType) {
        final JobListenerConfiguration jobListenerConfiguration = new JobListenerConfiguration();
        jobListenerConfiguration.setJobListenerType(jobListenerType);
        jobListenerConfiguration.setTaskExecutorType(TaskExecutorType.SYNCHRONOUS);
        jobListenerConfiguration.setSourceFolder(sourceFolder);
        jobListenerConfiguration.setFilePattern(pattern);
        jobListenerConfiguration.setPollerPeriod(1000L);
        jobListenerConfiguration.setListenerStatus(ListenerStatus.STOPPED);
        return jobListenerConfiguration;
    }

}
