package org.tuxdevelop.spring.batch.lightmin.client.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class JobExecutionEventPublisher implements ApplicationContextAware {

    private final RestTemplate restTemplate;
    private final LightminProperties lightminProperties;
    private final LightminClientProperties lightminClientProperties;
    private ApplicationContext applicationContext;

    public JobExecutionEventPublisher(final RestTemplate restTemplate, final LightminProperties lightminProperties,
                                      final LightminClientProperties lightminClientProperties) {
        this.restTemplate = restTemplate;
        this.lightminProperties = lightminProperties;
        this.lightminClientProperties = lightminClientProperties;
    }

    public void publishJobExecutionEvent(final JobExecutionEventInfo jobExecutionEventInfo) {
        final List<String> lightminUrls = getLightminServerUrls();
        log.debug("Sending JobExecutionInfos to Servers {}", lightminUrls);
        lightminUrls.parallelStream()
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
        final List<String> serverList = new ArrayList<>();
        if (this.lightminClientProperties.getDiscoverServer()) {
            final List<ServiceInstance> serverInstances = getDiscoveryClient().getInstances(this.lightminClientProperties.getServerDiscoveryName());
            serverInstances.parallelStream()
                    .forEach(
                            serviceInstance -> serverList.add(serviceInstance.getUri().toString())
                    );
        } else {
            if (this.lightminProperties.getLightminUrl() != null && this.lightminProperties.getLightminUrl().length > 0) {
                Collections.addAll(serverList, this.lightminProperties.getLightminUrl());
            } else {
                log.debug("No Lightmin Server Urls are available");
            }
        }
        return serverList;
    }

    private DiscoveryClient getDiscoveryClient() {
        return this.applicationContext.getBean(DiscoveryClient.class);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
