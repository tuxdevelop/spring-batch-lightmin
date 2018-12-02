package org.tuxdevelop.spring.batch.lightmin.repository.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.repository.server.api.controller.JobConfigurationRepositoryController;


/**
 * @author Marcel Becker
 * @since 0.4
 */
@Configuration
public class SpringBatchLightminRemoteRepositoryServerConfiguration {
    
    @Bean
    public JobConfigurationRepositoryController repositoryController(
            final JobConfigurationRepository jobConfigurationRepository) {
        return new JobConfigurationRepositoryController(jobConfigurationRepository);
    }
}
