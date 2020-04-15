package org.tuxdevelop.spring.batch.lightmin.server.sample.application.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.annotation.EnableLightminClientConsul;
import org.tuxdevelop.spring.batch.lightmin.repository.annotation.EnableLightminRemoteConfigurationRepository;

import java.util.Properties;

@EnableScheduling
@EnableLightminClientConsul
@EnableLightminRemoteConfigurationRepository
@SpringBootApplication(exclude = {BatchAutoConfiguration.class})
@PropertySource(value = "classpath:properties/sample/client/client.properties")
public class AddressMigratorApp2 {

    public static void main(final String[] args) {
        final SpringApplication application = new SpringApplication(AddressMigratorApp2.class);
        final Properties properties = new Properties();
        properties.putIfAbsent("server.port", 9010);
        application.setDefaultProperties(properties);
        application.run(args);
    }

}
