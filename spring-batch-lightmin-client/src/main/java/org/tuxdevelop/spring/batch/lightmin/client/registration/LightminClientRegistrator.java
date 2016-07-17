package org.tuxdevelop.spring.batch.lightmin.client.registration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.SchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.TaskExecutorType;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientInformation;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminProperties;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Slf4j
public class LightminClientRegistrator {

    private static HttpHeaders HTTP_HEADERS = createHttpHeaders();

    private final AtomicReference<String> registeredId = new AtomicReference<>();

    private LightminClientProperties lightminClientProperties;

    private LightminProperties lightminProperties;

    private RestTemplate restTemplate;

    private JobRegistry jobRegistry;

    public LightminClientRegistrator(final LightminClientProperties lightminClientProperties,
                                     final LightminProperties lightminProperties,
                                     final RestTemplate restTemplate,
                                     final JobRegistry jobRegistry) {
        this.lightminClientProperties = lightminClientProperties;
        this.lightminProperties = lightminProperties;
        this.restTemplate = restTemplate;
        this.jobRegistry = jobRegistry;
    }


    public Boolean register() {
        Boolean isRegistrationSuccessful = Boolean.FALSE;
        final LightminClientApplication lightminClientApplication = createApplication();
        for (final String lightminUrl : lightminProperties.getLightminUrl()) {
            try {
                @SuppressWarnings("rawtypes") final
                ResponseEntity<Map> response = restTemplate.postForEntity(lightminUrl,
                        new HttpEntity<>(lightminClientApplication, HTTP_HEADERS), Map.class);

                if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                    if (registeredId.compareAndSet(null, response.getBody().get("id").toString())) {
                        log.info("Application registered itself as {}", response.getBody());
                    } else {
                        log.debug("Application refreshed itself as {}", response.getBody());
                    }
                    isRegistrationSuccessful = Boolean.TRUE;
                    if (lightminProperties.isRegisterOnce()) {
                        break;
                    }
                } else {
                    log.warn("Application failed to registered itself as {}. Response: {}", lightminClientApplication,
                            response.toString());
                }
            } catch (final Exception ex) {
                log.warn("Failed to register application as {} at spring-boot-lightminProperties ({}): {}",
                        lightminClientApplication, lightminProperties.getLightminUrl(), ex.getMessage());
            }
        }
        return isRegistrationSuccessful;
    }

    public void deregister() {
        final String id = registeredId.get();
        if (id != null) {
            for (final String lightminUrl : lightminProperties.getLightminUrl()) {
                try {
                    restTemplate.delete(lightminUrl + "/" + id);
                    registeredId.compareAndSet(id, null);
                    if (lightminProperties.isRegisterOnce()) {
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

    private LightminClientApplication createApplication() {
        final List<String> jobNames = new LinkedList<>(jobRegistry.getJobNames());
        final LightminClientInformation lightminClientInformation = new LightminClientInformation();
        lightminClientInformation.setRegisteredJobs(jobNames);
        lightminClientInformation.setSupportedJobIncrementers(Arrays.asList(JobIncrementer.values()));
        lightminClientInformation.setSupportedSchedulerTypes(Arrays.asList(JobSchedulerType.values()));
        lightminClientInformation.setSupportedSchedulerStatuses(Arrays.asList(SchedulerStatus.values()));
        lightminClientInformation.setSupportedTaskExecutorTypes(Arrays.asList(TaskExecutorType.values()));

        final LightminClientApplication lightminClientApplication = new LightminClientApplication();
        lightminClientApplication.setHealthUrl(lightminClientProperties.getHealthUrl());
        lightminClientApplication.setName(lightminClientProperties.getName());
        lightminClientApplication.setServiceUrl(lightminClientProperties.getServiceUrl());
        lightminClientApplication.setManagementUrl(lightminClientProperties.getManagementUrl());
        lightminClientApplication.setLightminClientInformation(lightminClientInformation);

        return lightminClientApplication;
    }


}
