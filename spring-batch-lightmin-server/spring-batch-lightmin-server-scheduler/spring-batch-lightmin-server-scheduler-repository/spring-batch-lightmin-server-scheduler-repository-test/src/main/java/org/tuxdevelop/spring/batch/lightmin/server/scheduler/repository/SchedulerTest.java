package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import org.assertj.core.api.BDDAssertions;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;

import java.util.HashMap;
import java.util.Map;

public abstract class SchedulerTest {

    protected SchedulerConfiguration createSchedulerConfiguration(final String applicationName) {
        final SchedulerConfiguration schedulerConfiguration = new SchedulerConfiguration();
        schedulerConfiguration.setApplication(applicationName);
        schedulerConfiguration.setJobName("testJob");
        schedulerConfiguration.setMaxRetries(5);
        schedulerConfiguration.setInstanceExecutionCount(0);
        schedulerConfiguration.setRetriable(Boolean.TRUE);
        schedulerConfiguration.setCronExpression("* * * * * *");
        schedulerConfiguration.setJobIncrementer(JobIncrementer.DATE);
        final Map<String, Object> jobParameters = new HashMap<>();
        jobParameters.put("LONG", 200L);
        jobParameters.put("STRING", "hello");
        schedulerConfiguration.setJobParameters(jobParameters);
        final SchedulerConfiguration result = getSchedulerConfigurationRepository().save(schedulerConfiguration);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getId()).isNotNull();
        return result;
    }

    public abstract SchedulerConfigurationRepository getSchedulerConfigurationRepository();

    public abstract CleanUpRepository getCleanUpRepository();


}
