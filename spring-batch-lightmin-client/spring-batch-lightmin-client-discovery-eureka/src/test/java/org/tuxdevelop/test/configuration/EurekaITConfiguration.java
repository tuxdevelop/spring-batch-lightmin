package org.tuxdevelop.test.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.annotation.EnableLightminClientEureka;
import org.tuxdevelop.spring.batch.lightmin.repository.annotation.EnableLightminMapConfigurationRepository;

@EnableAutoConfiguration
@EnableLightminClientEureka
@EnableLightminMapConfigurationRepository
public class EurekaITConfiguration {

    public static void main(final String[] args) {
        SpringApplication.run(EurekaITConfiguration.class, args);
    }

}
