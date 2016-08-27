package org.tuxdevelop.spring.batch.lightmin.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.tuxdevelop.spring.batch.lightmin.configuration.EnableSpringBatchLightmin;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.EnableEmbeddedSpringBatchLightminServer;

@EnableAutoConfiguration
@EnableEmbeddedSpringBatchLightminServer
@EnableSpringBatchLightmin
public class ITConfigurationApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ITConfigurationApplication.class, args);
    }
}
