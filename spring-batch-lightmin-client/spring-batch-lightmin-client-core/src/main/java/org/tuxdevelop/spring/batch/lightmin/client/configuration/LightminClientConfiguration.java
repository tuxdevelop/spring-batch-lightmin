package org.tuxdevelop.spring.batch.lightmin.client.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.annotation.EnableLightminCore;
import org.tuxdevelop.spring.batch.lightmin.client.api.controller.LightminClientApplicationRestController;
import org.tuxdevelop.spring.batch.lightmin.client.event.JobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.event.RemoteJobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.listener.OnJobExecutionFinishedEventListener;
import org.tuxdevelop.spring.batch.lightmin.client.service.LightminClientApplicationService;
import org.tuxdevelop.spring.batch.lightmin.client.service.LightminServerLocatorService;
import org.tuxdevelop.spring.batch.lightmin.util.BasicAuthHttpRequestInterceptor;

import java.util.Collections;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Configuration
@EnableLightminCore
@EnableConfigurationProperties(value = {LightminClientProperties.class})
public class LightminClientConfiguration {

    @Bean
    public LightminClientApplicationService lightminClientApplicationService(
            final JobRegistry jobRegistry,
            final LightminClientProperties lightminClientProperties) {
        return new LightminClientApplicationService(jobRegistry, lightminClientProperties);
    }

    @Bean
    public LightminClientApplicationRestController lightminClientApplicationRestController(
            final LightminClientApplicationService lightminClientApplicationService) {
        return new LightminClientApplicationRestController(lightminClientApplicationService);
    }

    @Configuration
    @ConditionalOnProperty(
            prefix = "spring.batch.lightmin.client",
            value = "publish-job-events",
            havingValue = "true",
            matchIfMissing = true)
    public static class LightminClientPublishEventsConfiguration {

        @Bean
        @ConditionalOnMissingBean(value = {JobExecutionEventPublisher.class})
        public JobExecutionEventPublisher jobExecutionEventPublisher(
                final LightminServerLocatorService lightminServerLocatorService,
                final LightminClientProperties lightminClientProperties) {
            final RestTemplate restTemplate = LightminServerRestTemplateFactory.getRestTemplate(
                    lightminClientProperties.getServer());
            return new RemoteJobExecutionEventPublisher(restTemplate, lightminServerLocatorService);
        }

        @Bean
        public OnJobExecutionFinishedEventListener onJobExecutionFinishedEventListener(
                final JobExecutionEventPublisher jobExecutionEventPublisher) {
            return new OnJobExecutionFinishedEventListener(jobExecutionEventPublisher);
        }
    }

    public static class LightminServerRestTemplateFactory {

        private static RestTemplate restTemplate;

        public static RestTemplate getRestTemplate(
                final LightminClientProperties.ClientServerProperties lightminProperties) {

            if (restTemplate == null) {
                restTemplate = new RestTemplate();
            }
            if (lightminProperties.getUsername() != null) {
                restTemplate.setInterceptors(
                        Collections.singletonList(
                                new BasicAuthHttpRequestInterceptor(lightminProperties.getUsername(),
                                        lightminProperties.getPassword()))
                );
            }
            return restTemplate;
        }
    }

}
