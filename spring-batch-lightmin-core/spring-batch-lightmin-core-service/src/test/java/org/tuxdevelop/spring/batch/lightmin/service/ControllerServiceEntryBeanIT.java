package org.tuxdevelop.spring.batch.lightmin.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.test.api.ApiTestHelper;
import org.tuxdevelop.test.configuration.ITConfiguration;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ITConfiguration.class)
public class ControllerServiceEntryBeanIT {

    private static final String JOB_NAME = "simpleJob";

    @Autowired
    private ServiceEntry serviceEntry;
    @Autowired
    private AdminService adminService;

    @Test
    public void testSaveJobConfiguration() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = ApiTestHelper.createJobSchedulerConfiguration(null, 10L, 10L,
                JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = ApiTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(JOB_NAME);
        this.serviceEntry.saveJobConfiguration(jobConfiguration);
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add(JOB_NAME);
        final JobConfigurations jobConfigurations = this.serviceEntry.getJobConfigurations(jobNames);
        assertThat(jobConfigurations).isNotNull();
        final Collection<JobConfiguration> fetchedJobConfigurations = jobConfigurations.getJobConfigurations();
        assertThat(fetchedJobConfigurations).isNotEmpty();
    }

    @Test
    public void testUpdateJobConfiguration() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = ApiTestHelper.createJobSchedulerConfiguration(null, 10L, 10L,
                JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = ApiTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(JOB_NAME);
        this.serviceEntry.saveJobConfiguration(jobConfiguration);
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add(JOB_NAME);
        final JobConfigurations jobConfigurations = this.serviceEntry.getJobConfigurations(jobNames);
        assertThat(jobConfigurations).isNotNull();
        final Collection<JobConfiguration> fetchedJobConfigurations = jobConfigurations.getJobConfigurations();
        assertThat(fetchedJobConfigurations).isNotEmpty();
        Long jobConfigurationId = null;
        for (final JobConfiguration fetchedJobConfiguration : fetchedJobConfigurations) {
            jobConfigurationId = fetchedJobConfiguration.getJobConfigurationId();
        }
        assertThat(jobConfigurationId).isNotNull();
        final JobConfiguration fetchedJobConfiguration = this.serviceEntry.getJobConfigurationById(jobConfigurationId);
        assertThat(fetchedJobConfiguration).isNotNull();
        final JobParameters jobParameters = new JobParameters();
        final Map<String, JobParameter> jobParametersMap = new HashMap<>();
        final JobParameter jobParameter = new JobParameter();
        jobParameter.setParameter(20.2);
        jobParameter.setParameterType(ParameterType.DOUBLE);
        jobParametersMap.put("Double", jobParameter);
        jobParameters.setParameters(jobParametersMap);
        fetchedJobConfiguration.setJobParameters(jobParameters);
        this.serviceEntry.updateJobConfiguration(fetchedJobConfiguration);
        final JobConfiguration updatedJobConfiguration = this.serviceEntry.getJobConfigurationById(jobConfigurationId);
        assertThat(updatedJobConfiguration).isEqualTo(fetchedJobConfiguration);
    }

    @Test
    public void testDeleteJobConfiguration() {
        final String jobName = "simpleJob";
        final JobSchedulerConfiguration jobSchedulerConfiguration = ApiTestHelper.createJobSchedulerConfiguration(null, 10L, 10L,
                JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = ApiTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(jobName);
        this.serviceEntry.saveJobConfiguration(jobConfiguration);
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add(jobName);
        final JobConfigurations jobConfigurations = this.serviceEntry.getJobConfigurations(jobNames);
        assertThat(jobConfigurations).isNotNull();
        final Collection<JobConfiguration> fetchedJobConfigurations = jobConfigurations.getJobConfigurations();
        assertThat(fetchedJobConfigurations).hasSize(1);
        final Long jobConfigurationId = fetchedJobConfigurations.iterator().next().getJobConfigurationId();
        this.serviceEntry.deleteJobConfiguration(jobConfigurationId);
        final JobConfigurations jobConfigurationsAfterDelete = this.serviceEntry.getJobConfigurations(jobNames);
        assertThat(jobConfigurationsAfterDelete.getJobConfigurations()).isEmpty();
    }

    @Test
    public void testGetJobConfigurationsByJobName() {
        final String jobName = "simpleJob";
        final JobSchedulerConfiguration jobSchedulerConfiguration = ApiTestHelper.createJobSchedulerConfiguration(null, 10L, 10L,
                JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = ApiTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(jobName);
        this.serviceEntry.saveJobConfiguration(jobConfiguration);
        final JobConfigurations fetchedJobConfigurations = this.serviceEntry.getJobConfigurationsByJobName(jobName);
        final Collection<JobConfiguration> fetchedJobConfigurationsCollection = fetchedJobConfigurations
                .getJobConfigurations();
        assertThat(fetchedJobConfigurationsCollection).isNotEmpty();
        final JobConfiguration jobConfigurationResult = fetchedJobConfigurationsCollection.iterator().next();
        assertThat(jobConfigurationResult.getJobName()).isEqualTo(jobName);
    }

    @Test
    public void testGetJobConfigurationMap() {
        final String jobName = "simpleJob";
        final JobSchedulerConfiguration jobSchedulerConfiguration = ApiTestHelper.createJobSchedulerConfiguration(null, 10L, 10L,
                JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = ApiTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(jobName);
        this.serviceEntry.saveJobConfiguration(jobConfiguration);
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add(jobName);
        final Map<String, JobConfigurations> result = this.serviceEntry.getJobConfigurationMap(jobNames);
        assertThat(result.containsKey(jobName)).isTrue();
        final JobConfigurations jobConfigurations = result.get(jobName);
        final Collection<JobConfiguration> fetchedJobConfigurations = jobConfigurations.getJobConfigurations();
        assertThat(fetchedJobConfigurations).isNotEmpty();
        final JobConfiguration jobConfigurationResult = fetchedJobConfigurations.iterator().next();
        assertThat(jobConfigurationResult.getJobName()).isEqualTo(jobName);
    }

    @Before
    public void init() {
        try {
            final Collection<org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration> allJC = this.adminService.getJobConfigurationsByJobName(JOB_NAME);
            for (final org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration jobConfiguration : allJC) {
                this.adminService.deleteJobConfiguration(jobConfiguration.getJobConfigurationId());
            }
        } catch (final SpringBatchLightminApplicationException e) {
            log.warn("Repository clean up failed");
        }
    }
}
