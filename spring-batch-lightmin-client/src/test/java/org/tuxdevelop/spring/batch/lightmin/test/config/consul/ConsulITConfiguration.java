package org.tuxdevelop.spring.batch.lightmin.test.config.consul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientConfiguration;

@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(value = {LightminClientConfiguration.class})
@PropertySource(value = {"classpath:discovery/consul.properties"})
public class ConsulITConfiguration {

    public static void main(final String[] args) {
        SpringApplication.run(ConsulITConfiguration.class, args);
    }

}
