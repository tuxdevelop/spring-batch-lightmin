package org.tuxdevelop.spring.batch.lightmin.client.api;

import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.test.api.ApiTestHelper;
import org.tuxdevelop.spring.batch.lightmin.util.DomainParameterParser;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


public class ResourceToDomainMapperTest {

    @Test
    public void testMapJobConfigurations() {
        final Collection<org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration> jobConfigurations = new LinkedList<>();
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerConfiguration jobSchedulerConfiguration = ApiTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType.PERIOD);
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration jobConfiguration = ApiTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        final Map<String, JobParameter> parameters = new HashMap<>();
        final JobParameter jobParameterDouble = new JobParameter();
        jobParameterDouble.setParameter(10.1);
        jobParameterDouble.setParameterType(ParameterType.DOUBLE);
        final JobParameter jobParameterLong = new JobParameter();
        jobParameterLong.setParameter(20L);
        jobParameterLong.setParameterType(ParameterType.LONG);
        final JobParameter jobParameterDate = new JobParameter();
        jobParameterDate.setParameter("2017/02/10 13:42:00:001");
        jobParameterDate.setParameterType(ParameterType.DATE);
        parameters.put("double", jobParameterDouble);
        parameters.put("long", jobParameterLong);
        parameters.put("date", jobParameterDate);
        final JobParameters jobParameters = new JobParameters();
        jobParameters.setParameters(parameters);
        jobConfiguration.setJobParameters(jobParameters);
        jobConfigurations.add(jobConfiguration);
        final JobConfigurations jobConfigurationsToMap = new JobConfigurations();
        jobConfigurationsToMap.setJobConfigurations(jobConfigurations);
        final Collection<JobConfiguration> result = ResourceToDomainMapper.map(jobConfigurationsToMap);
        this.assertJobConfigurations(result, jobConfigurationsToMap);
    }

    @Test
    public void testMapJobConfigurationsWithListener() {
        final Collection<org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration> jobConfigurations = new LinkedList<>();
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerConfiguration jobListenerConfiguration = ApiTestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerType.LOCAL_FOLDER_LISTENER);
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration jobConfiguration = ApiTestHelper.createJobConfiguration(jobListenerConfiguration);
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
        final Collection<JobConfiguration> result = ResourceToDomainMapper.map(jobConfigurationsToMap);
        this.assertJobConfigurations(result, jobConfigurationsToMap);
    }

    /**
     * Test for issue #23
     */
    @Test
    public void testMapDateComparedToParameterParser() {

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DomainParameterParser.DATE_FORMAT_WITH_TIMESTAMP);
        final Date now = new Date();
        final JobParameter jobParameter = new JobParameter();
        jobParameter.setParameter(simpleDateFormat.format(now));
        jobParameter.setParameterType(ParameterType.DATE);

        final JobParameter jobParameter2 = new JobParameter();
        jobParameter2.setParameter(String.valueOf(now.getTime()));
        jobParameter2.setParameterType(ParameterType.DATE);


        final Map<String, JobParameter> parameterMap = new HashMap<>();
        parameterMap.put("date_parameter", jobParameter);
        parameterMap.put("second_date", jobParameter2);

        final JobParameters jobParameters = new JobParameters();
        jobParameters.setParameters(parameterMap);

        final org.springframework.batch.core.JobParameters result = ResourceToDomainMapper.map(jobParameters);
        assertThat(result).isNotNull();
        assertThat(result.getParameters()).hasSize(2);

        final Date resultDate = result.getDate("date_parameter");
        final Date resultDate2 = result.getDate("second_date");
        final Date parserResult = DomainParameterParser.parseDate(simpleDateFormat.format(now));

        assertThat(resultDate).isEqualTo(parserResult);
        assertThat(resultDate2).isEqualTo(parserResult);
    }

    private void assertJobConfigurations(final Collection<JobConfiguration> jobConfigurationCollection, final JobConfigurations jobConfigurations) {
        for (final JobConfiguration jobConfiguration : jobConfigurationCollection) {
            final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration compareWith = this.getById
                    (jobConfiguration.getJobConfigurationId(), jobConfigurations);
            this.assertJobConfiguration(jobConfiguration, compareWith);
        }
    }

    private void assertJobConfiguration(final JobConfiguration jobConfiguration,
                                        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration compareWith) {
        assertThat(jobConfiguration).isNotNull();
        assertThat(compareWith).isNotNull();
        assertThat(jobConfiguration.getJobName()).isEqualTo(compareWith.getJobName());
        assertThat(jobConfiguration.getJobConfigurationId()).isEqualTo(compareWith.getJobConfigurationId());
        this.assertJobParameters(jobConfiguration.getJobParameters(), compareWith.getJobParameters());
        this.assertJobSchedulerConfiguration(jobConfiguration.getJobSchedulerConfiguration(), compareWith
                .getJobSchedulerConfiguration());
        this.assertJobListenerConfiguration(jobConfiguration.getJobListenerConfiguration(), compareWith.getJobListenerConfiguration());
    }

    private void assertJobSchedulerConfiguration(final JobSchedulerConfiguration jobSchedulerConfiguration,
                                                 final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerConfiguration compareWith) {
        if (jobSchedulerConfiguration != null) {
            assertThat(jobSchedulerConfiguration.getFixedDelay()).isEqualTo(compareWith.getFixedDelay());
            assertThat(jobSchedulerConfiguration.getInitialDelay()).isEqualTo(compareWith.getInitialDelay());
            assertThat(jobSchedulerConfiguration.getCronExpression()).isEqualTo(compareWith.getCronExpression());
            this.assertJobSchedulerType(jobSchedulerConfiguration.getJobSchedulerType(), compareWith.getJobSchedulerType());
            this.assertSchedulerStatus(jobSchedulerConfiguration.getSchedulerStatus(), compareWith.getSchedulerStatus());
            this.assertTaskExecuorType(jobSchedulerConfiguration.getTaskExecutorType(), compareWith.getTaskExecutorType());
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
            assertThat(jobParameterMap.containsKey(entry.getKey())).isTrue();
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
