package org.tuxdevelop.spring.batch.lightmin.server.admin;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public class RemoteAdminServerServiceBean implements AdminServerService {

    private final RestTemplate restTemplate;

    public RemoteAdminServerServiceBean(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void saveJobConfiguration(final JobConfiguration jobConfiguration, final LightminClientApplication lightminClientApplication) {
        final ResponseEntity<Void> response = restTemplate.postForEntity(getClientUri(lightminClientApplication), jobConfiguration, Void.class);
        if (!HttpStatus.CREATED.equals(response.getStatusCode())) {
            final String errorMessage = "ERROR - HTTP STATUS: " + response.getStatusCode();
            throw new SpringBatchLightminApplicationException(errorMessage);
        }
    }

    @Override
    public void updateJobConfiguration(final JobConfiguration jobConfiguration, final LightminClientApplication lightminClientApplication) {
        restTemplate.put(getClientUri(lightminClientApplication), jobConfiguration);
    }

    @Override
    public JobConfigurations getJobConfigurations(final LightminClientApplication lightminClientApplication) {
        final ResponseEntity<JobConfigurations> response = restTemplate.getForEntity(getClientUri(lightminClientApplication), JobConfigurations.class);
        checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public Map<String, JobConfigurations> getJobConfigurationsMap(final LightminClientApplication
                                                                          lightminClientApplication) {
        final JobConfigurations jobConfigurations = getJobConfigurations(lightminClientApplication);
        final Map<String, JobConfigurations> jobConfigurationMap = new HashMap<>();
        for (final JobConfiguration jobConfiguration : jobConfigurations.getJobConfigurations()) {
            if (!jobConfigurationMap.containsKey(jobConfiguration.getJobName())) {
                jobConfigurationMap.put(jobConfiguration.getJobName(), new JobConfigurations());
            }
            jobConfigurationMap.get(jobConfiguration.getJobName()).getJobConfigurations().add(jobConfiguration);
        }
        return jobConfigurationMap;
    }

    @Override
    public void deleteJobConfiguration(final Long jobConfigurationId, final LightminClientApplication lightminClientApplication) {
        final String uri = getClientUri(lightminClientApplication);
        restTemplate.delete(uri + "/" + jobConfigurationId);
    }

    @Override
    public JobConfiguration getJobConfiguration(final Long jobConfigurationId, final LightminClientApplication lightminClientApplication) {
        final String uri = getClientUri(lightminClientApplication);
        final ResponseEntity<JobConfiguration> response = restTemplate.getForEntity(uri + "/jobconfiguration/" + jobConfigurationId, JobConfiguration.class);
        checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public void startJobConfigurationScheduler(final Long jobConfigurationId, final LightminClientApplication lightminClientApplication) {
        final String uri = getClientUri(lightminClientApplication);
        final ResponseEntity<Void> response = restTemplate.getForEntity(uri + "/" + jobConfigurationId + "/start", Void.class);
        checkHttpOk(response);
    }

    @Override
    public void stopJobConfigurationScheduler(final Long jobConfigurationId, final LightminClientApplication lightminClientApplication) {
        final String uri = getClientUri(lightminClientApplication);
        final ResponseEntity<Void> response = restTemplate.getForEntity(uri + "/" + jobConfigurationId + "/stop", Void.class);
        checkHttpOk(response);
    }

    private String getClientUri(final LightminClientApplication lightminClientApplication) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(lightminClientApplication.getServiceUrl());
        stringBuilder.append("/api/jobconfigurations");
        return stringBuilder.toString();
    }

    private void checkHttpOk(final ResponseEntity<?> responseEntity) {
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            final String errorMessage = "ERROR - HTTP STATUS: " + responseEntity.getStatusCode();
            throw new SpringBatchLightminApplicationException(errorMessage);
        }
    }

}
