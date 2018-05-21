package org.tuxdevelop.spring.batch.lightmin.client.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.server.LightminServerLocator;

import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class JobExecutionEventPublisher {

    private final RestTemplate restTemplate;
    private final LightminServerLocator lightminServerLocator;

    public JobExecutionEventPublisher(final RestTemplate restTemplate, final LightminServerLocator lightminServerLocator) {
        this.restTemplate = restTemplate;
        this.lightminServerLocator = lightminServerLocator;
    }

    public void publishJobExecutionEvent(final JobExecutionEventInfo jobExecutionEventInfo) {
        final List<String> lightminUrls = this.getLightminServerUrls();
        log.debug("Sending JobExecutionInfos to Servers {}", lightminUrls);
        lightminUrls
                .forEach(
                        lightminUrl -> {
                            try {
                                final String url = lightminUrl + "/api/events/jobexecutions";
                                final ResponseEntity<Void> response = JobExecutionEventPublisher.this.restTemplate.postForEntity(url, jobExecutionEventInfo, Void.class);
                                if (HttpStatus.CREATED.equals(response.getStatusCode())) {
                                    log.debug("Send JobExecutionEventInfo > {} to server > {}", jobExecutionEventInfo, lightminUrl);
                                }
                            } catch (final Exception e) {
                                log.warn("Could not send JobExecutionEventInfo > {} to server {}. Error {} ", jobExecutionEventInfo, lightminUrl, e.getMessage());
                            }
                        }
                );
    }

    private List<String> getLightminServerUrls() {
        return this.lightminServerLocator.getRemoteUrls();
    }


}
