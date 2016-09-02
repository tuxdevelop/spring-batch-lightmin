package org.tuxdevelop.spring.batch.lightmin.api.controller;


import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.tuxdevelop.spring.batch.lightmin.api.resource.AdminToResourceMapper;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.SchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class JobConfigurationRestControllerIT extends CommonControllerIT {

    @Test
    public void testGetJobConfigurations() {
        final String uri = LOCALHOST + ":" + getServerPort() + AbstractRestController.JobConfigurationRestControllerAPI
                .JOB_CONFIGURATIONS;
        final ResponseEntity<JobConfigurations> result = restTemplate.getForEntity(uri, JobConfigurations.class);
        assertThat(result).isNotNull();
        final JobConfigurations jobConfigurations = result.getBody();
        final Collection<JobConfiguration> jobConfigurationsCollection = jobConfigurations.getJobConfigurations();
        assertThat(jobConfigurationsCollection).hasSize(1);
    }

    @Test
    public void testGetJobConfigurationsByJobName() {
        final String uri = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS_JOB_NAME;
        final ResponseEntity<JobConfigurations> result = restTemplate.getForEntity(uri, JobConfigurations.class,
                "simpleJob");
        assertThat(result).isNotNull();
        final JobConfigurations jobConfigurations = result.getBody();
        final Collection<JobConfiguration> jobConfigurationsCollection = jobConfigurations.getJobConfigurations();
        assertThat(jobConfigurationsCollection).hasSize(1);
    }

    @Test
    public void testGetJobConfigurationById() {
        final String uri = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID;
        final ResponseEntity<JobConfiguration> result = restTemplate.getForEntity(uri, JobConfiguration.class, addedJobConfigurationId);
        assertThat(result).isNotNull();
        final JobConfiguration jobConfiguration = result.getBody();
        assertThat(jobConfiguration).isNotNull();
        assertThat(jobConfiguration.getJobConfigurationId()).isEqualTo(addedJobConfigurationId);
    }

    @Test
    public void testAddJobConfiguration() {
        final String uri = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS;
        final JobConfiguration jobConfiguration = AdminToResourceMapper.map(createJobConfiguration());
        final ResponseEntity<Void> responseEntity = restTemplate.postForEntity(uri, jobConfiguration, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        final String uriGet = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS_JOB_NAME;
        final ResponseEntity<JobConfigurations> result = restTemplate.getForEntity(uriGet, JobConfigurations.class,
                "simpleJob");
        assertThat(result).isNotNull();
        final JobConfigurations jobConfigurations = result.getBody();
        final Collection<JobConfiguration> jobConfigurationsCollection = jobConfigurations.getJobConfigurations();
        assertThat(jobConfigurationsCollection).hasSize(2);
    }

    @Test
    public void testUpdateJobConfiguration() {
        final String uriGet = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID;
        final ResponseEntity<JobConfiguration> result = restTemplate.getForEntity(uriGet, JobConfiguration.class, addedJobConfigurationId);
        assertThat(result).isNotNull();
        final JobConfiguration jobConfiguration = result.getBody();
        assertThat(jobConfiguration).isNotNull();
        assertThat(jobConfiguration.getJobConfigurationId()).isEqualTo(addedJobConfigurationId);
        final Map<String, Object> jobParameterMap = new HashMap<>();
        jobParameterMap.put("LONG", 20.2);
        final JobParameters jobParameters = AdminToResourceMapper.map(jobParameterMap);
        jobConfiguration.setJobParameters(jobParameters);

        final String uriPUT = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS;
        restTemplate.put(uriPUT, jobConfiguration);
        final ResponseEntity<JobConfiguration> resultUpdate = restTemplate.getForEntity(uriGet, JobConfiguration.class,
                addedJobConfigurationId);
        assertThat(result).isNotNull();
        final JobConfiguration jobConfigurationUpdate = resultUpdate.getBody();
        assertThat(jobConfigurationUpdate).isNotNull();
        assertThat(jobConfigurationUpdate.getJobConfigurationId()).isEqualTo(addedJobConfigurationId);
        assertThat(jobConfigurationUpdate.getJobParameters()).isEqualTo(jobParameters);
    }

    @Test
    public void testDeleteJobConfigurationById() {
        final String uriAdd = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS;
        final JobConfiguration jobConfiguration = AdminToResourceMapper.map(createJobConfiguration());
        final ResponseEntity<Void> responseEntity = restTemplate.postForEntity(uriAdd, jobConfiguration, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        final String uriGet = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS_JOB_NAME;
        final ResponseEntity<JobConfigurations> result = restTemplate.getForEntity(uriGet, JobConfigurations.class,
                "simpleJob");
        assertThat(result).isNotNull();
        final JobConfigurations jobConfigurations = result.getBody();
        final Collection<JobConfiguration> jobConfigurationsCollection = jobConfigurations.getJobConfigurations();
        assertThat(jobConfigurationsCollection).hasSize(2);

        final String uri = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID;

        restTemplate.delete(uri, addedJobConfigurationId);

        final ResponseEntity<JobConfigurations> resultAfterDelete = restTemplate.getForEntity(uriGet, JobConfigurations.class, "simpleJob");
        assertThat(result).isNotNull();
        final JobConfigurations jobConfigurationsAfterDelete = resultAfterDelete.getBody();
        final Collection<JobConfiguration> jobConfigurationsAfterDeleteCollection = jobConfigurationsAfterDelete.getJobConfigurations();
        assertThat(jobConfigurationsAfterDeleteCollection).hasSize(1);
        Long id = null;
        for (final JobConfiguration jobConfigurationIter : jobConfigurationsAfterDeleteCollection) {
            id = jobConfigurationIter.getJobConfigurationId();
        }
        assertThat(id).isNotEqualTo(addedJobConfigurationId);
    }

    @Test
    public void testStartJobConfigurationScheduler() {
        final ResponseEntity<Void> response = restTemplate.getForEntity(
                LOCALHOST + ":" + getServerPort() + AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_SCHEDULER_START, Void.class, addedJobConfigurationId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final ResponseEntity<JobConfiguration> responseEntity = restTemplate.getForEntity
                (LOCALHOST + ":" + getServerPort() + AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_JOB_CONFIGURATION_ID, JobConfiguration.class, addedJobConfigurationId);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        final JobConfiguration jobConfiguration = responseEntity.getBody();
        assertThat(jobConfiguration.getJobSchedulerConfiguration().getSchedulerStatus()).isEqualTo(SchedulerStatus
                .RUNNING);
    }

    @Test
    public void testStopJobConfigurationScheduler() {
        final ResponseEntity<Void> response = restTemplate.getForEntity(
                LOCALHOST + ":" + getServerPort() + AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_SCHEDULER_START, Void.class, addedJobConfigurationId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final ResponseEntity<JobConfiguration> responseEntity = restTemplate.getForEntity
                (LOCALHOST + ":" + getServerPort() + AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_JOB_CONFIGURATION_ID, JobConfiguration.class, addedJobConfigurationId);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        final JobConfiguration jobConfiguration = responseEntity.getBody();
        assertThat(jobConfiguration.getJobSchedulerConfiguration().getSchedulerStatus()).isEqualTo(SchedulerStatus
                .RUNNING);
        final ResponseEntity<Void> responseStop = restTemplate.getForEntity(
                LOCALHOST + ":" + getServerPort() + AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_SCHEDULER_STOP, Void.class, addedJobConfigurationId);
        assertThat(responseStop.getStatusCode()).isEqualTo(HttpStatus.OK);
        final ResponseEntity<JobConfiguration> responseEntityStopped = restTemplate.getForEntity
                (LOCALHOST + ":" + getServerPort() + AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_JOB_CONFIGURATION_ID, JobConfiguration.class, addedJobConfigurationId);
        final JobConfiguration jobConfigurationStopped = responseEntityStopped.getBody();
        assertThat(jobConfigurationStopped.getJobSchedulerConfiguration().getSchedulerStatus()).isEqualTo(SchedulerStatus
                .STOPPED);
    }

    @Before
    public void init() {
        this.addJobConfigurations();
    }

}
