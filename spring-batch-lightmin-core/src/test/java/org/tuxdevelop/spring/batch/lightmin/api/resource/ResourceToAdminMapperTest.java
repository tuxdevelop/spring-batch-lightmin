package org.tuxdevelop.spring.batch.lightmin.api.resource;

import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class ResourceToAdminMapperTest {

    @Test
    public void testMapJobConfigurations() {
        final Collection<org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration> jobConfigurations = new LinkedList<>();
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType.PERIOD);
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        final Map<String, JobParameter> parameters = new HashMap<>();
        final JobParameter jobParameterDouble = new JobParameter();
        jobParameterDouble.setParameter(10.1);
        jobParameterDouble.setParameterType(ParameterType.DOUBLE);
        final JobParameter jobParameterLong = new JobParameter();
        jobParameterLong.setParameter(20L);
        jobParameterLong.setParameterType(ParameterType.LONG);
        parameters.put("double", jobParameterDouble);
        parameters.put("long", jobParameterLong);
        final JobParameters jobParameters = new JobParameters();
        jobParameters.setParameters(parameters);
        jobConfiguration.setJobParameters(jobParameters);
        jobConfigurations.add(jobConfiguration);
        final JobConfigurations jobConfigurationsToMap = new JobConfigurations();
        jobConfigurationsToMap.setJobConfigurations(jobConfigurations);
        final Collection<JobConfiguration> result = ResourceToAdminMapper.map(jobConfigurationsToMap);
        assertJobConfigurations(result, jobConfigurationsToMap);
    }

    @Test
    public void testMapJobConfigurationsWithListener() {
        final Collection<org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration> jobConfigurations = new LinkedList<>();
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerConfiguration jobListenerConfiguration = TestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerType.LOCAL_FOLDER_LISTENER);
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobListenerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        final Map<String, JobParameter> parameters = new HashMap<>();
        final JobParameter jobParameterDouble = new JobParameter();
        jobParameterDouble.setParameter(10.1);
        jobParameterDouble.setParameterType(ParameterType.DOUBLE);
        final JobParameter jobParameterLong = new JobParameter();
        jobParameterLong.setParameter(20L);
        jobParameterLong.setParameterType(ParameterType.LONG);
        parameters.put("double", jobParameterDouble);
        parameters.put("long", jobParameterLong);
        final JobParameters jobParameters = new JobParameters();
        jobParameters.setParameters(parameters);
        jobConfiguration.setJobParameters(jobParameters);
        jobConfigurations.add(jobConfiguration);
        final JobConfigurations jobConfigurationsToMap = new JobConfigurations();
        jobConfigurationsToMap.setJobConfigurations(jobConfigurations);
        final Collection<JobConfiguration> result = ResourceToAdminMapper.map(jobConfigurationsToMap);
        assertJobConfigurations(result, jobConfigurationsToMap);
    }

    private void assertJobConfigurations(final Collection<JobConfiguration> jobConfigurationCollection, final JobConfigurations jobConfigurations) {
        for (final JobConfiguration jobConfiguration : jobConfigurationCollection) {
            final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration compareWith = getById
                    (jobConfiguration.getJobConfigurationId(), jobConfigurations);
            assertJobConfiguration(jobConfiguration, compareWith);
        }
    }

    private void assertJobConfiguration(final JobConfiguration jobConfiguration,
                                        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration compareWith) {
        assertThat(jobConfiguration).isNotNull();
        assertThat(compareWith).isNotNull();
        assertThat(jobConfiguration.getJobName()).isEqualTo(compareWith.getJobName());
        assertThat(jobConfiguration.getJobConfigurationId()).isEqualTo(compareWith.getJobConfigurationId());
        assertJobParameters(jobConfiguration.getJobParameters(), compareWith.getJobParameters());
        assertJobSchedulerConfiguration(jobConfiguration.getJobSchedulerConfiguration(), compareWith
                .getJobSchedulerConfiguration());
        assertJobListenerConfiguration(jobConfiguration.getJobListenerConfiguration(), compareWith.getJobListenerConfiguration());
    }

    private void assertJobSchedulerConfiguration(final JobSchedulerConfiguration jobSchedulerConfiguration,
                                                 final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerConfiguration compareWith) {
        if (jobSchedulerConfiguration != null) {
            assertThat(jobSchedulerConfiguration.getFixedDelay()).isEqualTo(compareWith.getFixedDelay());
            assertThat(jobSchedulerConfiguration.getInitialDelay()).isEqualTo(compareWith.getInitialDelay());
            assertThat(jobSchedulerConfiguration.getCronExpression()).isEqualTo(compareWith.getCronExpression());
            assertJobSchedulerType(jobSchedulerConfiguration.getJobSchedulerType(), compareWith.getJobSchedulerType());
            assertSchedulerStatus(jobSchedulerConfiguration.getSchedulerStatus(), compareWith.getSchedulerStatus());
            assertTaskExecuorType(jobSchedulerConfiguration.getTaskExecutorType(), compareWith.getTaskExecutorType());
        } else {
            assertThat(compareWith).isNull();
        }
    }

    private void assertJobListenerConfiguration(final JobListenerConfiguration jobListenerConfiguration,
                                                final org.tuxdevelop.spring.batch.lightmin.api.resource.admin
                                                        .JobListenerConfiguration compareWith) {
        if (jobListenerConfiguration != null) {
            assertThat(jobListenerConfiguration.getFilePattern()).isEqualTo(compareWith.getFilePattern());
            assertThat(jobListenerConfiguration.getSourceFolder()).isEqualTo(compareWith.getSourceFolder());
            assertThat(jobListenerConfiguration.getPollerPeriod()).isEqualTo(compareWith.getPollerPeriod());
        } else {
            assertThat(compareWith).isNull();
        }

    }

    private void assertJobSchedulerType(final JobSchedulerType jobSchedulerType,
                                        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType compareWith) {
        assertThat(jobSchedulerType.name()).isEqualTo(compareWith.name());
    }

    private void assertTaskExecuorType(final TaskExecutorType taskExecutorType,
                                       final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.TaskExecutorType compareWith) {
        assertThat(taskExecutorType.name()).isEqualTo(compareWith.name());
    }

    private void assertSchedulerStatus(final SchedulerStatus schedulerStatus,
                                       final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.SchedulerStatus compareWith) {
        assertThat(schedulerStatus.name()).isEqualTo(compareWith.name());
    }

    private void assertJobParameters(final Map<String, Object> jobParameterMap, final JobParameters jobParameters) {
        for (final Map.Entry<String, JobParameter> entry : jobParameters.getParameters().entrySet()) {
            assertThat(jobParameterMap.containsKey(entry.getKey()));
            final Object expected = jobParameterMap.get(entry.getKey());
            final JobParameter jobParameter = entry.getValue();
            assertThat(jobParameter.getParameter()).isEqualTo(expected);
        }
    }

    private org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration getById(final Long id, final JobConfigurations jobConfigurations) {
        org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration result = null;
        for (final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration jobConfiguration :
                jobConfigurations.getJobConfigurations()) {
            if (id.equals(jobConfiguration.getJobConfigurationId())) {
                result = jobConfiguration;
                break;
            }
        }
        return result;
    }
}
