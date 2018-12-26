package org.tuxdevelop.spring.batch.lightmin.client.api;


import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DomainToResourceMapperTest {

    @Test
    public void testMapJobConfigurations() {
        final Collection<JobConfiguration> jobConfigurations = new LinkedList<>();
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("double", 10.1);
        parameters.put("long", 20L);
        jobConfiguration.setJobParameters(parameters);
        jobConfigurations.add(jobConfiguration);
        final JobConfigurations result = DomainToResourceMapper.map(jobConfigurations);
        this.assertJobConfigurations(result, jobConfigurations);
    }

    private void assertJobConfigurations(final JobConfigurations jobConfigurations, final Collection<JobConfiguration> jobConfigurationCollection) {
        for (final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration jobConfiguration : jobConfigurations.getJobConfigurations()) {
            final JobConfiguration compareJobConfiguration = this.getById(jobConfiguration.getJobConfigurationId(),
                    jobConfigurationCollection);
            this.assertJobConfiguration(jobConfiguration, compareJobConfiguration);
        }
    }

    private void assertJobConfiguration(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin
            .JobConfiguration jobConfiguration, final JobConfiguration compareWith) {

        assertThat(jobConfiguration).isNotNull();
        assertThat(compareWith).isNotNull();
        assertThat(jobConfiguration.getJobName()).isEqualTo(compareWith.getJobName());
        assertThat(jobConfiguration.getJobConfigurationId()).isEqualTo(compareWith.getJobConfigurationId());
        this.assertJobParameters(jobConfiguration.getJobParameters(), compareWith.getJobParameters());
        this.assertJobSchedulerConfiguration(jobConfiguration.getJobSchedulerConfiguration(), compareWith
                .getJobSchedulerConfiguration());
        this.assertJobListenerConfiguration(jobConfiguration.getJobListenerConfiguration(), compareWith.getJobListenerConfiguration());
    }

    private void assertJobSchedulerConfiguration(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin
            .JobSchedulerConfiguration jobSchedulerConfiguration, final JobSchedulerConfiguration compareWith) {
        assertThat(jobSchedulerConfiguration.getFixedDelay()).isEqualTo(compareWith.getFixedDelay());
        assertThat(jobSchedulerConfiguration.getInitialDelay()).isEqualTo(compareWith.getInitialDelay());
        assertThat(jobSchedulerConfiguration.getCronExpression()).isEqualTo(compareWith.getCronExpression());
        this.assertJobSchedulerType(jobSchedulerConfiguration.getJobSchedulerType(), compareWith.getJobSchedulerType());
        this.assertSchedulerStatus(jobSchedulerConfiguration.getSchedulerStatus(), compareWith.getSchedulerStatus());
        this.assertTaskExecuorType(jobSchedulerConfiguration.getTaskExecutorType(), compareWith.getTaskExecutorType());

    }

    private void assertJobListenerConfiguration(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerConfiguration jobListenerConfiguration,
                                                final JobListenerConfiguration compareWith) {
        if (jobListenerConfiguration != null) {
            assertThat(jobListenerConfiguration.getFilePattern()).isEqualTo(compareWith.getFilePattern());
            assertThat(jobListenerConfiguration.getSourceFolder()).isEqualTo(compareWith.getSourceFolder());
            assertThat(jobListenerConfiguration.getPollerPeriod()).isEqualTo(compareWith.getPollerPeriod());
        } else {
            assertThat(compareWith).isNull();
        }

    }

    private void assertJobSchedulerType(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin
            .JobSchedulerType jobSchedulerType, final JobSchedulerType compareWith) {
        assertThat(jobSchedulerType.name()).isEqualTo(compareWith.name());
    }

    private void assertTaskExecuorType(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin
            .TaskExecutorType taskExecutorType, final TaskExecutorType compareWith) {
        assertThat(taskExecutorType.name()).isEqualTo(compareWith.name());
    }

    private void assertSchedulerStatus(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin
            .SchedulerStatus schedulerStatus, final SchedulerStatus compareWith) {
        assertThat(schedulerStatus.name()).isEqualTo(compareWith.name());
    }

    private void assertJobParameters(final JobParameters jobParameters, final Map<String, Object> jobParameterMap) {
        for (final Map.Entry<String, JobParameter> entry : jobParameters.getParameters().entrySet()) {
            assertThat(jobParameterMap.containsKey(entry.getKey()));
            final Object expected = jobParameterMap.get(entry.getKey());
            final JobParameter jobParameter = entry.getValue();
            assertThat(jobParameter.getParameter()).isEqualTo(expected);
        }
    }

    private JobConfiguration getById(final Long id, final Collection<JobConfiguration> jobConfigurations) {
        JobConfiguration result = null;
        for (final JobConfiguration jobConfiguration : jobConfigurations) {
            if (id.equals(jobConfiguration.getJobConfigurationId())) {
                result = jobConfiguration;
                break;
            }
        }
        return result;
    }


}
