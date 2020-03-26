package org.tuxdevelop.spring.batch.lightmin.client.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.annotation.EnableLightminCore;
import org.tuxdevelop.spring.batch.lightmin.client.api.controller.LightminClientApplicationRestController;
import org.tuxdevelop.spring.batch.lightmin.client.listener.LightminMetricClientListenerBean;
import org.tuxdevelop.spring.batch.lightmin.client.listener.OnJobExecutionFinishedEventListener;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.*;
import org.tuxdevelop.spring.batch.lightmin.client.service.LightminClientApplicationService;
import org.tuxdevelop.spring.batch.lightmin.client.service.LightminServerLocatorService;
import org.tuxdevelop.spring.batch.lightmin.service.MetricService;
import org.tuxdevelop.spring.batch.lightmin.service.MetricServiceBean;
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

    @Bean(name = "serverRestTemplate")
    @ConditionalOnMissingBean(name = "serverRestTemplate")
    public RestTemplate serverRestTemplate(final LightminClientProperties lightminClientProperties) {
        return LightminServerRestTemplateFactory.getRestTemplate(
                lightminClientProperties.getServer());
    }

    @Configuration
    @ConditionalOnProperty(
            prefix = "spring.batch.lightmin.client",
            value = "publish-job-events",
            havingValue = "true",
            matchIfMissing = true)
    public static class LightminClientPublishEventsConfiguration {

        @Bean
        @ConditionalOnMissingBean(value = {StepExecutionEventPublisher.class})
        public StepExecutionEventPublisher stepExecutionEventPublisher(
                final LightminServerLocatorService lightminServerLocator,
                @Qualifier("serverRestTemplate") final RestTemplate restTemplate) {
            return new RemoteStepExecutionEventPublisher(restTemplate, lightminServerLocator);
        }

        @Bean
        @ConditionalOnMissingBean(value = {JobExecutionEventPublisher.class})
        public JobExecutionEventPublisher jobExecutionEventPublisher(
                final LightminServerLocatorService lightminServerLocatorService,
                @Qualifier("serverRestTemplate") final RestTemplate restTemplate) {
            return new RemoteJobExecutionEventPublisher(restTemplate, lightminServerLocatorService);
        }

        @Bean
        public MetricEventPublisher metricEventPublisher() {
            return new MetricEventPublisher();
        }

        @Bean
        public OnJobExecutionFinishedEventListener onJobExecutionFinishedEventListener(
                final JobExecutionEventPublisher jobExecutionEventPublisher,
                final StepExecutionEventPublisher stepExecutionEventPublisher,
                final MetricEventPublisher metricEventPublisher) {
            return new OnJobExecutionFinishedEventListener(jobExecutionEventPublisher, stepExecutionEventPublisher, metricEventPublisher);
        }

        @Configuration
        @ConditionalOnProperty(prefix = "spring.batch.lightmin.client", name = "metrics-enabled", havingValue = "true")
        static class ServerMetricsConfiguration {

            @Bean
            @ConditionalOnMissingBean(value = MeterRegistryCustomizer.class)
            public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
                return registry -> registry.config();
            }

            @Bean("clientMetricService")
            @ConditionalOnMissingBean(name = "clientMetricService")
            public MetricService metricService(final MeterRegistry registry) {
                return new MetricServiceBean(registry);
            }

            @Bean
            public LightminMetricClientListenerBean lightminClientMetricListener(@Qualifier("clientMetricService") final MetricService metricService) {
                return new LightminMetricClientListenerBean(metricService);
            }
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
