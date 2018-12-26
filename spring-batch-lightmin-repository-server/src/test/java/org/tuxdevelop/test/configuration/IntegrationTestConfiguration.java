package org.tuxdevelop.test.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.tuxdevelop.spring.batch.lightmin.repository.annotation.EnableLightminMapConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.repository.server.configuration.EnableLightminRepositoryServer;
import org.tuxdevelop.spring.batch.lightmin.test.util.ITMapJobConfigurationRepository;

@Configuration
@EnableLightminRepositoryServer
@EnableLightminMapConfigurationRepository
@PropertySource(value = {"classpath:application.properties"})
public class IntegrationTestConfiguration {

    @Bean
    public ITMapJobConfigurationRepository itJobConfigurationRepository() {
        return new ITMapJobConfigurationRepository();
    }

}
