package org.tuxdevelop.spring.batch.lightmin.client.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminProperties;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class JobExecutionEventPublisher {

    private final RestTemplate restTemplate;
    private final LightminProperties lightminProperties;

    public JobExecutionEventPublisher(final RestTemplate restTemplate, final LightminProperties lightminProperties) {
        this.restTemplate = restTemplate;
        this.lightminProperties = lightminProperties;
    }

    public void publishJobExecutionEvent(final JobExecutionEventInfo jobExecutionEventInfo) {
        for (final String lightminUrl : this.lightminProperties.getLightminUrl()) {
            try {
                final String url = lightminUrl + "/api/events/jobexecutions";
                final ResponseEntity<Void> response = this.restTemplate.postForEntity(url, jobExecutionEventInfo, Void.class);
                if (HttpStatus.CREATED.equals(response.getStatusCode())) {
                    log.debug("Send JobExecutionEventInfo > {} to server > {}", jobExecutionEventInfo, lightminUrl);
                }
            } catch (final Exception e) {
                log.warn("Could not send JobExecutionEventInfo > {} to server {}. Error {} ", jobExecutionEventInfo, lightminUrl, e.getMessage());
            }
        }
    }

}
