package org.tuxdevelop.spring.batch.lightmin.client.classic.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.classic.configuration.LightminClientClassicConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.service.LightminServerLocatorService;
import org.tuxdevelop.spring.batch.lightmin.util.RequestUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Slf4j
public class LightminClientRegistratorService {

    private static final String SLASH = "/";

    private final AtomicReference<String> registeredId = new AtomicReference<>();

    private final LightminClientProperties lightminClientProperties;
    private final LightminClientClassicConfigurationProperties lightminClientClassicConfigurationProperties;
    private final RestTemplate restTemplate;
    private final JobRegistry jobRegistry;
    private final LightminServerLocatorService lightminServerLocatorService;

    public LightminClientRegistratorService(
            final LightminClientProperties lightminClientProperties,
            final LightminClientClassicConfigurationProperties lightminClientClassicConfigurationProperties,
            final RestTemplate restTemplate,
            final JobRegistry jobRegistry,
            final LightminServerLocatorService lightminServerLocatorService) {
        this.lightminClientProperties = lightminClientProperties;
        this.lightminClientClassicConfigurationProperties = lightminClientClassicConfigurationProperties;
        this.restTemplate = restTemplate;
        this.jobRegistry = jobRegistry;
        this.lightminServerLocatorService = lightminServerLocatorService;
    }


    public Boolean register() {
        Boolean isRegistrationSuccessful = Boolean.FALSE;

        final LightminClientApplication lightminClientApplication =
                LightminClientApplication
                        .createApplication(
                                new LinkedList<>(this.jobRegistry.getJobNames()),
                                this.lightminClientProperties);

        final HttpEntity<LightminClientApplication> entity =
                RequestUtil.createApplicationJsonEntity(lightminClientApplication);

        final List<String> serverUrls = this.lightminServerLocatorService.getRemoteUrls();
        for (final String lightminUrl : serverUrls) {
            try {
                final String applicationPath =
                        this.getLightminServerApplicationPath(
                                lightminUrl,
                                this.lightminClientClassicConfigurationProperties.getServer().getApiApplicationsPath());
                final String lightminAppplicationsUrl = lightminUrl + applicationPath;
                final ResponseEntity<LightminClientApplication> response = this.restTemplate.postForEntity(
                        lightminAppplicationsUrl,
                        entity,
                        LightminClientApplication.class);
                if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                    if (this.registeredId.compareAndSet(null, response.getBody().getId())) {
                        log.info("Application registered itself as {}", response.getBody());
                    } else {
                        log.debug("Application refreshed itself as {}", response.getBody());
                    }
                    isRegistrationSuccessful = Boolean.TRUE;
                    if (this.lightminClientClassicConfigurationProperties.isRegisterOnce()) {
                        break;
                    }
                } else {
                    log.warn("Application failed to registered itself as {}. Response: {}", lightminClientApplication,
                            response.toString());
                }
            } catch (final Exception ex) {
                log.warn("Failed to register application as {} at spring-boot-lightminClientServerProperties ({}): {}",
                        lightminClientApplication,
                        this.lightminClientClassicConfigurationProperties.getServer().getLightminUrl(), ex.getMessage());
            }
        }
        return isRegistrationSuccessful;
    }

    @EventListener(ContextClosedEvent.class)
    public void deregister(final ContextClosedEvent event) {
        log.debug("Retrievied ContextClosedEvent for dereigistration: {}", event);
        if (this.lightminClientClassicConfigurationProperties.isAutoDeregistration()) {
            final String id = this.registeredId.get();
            if (id != null) {
                final List<String> serverUrls = this.lightminServerLocatorService.getRemoteUrls();
                for (final String lightminUrl : serverUrls) {
                    try {
                        final String applicationPath =
                                this.getLightminServerApplicationPath(
                                        lightminUrl,
                                        this.lightminClientClassicConfigurationProperties.getServer().getApiApplicationsPath());
                        this.restTemplate.delete(lightminUrl + applicationPath + "/" + id);
                        this.registeredId.compareAndSet(id, null);
                        if (this.lightminClientClassicConfigurationProperties.isRegisterOnce()) {
                            break;
                        }
                    } catch (final Exception ex) {
                        log.warn(
                                "Failed to deregister application (id={}) at spring-batch-lightmin ({}): {}", id, lightminUrl, ex.getMessage());
                    }
                }
            } else {
                log.debug("Application id is null, deregistration deactivated");
            }
        } else {
            log.debug("No auto deregistration active, nothinhg to do");
        }
    }

    private String getLightminServerApplicationPath(final String lightminUrl, final String applicationApiPath) {

        final String path;

        if (StringUtils.hasText(applicationApiPath)) {

            if (lightminUrl.endsWith(SLASH)) {
                if (applicationApiPath.startsWith(SLASH)) {
                    path = applicationApiPath.replaceFirst(SLASH, "");
                } else {
                    path = applicationApiPath;
                }
            } else {
                if (applicationApiPath.startsWith(SLASH)) {
                    path = applicationApiPath;
                } else {
                    path = SLASH + applicationApiPath;
                }
            }
        } else {
            path = "";
        }
        return path;
    }

}
