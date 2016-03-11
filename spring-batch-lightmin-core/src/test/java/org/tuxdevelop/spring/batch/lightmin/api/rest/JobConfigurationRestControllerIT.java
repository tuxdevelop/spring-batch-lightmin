package org.tuxdevelop.spring.batch.lightmin.api.rest;


import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.response.JobConfigurations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class JobConfigurationRestControllerIT extends CommonControllerIT {

    @Test
    public void getJobConfigurationsIT() {
        final String uri = LOCALHOST + ":" + getServerPort() + AbstractRestController.JobConfigurationRestControllerAPI
                .JOB_CONFIGURATIONS;
        final ResponseEntity<JobConfigurations> result = restTemplate.getForEntity(uri, JobConfigurations.class);
        assertThat(result).isNotNull();
        final JobConfigurations jobConfigurations = result.getBody();
        final Collection<JobConfiguration> jobConfigurationsCollection = jobConfigurations.getJobConfigurations();
        assertThat(jobConfigurationsCollection).hasSize(1);
    }

    @Test
    public void getJobConfigurationsByJobNameIT() {
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
    public void getJobConfigurationByIdIT() {
        final String uri = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID;
        final ResponseEntity<JobConfiguration> result = restTemplate.getForEntity(uri, JobConfiguration.class, addedJobConfigurationId);
        assertThat(result).isNotNull();
        final JobConfiguration jobConfiguration = result.getBody();
        assertThat(jobConfiguration).isNotNull();
        assertThat(jobConfiguration.getJobConfigurationId()).isEqualTo(addedJobConfigurationId);
    }

    @Test
    public void addJobConfigurationIT() {
        final String uri = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS;
        final JobConfiguration jobConfiguration = createJobConfiguration();
        jobConfiguration.getJobSchedulerConfiguration().setBeanName("anOtherBeanName");
        final ResponseEntity<Void> responseEntity = restTemplate.postForEntity(uri, jobConfiguration, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
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
    public void updateJobConfigurationIT() {
        final String uriGet = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID;
        final ResponseEntity<JobConfiguration> result = restTemplate.getForEntity(uriGet, JobConfiguration.class, addedJobConfigurationId);
        assertThat(result).isNotNull();
        final JobConfiguration jobConfiguration = result.getBody();
        assertThat(jobConfiguration).isNotNull();
        assertThat(jobConfiguration.getJobConfigurationId()).isEqualTo(addedJobConfigurationId);
        final Map<String, Object> jobParameters = new HashMap<String, Object>();
        jobParameters.put("LONG", 20.2);
        jobConfiguration.setJobParameters(jobParameters);

        final String uriPUT = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS;
        restTemplate.put(uriPUT, jobConfiguration);
        final ResponseEntity<JobConfiguration> resultUpdate = restTemplate.getForEntity(uriGet, JobConfiguration.class,
                addedJobConfigurationId);
        assertThat(result).isNotNull();
        final JobConfiguration jobConfigurationUpate = resultUpdate.getBody();
        assertThat(jobConfigurationUpate).isNotNull();
        assertThat(jobConfigurationUpate.getJobConfigurationId()).isEqualTo(addedJobConfigurationId);
        assertThat(jobConfigurationUpate.getJobParameters()).isEqualTo(jobParameters);
    }

    @Test
    public void deleteJobConfigurationByIdIT() {
        final String uriAdd = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS;
        final JobConfiguration jobConfiguration = createJobConfiguration();
        jobConfiguration.getJobSchedulerConfiguration().setBeanName("anOtherBeanName");
        final ResponseEntity<Void> responseEntity = restTemplate.postForEntity(uriAdd, jobConfiguration, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
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
        for (JobConfiguration jobConfigurationIter : jobConfigurationsAfterDeleteCollection) {
            id = jobConfigurationIter.getJobConfigurationId();
        }
        assertThat(id).isNotEqualTo(addedJobConfigurationId);
    }

    @Before
    public void init() {
        this.addJobConfigurations();
    }

}
