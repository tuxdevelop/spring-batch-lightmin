package org.tuxdevelop.spring.batch.lightmin.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.configuration.EnableSpringBatchLightmin;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.EnableSpringBatchLightminServer;

@Configuration
@EnableAutoConfiguration
@EnableSpringBatchLightminServer
@EnableSpringBatchLightmin
public class ITServerConfigurationApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ITServerConfigurationApplication.class, args);
    }
}
