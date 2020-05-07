package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain;

import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;

import java.util.HashMap;

public final class SchedulerConfigurationTestHelper {

    private SchedulerConfigurationTestHelper() {
    }


    public static SchedulerConfiguration createSchedulerConfiguration() {
        return createSchedulerConfiguration("testApplication", "testJob");
    }

    public static SchedulerConfiguration createSchedulerConfiguration(final String applicationName,
                                                                      final String jobName) {
        final SchedulerConfiguration schedulerConfiguration = new SchedulerConfiguration();
        schedulerConfiguration.setApplication(applicationName);
        schedulerConfiguration.setCronExpression("0 0/2 0 ? * * *");
        schedulerConfiguration.setInstanceExecutionCount(1);
        schedulerConfiguration.setJobIncrementer(JobIncrementer.DATE);
        schedulerConfiguration.setJobName(jobName);
        schedulerConfiguration.setJobParameters(new HashMap<>());
        schedulerConfiguration.setMaxRetries(3);
        schedulerConfiguration.setStatus(ServerSchedulerStatus.ACTIVE);
        schedulerConfiguration.setRetryable(Boolean.TRUE);
        schedulerConfiguration.setRetryInterval(2000L);

        return schedulerConfiguration;

    }
}