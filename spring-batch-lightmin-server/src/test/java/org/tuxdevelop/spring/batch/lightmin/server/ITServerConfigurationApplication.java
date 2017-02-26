package org.tuxdevelop.spring.batch.lightmin.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.tuxdevelop.spring.batch.lightmin.configuration.EnableSpringBatchLightmin;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.EnableSpringBatchLightminServer;

@Configuration
@EnableAutoConfiguration
@EnableSpringBatchLightminServer
@EnableSpringBatchLightmin
@PropertySource(value = {"classpath:properties/local_client.properties"})
public class ITServerConfigurationApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ITServerConfigurationApplication.class, args);
    }
}
