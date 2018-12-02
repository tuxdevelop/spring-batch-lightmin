/*
 * Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tuxdevelop.spring.batch.lightmin.server.support;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplicationStatus;
import org.tuxdevelop.spring.batch.lightmin.server.event.LightminClientApplicationChangedEvent;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;

import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Slf4j
public class ClientApplicationStatusUpdater implements ApplicationEventPublisherAware {

    private static final String STATUS_RESPONSE_KEY = "status";
    private final LightminApplicationRepository lightminApplicationRepository;
    private final RestTemplate restTemplate;
    private ApplicationEventPublisher publisher;
    @Setter
    private long heartBeatPeriod = 10000L;

    public ClientApplicationStatusUpdater(final RestTemplate restTemplate,
                                          final LightminApplicationRepository lightminApplicationRepository) {
        this.restTemplate = restTemplate;
        this.lightminApplicationRepository = lightminApplicationRepository;
    }

    public void updateStatusForAllApplications() {
        final long now = System.currentTimeMillis();
        for (final LightminClientApplication lightminClientApplication : this.lightminApplicationRepository.findAll()) {
            if (now - this.heartBeatPeriod > lightminClientApplication.getLightminClientApplicationStatus().getTimeInMills()) {
                this.updateStatus(lightminClientApplication);
            }
        }
    }

    public void updateStatus(final LightminClientApplication lightminClientApplication) {
        final LightminClientApplicationStatus currentStatus = lightminClientApplication.getLightminClientApplicationStatus();
        final LightminClientApplicationStatus newStatus = this.getStatus(lightminClientApplication);
        lightminClientApplication.setLightminClientApplicationStatus(newStatus);
        this.lightminApplicationRepository.save(lightminClientApplication);

        if (!newStatus.getStatus().equals(currentStatus.getStatus())) {
            this.publisher.publishEvent(
                    new LightminClientApplicationChangedEvent(lightminClientApplication, currentStatus.getStatus(), newStatus.getStatus()));
        }
    }

    private LightminClientApplicationStatus getStatus(final LightminClientApplication lightminClientApplication) {
        LightminClientApplicationStatus lightminClientApplicationStatus;
        try {
            @SuppressWarnings("unchecked") final ResponseEntity<Map<String, Object>> response =
                    this.restTemplate.getForEntity(lightminClientApplication.getHealthUrl(), (Class<Map<String, Object>>) (Class<?>) Map.class);

            if (response.hasBody() && response.getBody().get(STATUS_RESPONSE_KEY) instanceof String) {
                lightminClientApplicationStatus = LightminClientApplicationStatus.valueOf((String) response.getBody().get(STATUS_RESPONSE_KEY));
            } else if (response.getStatusCode().is2xxSuccessful()) {
                lightminClientApplicationStatus = LightminClientApplicationStatus.ofUp();
            } else {
                lightminClientApplicationStatus = LightminClientApplicationStatus.ofOffline();
            }

        } catch (final Exception ex) {
            if (LightminClientApplicationStatus.ofOffline().getStatus().equals(lightminClientApplication
                    .getLightminClientApplicationStatus().getStatus())) {
                log.debug("Error while getting status for {}", lightminClientApplication, ex);
            } else {
                log.warn("Error while getting status for {}", lightminClientApplication, ex);
            }
            lightminClientApplicationStatus = LightminClientApplicationStatus.ofOffline();
        }
        return lightminClientApplicationStatus;
    }

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

}
