package org.tuxdevelop.spring.batch.lightmin.address_migrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.tuxdevelop.spring.batch.lightmin.configuration.EnableSpringBatchLightminUI;

@Configuration
@EnableScheduling
@EnableSpringBatchLightminUI
@EnableAutoConfiguration(exclude = {BatchAutoConfiguration.class})
@ComponentScan(basePackages = "org.tuxdevelop.spring.batch.lightmin.address_migrator")
public class AddressMigratorApp {

    public static void main(final String[] args) {
        SpringApplication.run(AddressMigratorApp.class, args);
    }

}
