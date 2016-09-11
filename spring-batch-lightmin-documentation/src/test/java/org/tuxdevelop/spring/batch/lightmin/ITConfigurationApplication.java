package org.tuxdevelop.spring.batch.lightmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.EnableSpringBatchLightminClient;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.EnableSpringBatchLightminServer;

@SpringBootApplication
@EnableSpringBatchLightminServer
@EnableSpringBatchLightminClient
public class ITConfigurationApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ITConfigurationApplication.class, args);
    }
}
