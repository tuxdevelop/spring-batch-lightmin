package org.tuxdevelop.spring.batch.lightmin.client.registration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminProperties;
import org.tuxdevelop.spring.batch.lightmin.client.server.LightminServerLocator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Slf4j
public class LightminClientRegistrator {

    private static final HttpHeaders HTTP_HEADERS = createHttpHeaders();

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

        final List<String> serverUrls = this.lightminServerLocator.getRemoteUrls();
        for (final String lightminUrl : serverUrls) {
            try {
                final ResponseEntity<LightminClientApplication> response
                        = this.restTemplate.postForEntity(lightminUrl, new HttpEntity<>(lightminClientApplication, HTTP_HEADERS), LightminClientApplication.class);
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

    public void deregister() {
        final String id = this.registeredId.get();
        if (id != null) {
            final List<String> serverUrls = this.lightminServerLocator.getRemoteUrls();
            for (final String lightminUrl : serverUrls) {
                try {
                    this.restTemplate.delete(lightminUrl + "/" + id);
                    this.registeredId.compareAndSet(id, null);
                    if (this.lightminProperties.isRegisterOnce()) {
                        break;
                    }
                } catch (final Exception ex) {
                    log.warn(
                            "Failed to deregister application (id={}) at spring-boot-admin ({}): {}", id, lightminUrl, ex.getMessage());
                }
            }
        }
    }

    private static HttpHeaders createHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return HttpHeaders.readOnlyHttpHeaders(headers);
    }


}
