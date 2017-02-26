package org.tuxdevelop.test.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.repository.server.configuration.EnableSpringBatchLightminRemoteRepositoryServer;
import org.tuxdevelop.spring.batch.lightmin.test.util.ITJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.test.util.ITMapJobConfigurationRepository;

@Configuration
@EnableAutoConfiguration
@EnableSpringBatchLightminRemoteRepositoryServer
@EnableConfigurationProperties(value = {SpringBatchLightminConfigurationProperties.class})
public class RemoteIntegrationTestConfiguration {

    @Bean
    public ITJobConfigurationRepository itJobConfigurationRepository() {
        return new ITMapJobConfigurationRepository();
    }

}
