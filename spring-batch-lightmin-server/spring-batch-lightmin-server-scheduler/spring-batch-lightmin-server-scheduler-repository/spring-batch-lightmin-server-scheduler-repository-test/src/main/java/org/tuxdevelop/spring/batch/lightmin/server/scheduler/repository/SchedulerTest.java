package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import org.assertj.core.api.BDDAssertions;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ServerSchedulerStatus;

import java.util.HashMap;
import java.util.Map;

public abstract class SchedulerTest {

    protected SchedulerConfiguration createSchedulerConfiguration(final String applicationName) {
        return this.createSchedulerConfiguration(applicationName, Boolean.TRUE);
    }

    protected SchedulerConfiguration createSchedulerConfiguration(final String applicationName, final Boolean retry) {
        final SchedulerConfiguration schedulerConfiguration = new SchedulerConfiguration();
        schedulerConfiguration.setApplication(applicationName);
        schedulerConfiguration.setJobName("testJob");

        schedulerConfiguration.setInstanceExecutionCount(0);
        schedulerConfiguration.setRetryable(retry);
        if (retry) {
            schedulerConfiguration.setMaxRetries(5);
            schedulerConfiguration.setRetryInterval(2000L);
        } else {
            schedulerConfiguration.setMaxRetries(null);
            schedulerConfiguration.setRetryInterval(null);
        }
        schedulerConfiguration.setCronExpression("* * * * * *");
        schedulerConfiguration.setJobIncrementer(JobIncrementer.DATE);
        final Map<String, Object> jobParameters = new HashMap<>();
        jobParameters.put("LONG", 200L);
        jobParameters.put("STRING", "hello");
        schedulerConfiguration.setJobParameters(jobParameters);
        schedulerConfiguration.setStatus(ServerSchedulerStatus.ACTIVE);
        final SchedulerConfiguration result = this.getSchedulerConfigurationRepository().save(schedulerConfiguration);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getId()).isNotNull();
        return result;
    }

    public abstract SchedulerConfigurationRepository getSchedulerConfigurationRepository();

    public abstract CleanUpRepository getCleanUpRepository();


}
