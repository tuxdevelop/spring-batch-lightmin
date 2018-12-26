package org.tuxdevelop.spring.batch.lightmin.repository;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.repository.configuration.RemoteJobConfigurationRepositoryConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.util.RequestUtil;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Marcel Becker
 * @since 0.4
 */
public class RemoteJobConfigurationRepository implements JobConfigurationRepository, InitializingBean {

    private static final String BASE_URI = "/api/repository/jobconfigurations";

    private final RestTemplate restTemplate;
    private final RemoteJobConfigurationRepositoryLocator remoteJobConfigurationRepositoryLocator;

    public RemoteJobConfigurationRepository(
            final RemoteJobConfigurationRepositoryConfigurationProperties properties,
            final RestTemplate restTemplate,
            final RemoteJobConfigurationRepositoryLocator remoteJobConfigurationRepositoryLocator) {
        this.restTemplate = restTemplate;
        this.remoteJobConfigurationRepositoryLocator = remoteJobConfigurationRepositoryLocator;
    }

    @Override
    public JobConfiguration getJobConfiguration(final Long jobConfigurationId, final String applicationName) {
        final String uri = this.getServerBase(this.remoteJobConfigurationRepositoryLocator.getRemoteUrl()) + "/{jobconfigurationid}";
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        uriComponentsBuilder.queryParam("applicationname", applicationName);
        final String replacedUri = uriComponentsBuilder.buildAndExpand(jobConfigurationId).toUriString();
        final ResponseEntity<JobConfiguration> response = this.restTemplate.getForEntity(replacedUri, JobConfiguration.class);
        this.evaluateReponse(response, HttpStatus.OK);
        return response.getBody();
    }

    @Override
    public Collection<JobConfiguration> getJobConfigurations(final String jobName, final String applicationName) {
        final UriComponentsBuilder uriComponentsBuilder =
                UriComponentsBuilder
                        .fromUriString(this.getServerBase(this.remoteJobConfigurationRepositoryLocator.getRemoteUrl()));
        uriComponentsBuilder.queryParam("jobname", jobName);
        uriComponentsBuilder.queryParam("applicationname", applicationName);
        final ResponseEntity<JobConfiguration[]> response = this.restTemplate.getForEntity(uriComponentsBuilder.toUriString(), JobConfiguration[].class);
        this.evaluateReponse(response, HttpStatus.OK);
        return Arrays.asList(response.getBody());
    }

    @Override
    public JobConfiguration add(final JobConfiguration jobConfiguration, final String applicationName) {
        final HttpEntity<JobConfiguration> entity = RequestUtil.createApplicationJsonEntity(jobConfiguration);
        final ResponseEntity<JobConfiguration> response =
                this.restTemplate.postForEntity(
                        this.getServerBase(
                                this.remoteJobConfigurationRepositoryLocator.getRemoteUrl()) + "/{applicationname}",
                        entity,
                        JobConfiguration.class,
                        applicationName);
        this.evaluateReponse(response, HttpStatus.OK);
        return response.getBody();
    }

    @Override
    public JobConfiguration update(final JobConfiguration jobConfiguration, final String applicationName) {
        final HttpEntity<JobConfiguration> entity = RequestUtil.createApplicationJsonEntity(jobConfiguration);
        final ResponseEntity<JobConfiguration> response =
                this.restTemplate.exchange(
                        this.getServerBase(this.remoteJobConfigurationRepositoryLocator.getRemoteUrl()) + "/{applicationname}",
                        HttpMethod.PUT,
                        entity,
                        JobConfiguration.class,
                        applicationName);
        this.evaluateReponse(response, HttpStatus.OK);
        return response.getBody();
    }

    @Override
    public void delete(final JobConfiguration jobConfiguration, final String applicationName) {
        final HttpEntity<JobConfiguration> entity = RequestUtil.createApplicationJsonEntity(jobConfiguration);
        final ResponseEntity<Void> response =
                this.restTemplate.postForEntity(
                        this.getServerBase(this.remoteJobConfigurationRepositoryLocator.getRemoteUrl()) + "/delete/{applicationname}",
                        entity,
                        Void.class,
                        applicationName);
        this.evaluateReponse(response, HttpStatus.OK);
    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurations(final String applicationName) {
        final ResponseEntity<JobConfiguration[]> response =
                this.restTemplate.getForEntity(
                        this.getServerBase(this.remoteJobConfigurationRepositoryLocator.getRemoteUrl()) + "/all/{applicationname}",
                        JobConfiguration[].class,
                        applicationName);
        this.evaluateReponse(response, HttpStatus.OK);
        return Arrays.asList(response.getBody());
    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurationsByJobNames(final Collection<String> jobNames, final String applicationName) {
        final UriComponentsBuilder uriComponentsBuilder =
                UriComponentsBuilder
                        .fromUriString(this.getServerBase(this.remoteJobConfigurationRepositoryLocator.getRemoteUrl()) + "/all/{applicationname}/jobs");
        if (jobNames != null) {
            for (final String jobName : jobNames) {
                uriComponentsBuilder.queryParam("jobnames", jobName);
            }
        }
        final String uri = uriComponentsBuilder.buildAndExpand(applicationName).toUriString();
        final ResponseEntity<JobConfiguration[]> response = this.restTemplate.getForEntity(uri, JobConfiguration[].class);
        this.evaluateReponse(response, HttpStatus.OK);
        return Arrays.asList(response.getBody());
    }

    private void evaluateReponse(final ResponseEntity<?> responseEntity, final HttpStatus expectedStatus) {
        if (!expectedStatus.equals(responseEntity.getStatusCode())) {
            throw new SpringBatchLightminApplicationException("Could not execute remote call HttpStatusCode: " + responseEntity.getStatusCode());
        }
    }

    private String getServerBase(final String url) {
        return url + BASE_URI;
    }

    @Override
    public void afterPropertiesSet() {
        assert this.restTemplate != null : "RestTemplate must not be null!";
    }

}
