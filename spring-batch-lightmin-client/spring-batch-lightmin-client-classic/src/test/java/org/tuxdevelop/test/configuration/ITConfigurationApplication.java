package org.tuxdevelop.test.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.tuxdevelop.spring.batch.lightmin.client.classic.annotation.EnableLightminClientClassic;
import org.tuxdevelop.spring.batch.lightmin.repository.annotation.EnableLightminMapConfigurationRepository;

@EnableAutoConfiguration
@EnableLightminMapConfigurationRepository
@EnableLightminClientClassic
public class ITConfigurationApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ITConfigurationApplication.class, args);
    }
}
