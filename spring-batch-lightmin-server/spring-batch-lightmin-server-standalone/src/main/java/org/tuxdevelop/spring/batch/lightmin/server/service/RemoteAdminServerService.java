package org.tuxdevelop.spring.batch.lightmin.server.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.util.RequestUtil;
import org.tuxdevelop.spring.batch.lightmin.util.ResponseUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Remote implementation of the {@link AdminServerService}
 *
 * @author Marcel Becker
 * @since 0.3
 */
@Slf4j
public class RemoteAdminServerService implements AdminServerService {

    private final RestTemplate restTemplate;

    public RemoteAdminServerService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void saveJobConfiguration(final JobConfiguration jobConfiguration,
                                     final LightminClientApplication lightminClientApplication) {
        final HttpEntity<JobConfiguration> entity = RequestUtil.createApplicationJsonEntity(jobConfiguration);
        final ResponseEntity<Void> response =
                this.restTemplate.postForEntity(this.getClientUri(lightminClientApplication), entity, Void.class);
        if (!HttpStatus.CREATED.equals(response.getStatusCode())) {
            final String errorMessage = "ERROR - HTTP STATUS: " + response.getStatusCode();
            throw new SpringBatchLightminApplicationException(errorMessage);
        }
    }

    @Override
    public void updateJobConfiguration(final JobConfiguration jobConfiguration,
                                       final LightminClientApplication lightminClientApplication) {
        final HttpEntity<JobConfiguration> entity = RequestUtil.createApplicationJsonEntity(jobConfiguration);
        this.restTemplate.put(this.getClientUri(lightminClientApplication), entity);
    }

    @Override
    public JobConfigurations getJobConfigurations(final LightminClientApplication lightminClientApplication) {
        final ResponseEntity<JobConfigurations> response =
                this.restTemplate.getForEntity(this.getClientUri(lightminClientApplication), JobConfigurations.class);
        ResponseUtil.checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public Map<String, JobConfigurations> getJobConfigurationsMap(final LightminClientApplication
                                                                          lightminClientApplication) {
        final JobConfigurations jobConfigurations = this.getJobConfigurations(lightminClientApplication);
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
    public void deleteJobConfiguration(final Long jobConfigurationId,
                                       final LightminClientApplication lightminClientApplication) {
        final String uri = this.getClientUri(lightminClientApplication) + "/jobconfiguration/{jobconfigurationid}";
        this.restTemplate.delete(uri, jobConfigurationId);
    }

    @Override
    public JobConfiguration getJobConfiguration(final Long jobConfigurationId,
                                                final LightminClientApplication lightminClientApplication) {
        final String uri = this.getClientUri(lightminClientApplication);
        final ResponseEntity<JobConfiguration> response =
                this.restTemplate.getForEntity(
                        uri + "/jobconfiguration/" + jobConfigurationId,
                        JobConfiguration.class);
        ResponseUtil.checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public void startJobConfigurationScheduler(final Long jobConfigurationId,
                                               final LightminClientApplication lightminClientApplication) {
        final String uri = this.getClientUri(lightminClientApplication);
        final ResponseEntity<Void> response =
                this.restTemplate.getForEntity(uri + "/" + jobConfigurationId + "/start", Void.class);
        ResponseUtil.checkHttpOk(response);
    }

    @Override
    public void stopJobConfigurationScheduler(final Long jobConfigurationId,
                                              final LightminClientApplication lightminClientApplication) {
        final String uri = this.getClientUri(lightminClientApplication);
        final ResponseEntity<Void> response =
                this.restTemplate.getForEntity(uri + "/" + jobConfigurationId + "/stop", Void.class);
        ResponseUtil.checkHttpOk(response);
    }

    private String getClientUri(final LightminClientApplication lightminClientApplication) {
        return lightminClientApplication.getServiceUrl() + "/api/jobconfigurations";
    }


}
