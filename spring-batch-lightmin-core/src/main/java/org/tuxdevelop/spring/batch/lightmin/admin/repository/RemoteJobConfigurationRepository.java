package org.tuxdevelop.spring.batch.lightmin.admin.repository;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.util.BasicAuthHttpRequestInterceptor;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Marcel Becker
 * @since 0.4
 */
public class RemoteJobConfigurationRepository implements JobConfigurationRepository, InitializingBean {

    private static final String BASE_URI = "/api/repository/jobconfigurations";

    private final String serverUrl;
    private final RestTemplate restTemplate;
    private final String serverBase;

    public RemoteJobConfigurationRepository(final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties) {
        this.serverUrl = springBatchLightminConfigurationProperties.getRemoteRepositoryServerUrl();
        this.restTemplate = getRestTemplate(springBatchLightminConfigurationProperties);
        serverBase = serverUrl + BASE_URI;
    }

    @Override
    public JobConfiguration getJobConfiguration(final Long jobConfigurationId, final String applicationName) throws NoSuchJobConfigurationException {
        final String uri = serverBase + "/{jobconfigurationid}";
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        uriComponentsBuilder.queryParam("applicationname", applicationName);
        final ResponseEntity<JobConfiguration> response = restTemplate.getForEntity(uriComponentsBuilder.toUriString(), JobConfiguration.class, jobConfigurationId);
        evaluateReponse(response, HttpStatus.OK);
        return response.getBody();
    }

    @Override
    public Collection<JobConfiguration> getJobConfigurations(final String jobName, final String applicationName) throws NoSuchJobException, NoSuchJobConfigurationException {
        return null;
    }

    @Override
    public JobConfiguration add(final JobConfiguration jobConfiguration, final String applicationName) {
        return null;
    }

    @Override
    public JobConfiguration update(final JobConfiguration jobConfiguration, final String applicationName) throws NoSuchJobConfigurationException {
        return null;
    }

    @Override
    public void delete(final JobConfiguration jobConfiguration, final String applicationName) throws NoSuchJobConfigurationException {

    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurations(final String applicationName) {
        return null;
    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurationsByJobNames(final Collection<String> jobNames, final String applicationName) {
        return null;
    }

    /*
     * Helpers
     */

    private RestTemplate getRestTemplate(final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties) {
        final String userName = springBatchLightminConfigurationProperties.getRemoteRepositoryUsername();
        final String password = springBatchLightminConfigurationProperties.getRemoteRepositoryPassword();
        final RestTemplate restTemplate = new RestTemplate();
        if (StringUtils.hasText(userName)) {
            restTemplate.setInterceptors(
                    Collections.singletonList(
                            new BasicAuthHttpRequestInterceptor(userName,
                                    password))
            );
        }
        return restTemplate;
    }

    private void evaluateReponse(final ResponseEntity<?> responseEntity, final HttpStatus expectedStatus) {
        if (!expectedStatus.equals(responseEntity.getStatusCode())) {
            throw new SpringBatchLightminApplicationException("Could not execute remote call HttpStatusCode: " + responseEntity.getStatusCode());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        assert restTemplate != null : "RestTemplate must not be null!";
        assert StringUtils.hasText(serverUrl) == Boolean.TRUE : "Remote Repository Server Url must not be null or empty";
    }

}
