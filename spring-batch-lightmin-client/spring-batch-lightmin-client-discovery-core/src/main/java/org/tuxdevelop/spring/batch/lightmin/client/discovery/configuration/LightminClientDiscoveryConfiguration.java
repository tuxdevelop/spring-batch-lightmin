package org.tuxdevelop.spring.batch.lightmin.client.discovery.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientConfiguration;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.listener.DiscoveryListener;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.service.DiscoveryLightminServerLocatorService;
import org.tuxdevelop.spring.batch.lightmin.client.service.LightminServerLocatorService;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Configuration
@EnableDiscoveryClient
@Import(value = {LightminClientConfiguration.class})
@EnableConfigurationProperties(value = {LightminClientDiscoveryProperties.class})
@AutoConfigureAfter({SimpleDiscoveryClientAutoConfiguration.class})
public class LightminClientDiscoveryConfiguration {

    @Bean
    public DiscoveryListener discoveryListener(final LightminClientProperties lightminClientProperties) {
        return new DiscoveryListener(lightminClientProperties);
    }

    @Bean
    public LightminServerLocatorService discoveryLightminServerLocator(
            final LightminClientDiscoveryProperties lightminClientDiscoveryProperties,
            final DiscoveryClient discoveryClient) {
        return new DiscoveryLightminServerLocatorService(lightminClientDiscoveryProperties, discoveryClient);
    }
}
