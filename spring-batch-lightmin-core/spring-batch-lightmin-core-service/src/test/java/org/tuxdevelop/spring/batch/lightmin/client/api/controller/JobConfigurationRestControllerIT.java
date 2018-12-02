package org.tuxdevelop.spring.batch.lightmin.client.api.controller;


import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.SchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.client.api.DomainToResourceMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class JobConfigurationRestControllerIT extends CommonControllerIT {

    @Test
    public void testGetJobConfigurations() {
        final String uri = LOCALHOST + ":" + this.getServerPort() + AbstractRestController.JobConfigurationRestControllerAPI
                .JOB_CONFIGURATIONS;
        final ResponseEntity<JobConfigurations> result = this.restTemplate.getForEntity(uri, JobConfigurations.class);
        assertThat(result).isNotNull();
        final JobConfigurations jobConfigurations = result.getBody();
        final Collection<JobConfiguration> jobConfigurationsCollection = jobConfigurations.getJobConfigurations();
        assertThat(jobConfigurationsCollection).hasSize(1);
    }

    @Test
    public void testGetJobConfigurationsByJobName() {
        final String uri = LOCALHOST + ":" + this.getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS_JOB_NAME;
        final ResponseEntity<JobConfigurations> result = this.restTemplate.getForEntity(uri, JobConfigurations.class,
                "simpleJob");
        assertThat(result).isNotNull();
        final JobConfigurations jobConfigurations = result.getBody();
        final Collection<JobConfiguration> jobConfigurationsCollection = jobConfigurations.getJobConfigurations();
        assertThat(jobConfigurationsCollection).hasSize(1);
    }

    @Test
    public void testGetJobConfigurationById() {
        final String uri = LOCALHOST + ":" + this.getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID;
        final ResponseEntity<JobConfiguration> result = this.restTemplate.getForEntity(uri, JobConfiguration.class, this.addedJobConfigurationId);
        assertThat(result).isNotNull();
        final JobConfiguration jobConfiguration = result.getBody();
        assertThat(jobConfiguration).isNotNull();
        assertThat(jobConfiguration.getJobConfigurationId()).isEqualTo(this.addedJobConfigurationId);
    }

    @Test
    public void testAddJobConfiguration() {
        final String uri = LOCALHOST + ":" + this.getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS;
        final JobConfiguration jobConfiguration = DomainToResourceMapper.map(this.createJobConfiguration());
        final ResponseEntity<Void> responseEntity = this.restTemplate.postForEntity(uri, jobConfiguration, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        final String uriGet = LOCALHOST + ":" + this.getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS_JOB_NAME;
        final ResponseEntity<JobConfigurations> result = this.restTemplate.getForEntity(uriGet, JobConfigurations.class,
                "simpleJob");
        assertThat(result).isNotNull();
        final JobConfigurations jobConfigurations = result.getBody();
        final Collection<JobConfiguration> jobConfigurationsCollection = jobConfigurations.getJobConfigurations();
        assertThat(jobConfigurationsCollection).hasSize(2);
    }

    @Test
    public void testUpdateJobConfiguration() {
        final String uriGet = LOCALHOST + ":" + this.getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID;
        final ResponseEntity<JobConfiguration> result = this.restTemplate.getForEntity(uriGet, JobConfiguration.class, this.addedJobConfigurationId);
        assertThat(result).isNotNull();
        final JobConfiguration jobConfiguration = result.getBody();
        assertThat(jobConfiguration).isNotNull();
        assertThat(jobConfiguration.getJobConfigurationId()).isEqualTo(this.addedJobConfigurationId);
        final Map<String, Object> jobParameterMap = new HashMap<>();
        jobParameterMap.put("LONG", 20.2);
        final JobParameters jobParameters = DomainToResourceMapper.map(jobParameterMap);
        jobConfiguration.setJobParameters(jobParameters);

        final String uriPUT = LOCALHOST + ":" + this.getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS;
        this.restTemplate.put(uriPUT, jobConfiguration);
        final ResponseEntity<JobConfiguration> resultUpdate = this.restTemplate.getForEntity(uriGet, JobConfiguration.class,
                this.addedJobConfigurationId);
        assertThat(result).isNotNull();
        final JobConfiguration jobConfigurationUpdate = resultUpdate.getBody();
        assertThat(jobConfigurationUpdate).isNotNull();
        assertThat(jobConfigurationUpdate.getJobConfigurationId()).isEqualTo(this.addedJobConfigurationId);
        assertThat(jobConfigurationUpdate.getJobParameters()).isEqualTo(jobParameters);
    }

    @Test
    public void testDeleteJobConfigurationById() {
        final String uriAdd = LOCALHOST + ":" + this.getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS;
        final JobConfiguration jobConfiguration = DomainToResourceMapper.map(this.createJobConfiguration());
        final ResponseEntity<Void> responseEntity = this.restTemplate.postForEntity(uriAdd, jobConfiguration, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        final String uriGet = LOCALHOST + ":" + this.getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS_JOB_NAME;
        final ResponseEntity<JobConfigurations> result = this.restTemplate.getForEntity(uriGet, JobConfigurations.class,
                "simpleJob");
        assertThat(result).isNotNull();
        final JobConfigurations jobConfigurations = result.getBody();
        final Collection<JobConfiguration> jobConfigurationsCollection = jobConfigurations.getJobConfigurations();
        assertThat(jobConfigurationsCollection).hasSize(2);

        final String uri = LOCALHOST + ":" + this.getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID;

        this.restTemplate.delete(uri, this.addedJobConfigurationId);

        final ResponseEntity<JobConfigurations> resultAfterDelete = this.restTemplate.getForEntity(uriGet, JobConfigurations.class, "simpleJob");
        assertThat(result).isNotNull();
        final JobConfigurations jobConfigurationsAfterDelete = resultAfterDelete.getBody();
        final Collection<JobConfiguration> jobConfigurationsAfterDeleteCollection = jobConfigurationsAfterDelete.getJobConfigurations();
        assertThat(jobConfigurationsAfterDeleteCollection).hasSize(1);
        Long id = null;
        for (final JobConfiguration jobConfigurationIter : jobConfigurationsAfterDeleteCollection) {
            id = jobConfigurationIter.getJobConfigurationId();
        }
        assertThat(id).isNotEqualTo(this.addedJobConfigurationId);
    }

    @Test
    public void testStartJobConfigurationScheduler() {
        final ResponseEntity<Void> response = this.restTemplate.getForEntity(
                LOCALHOST + ":" + this.getServerPort() + AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_START, Void.class, this.addedJobConfigurationId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final ResponseEntity<JobConfiguration> responseEntity = this.restTemplate.getForEntity
                (LOCALHOST + ":" + this.getServerPort() + AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_JOB_CONFIGURATION_ID, JobConfiguration.class, this.addedJobConfigurationId);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        final JobConfiguration jobConfiguration = responseEntity.getBody();
        assertThat(jobConfiguration.getJobSchedulerConfiguration().getSchedulerStatus()).isEqualTo(SchedulerStatus
                .RUNNING);
    }

    @Test
    public void testStopJobConfigurationScheduler() {
        final ResponseEntity<Void> response = this.restTemplate.getForEntity(
                LOCALHOST + ":" + this.getServerPort() + AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_START, Void.class, this.addedJobConfigurationId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final ResponseEntity<JobConfiguration> responseEntity = this.restTemplate.getForEntity
                (LOCALHOST + ":" + this.getServerPort() + AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_JOB_CONFIGURATION_ID, JobConfiguration.class, this.addedJobConfigurationId);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        final JobConfiguration jobConfiguration = responseEntity.getBody();
        assertThat(jobConfiguration.getJobSchedulerConfiguration().getSchedulerStatus()).isEqualTo(SchedulerStatus
                .RUNNING);
        final ResponseEntity<Void> responseStop = this.restTemplate.getForEntity(
                LOCALHOST + ":" + this.getServerPort() + AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_STOP, Void.class, this.addedJobConfigurationId);
        assertThat(responseStop.getStatusCode()).isEqualTo(HttpStatus.OK);
        final ResponseEntity<JobConfiguration> responseEntityStopped = this.restTemplate.getForEntity
                (LOCALHOST + ":" + this.getServerPort() + AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_JOB_CONFIGURATION_ID, JobConfiguration.class, this.addedJobConfigurationId);
        final JobConfiguration jobConfigurationStopped = responseEntityStopped.getBody();
        assertThat(jobConfigurationStopped.getJobSchedulerConfiguration().getSchedulerStatus()).isEqualTo(SchedulerStatus
                .STOPPED);
    }

    @Before
    public void init() {
        this.cleanUp();
        this.addJobConfigurations();
    }

}
