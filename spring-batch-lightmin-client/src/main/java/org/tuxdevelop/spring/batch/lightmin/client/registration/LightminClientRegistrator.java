package org.tuxdevelop.spring.batch.lightmin.client.registration;

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
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminProperties;
import org.tuxdevelop.spring.batch.lightmin.client.server.LightminServerLocator;
import org.tuxdevelop.spring.batch.lightmin.util.RequestUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Slf4j
public class LightminClientRegistrator {

    private static final String SLASH = "/";

    private final AtomicReference<String> registeredId = new AtomicReference<>();

    private final LightminClientProperties lightminClientProperties;
    private final LightminProperties lightminProperties;
    private final RestTemplate restTemplate;
    private final JobRegistry jobRegistry;
    private final LightminServerLocator lightminServerLocator;

    public LightminClientRegistrator(final LightminClientProperties lightminClientProperties,
                                     final LightminProperties lightminProperties,
                                     final RestTemplate restTemplate,
                                     final JobRegistry jobRegistry, final LightminServerLocator lightminServerLocator) {
        this.lightminClientProperties = lightminClientProperties;
        this.lightminProperties = lightminProperties;
        this.restTemplate = restTemplate;
        this.jobRegistry = jobRegistry;
        this.lightminServerLocator = lightminServerLocator;
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

        final List<String> serverUrls = this.lightminServerLocator.getRemoteUrls();
        for (final String lightminUrl : serverUrls) {
            try {
                final String applicationPath =
                        this.getLightminServerApplicationPath(
                                lightminUrl,
                                this.lightminProperties.getApiApplicationsPath());
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
                    if (this.lightminProperties.isRegisterOnce()) {
                        break;
                    }
                } else {
                    log.warn("Application failed to registered itself as {}. Response: {}", lightminClientApplication,
                            response.toString());
                }
            } catch (final Exception ex) {
                log.warn("Failed to register application as {} at spring-boot-lightminProperties ({}): {}",
                        lightminClientApplication, this.lightminProperties.getLightminUrl(), ex.getMessage());
            }
        }
        return isRegistrationSuccessful;
    }

    @EventListener(ContextClosedEvent.class)
    public void deregister(final ContextClosedEvent event) {
        log.debug("Retrievied ContextClosedEvent for dereigistration: {}", event);
        if (this.lightminProperties.isAutoDeregistration()) {
            final String id = this.registeredId.get();
            if (id != null) {
                final List<String> serverUrls = this.lightminServerLocator.getRemoteUrls();
                for (final String lightminUrl : serverUrls) {
                    try {
                        final String applicationPath =
                                this.getLightminServerApplicationPath(
                                        lightminUrl,
                                        this.lightminProperties.getApiApplicationsPath());
                        this.restTemplate.delete(lightminUrl + applicationPath + "/" + id);
                        this.registeredId.compareAndSet(id, null);
                        if (this.lightminProperties.isRegisterOnce()) {
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
