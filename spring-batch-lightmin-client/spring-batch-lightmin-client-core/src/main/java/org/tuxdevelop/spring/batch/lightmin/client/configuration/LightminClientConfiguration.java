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
            name = "metrics-enabled",
            havingValue = "true",
            matchIfMissing = true)
    static class ClientMetricsConfiguration {

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

        @Bean
        public MetricEventPublisher metricEventPublisher() {
            return new MetricEventPublisher();
        }
    }

    public static class LightminServerRestTemplateFactory {

        private static RestTemplate restTemplate;

        private LightminServerRestTemplateFactory() {
        }

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
