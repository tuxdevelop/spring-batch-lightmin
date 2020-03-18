package org.tuxdevelop.spring.batch.lightmin.client.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.service.LightminServerLocatorService;
import org.tuxdevelop.spring.batch.lightmin.util.RequestUtil;

import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class RemoteMetricEventPublisher implements MetricEventPublisher {

    private final RestTemplate restTemplate;
    private final LightminServerLocatorService lightminServerLocator;

    public RemoteMetricEventPublisher(final RestTemplate restTemplate, final LightminServerLocatorService lightminServerLocator) {
        this.restTemplate = restTemplate;
        this.lightminServerLocator = lightminServerLocator;
    }

    @Override
    public void publishEvent(JobExecutionEventInfo jobExecutionEventInfo) {
        final HttpEntity<JobExecutionEventInfo> entity = RequestUtil.createApplicationJsonEntity(jobExecutionEventInfo);
        final List<String> lightminUrls = this.getLightminServerUrls();
        log.debug("Sending MetricInfos to Servers {}", lightminUrls);
        lightminUrls
                .forEach(
                        lightminUrl ->
                                doRequestForUrl(lightminUrl, "api/events/metrics/jobexecution", entity, jobExecutionEventInfo));
    }

    @Override
    public void publishEvent(StepExecutionEventInfo stepExecutionEventInfo) {
        final HttpEntity<StepExecutionEventInfo> entity = RequestUtil.createApplicationJsonEntity(stepExecutionEventInfo);
        final List<String> lightminUrls = this.getLightminServerUrls();
        log.debug("Sending MetricInfos to Servers {}", lightminUrls);
        lightminUrls
                .forEach(
                        lightminUrl ->
                                doRequestForUrl(lightminUrl, "api/events/metrics/stepexecution", entity, stepExecutionEventInfo));
    }


    private void doRequestForUrl(String lightminUrl, String serviceUrl, HttpEntity entity, Object eventInfo) {
        try {
            final ResponseEntity<Void> response =
                    this.restTemplate.postForEntity(
                            lightminUrl
                                    + serviceUrl,
                            entity,
                            Void.class);

            if (HttpStatus.CREATED.equals(response.getStatusCode())) {
                log.debug("Send JobExecutionEventInfo > {} to server > {}",
                        eventInfo, lightminUrl);
            } else {
                log.warn("Could send JobExecutionEventInfo to Server {}", response);
            }
        } catch (final Exception e) {
            log.warn("Could not send JobExecutionEventInfo > {} to server {}. Error {} ",
                    eventInfo, lightminUrl, e.getMessage());
        }
    }

    private List<String> getLightminServerUrls() {
        return this.lightminServerLocator.getRemoteUrls();
    }
}
