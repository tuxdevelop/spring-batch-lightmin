package org.tuxdevelop.spring.batch.lightmin.address_migrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.tuxdevelop.spring.batch.lightmin.configuration.EnableSpringBatchLightminUI;

@Configuration
@EnableScheduling
@EnableSpringBatchLightminUI
@EnableAutoConfiguration
@ComponentScan(basePackages = "org.tuxdevelop.spring.batch.lightmin.address_migrator")
public class AddressMigratorApp extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(AddressMigratorApp.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }

    private static Class<AddressMigratorApp> applicationClass = AddressMigratorApp.class;

}
