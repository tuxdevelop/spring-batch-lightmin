package org.tuxdevelop.test.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.tuxdevelop.spring.batch.lightmin.annotation.EnableLightminEmbedded;

@Configuration
@EnableAutoConfiguration
@EnableLightminEmbedded
@PropertySource(value = {"classpath:properties/local_client.properties"})
public class ITEmbeddedConfigurationApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ITEmbeddedConfigurationApplication.class, args);
    }
}
