package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.server.discovery.DiscoveryRegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.server.discovery.LightminApplicationDiscoveryListener;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

@Configuration
@ConditionalOnProperty(prefix = "spring.batch.lightmin.server", value = "discovery-enabled", havingValue = "true")
@AutoConfigureAfter(value = {CommonsClientAutoConfiguration.class})
public class LightminServerStandaloneDiscoveryConfiguration {


    @Bean
    public DiscoveryRegistrationBean discoveryRegistrationBean(final RegistrationBean registrationBean,
                                                               final LightminServerCoreProperties lightminServerCoreProperties) {
        final RestTemplate restTemplate = LightminServerCoreConfiguration.RestTemplateFactory.getRestTemplate(lightminServerCoreProperties);
        return new DiscoveryRegistrationBean(registrationBean, restTemplate);
    }

    @Bean
    public LightminApplicationDiscoveryListener lightminApplicationDiscoveryListener(final DiscoveryClient discoveryClient,
                                                                                     final DiscoveryRegistrationBean discoveryRegistrationBean,
                                                                                     final HeartbeatMonitor heartbeatMonitor) {
        return new LightminApplicationDiscoveryListener(discoveryClient, discoveryRegistrationBean, heartbeatMonitor);
    }

    @Bean
    @ConditionalOnMissingBean(HeartbeatMonitor.class)
    public HeartbeatMonitor heartbeatMonitor() {
        return new HeartbeatMonitor();
    }
}
