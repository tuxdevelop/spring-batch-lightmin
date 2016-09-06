package org.tuxdevelop.spring.batch.lightmin.server.sample.application.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.EnableSpringBatchLightminClient;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.EnableSpringBatchLightminServer;

@SpringBootApplication
@EnableSpringBatchLightminServer
@EnableSpringBatchLightminClient
@PropertySource(value = "classpath:properties/sample/server/server.properties")
@ComponentScan(basePackages = "org.tuxdevelop.spring.batch.lightmin.server.sample.application.server")
public class LightminServer {


    public static void main(final String[] args) {
        SpringApplication.run(LightminServer.class, args);
    }
}
