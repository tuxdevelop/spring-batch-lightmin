package org.tuxdevelop.spring.batch.lightmin.repository.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.repository.MapJobConfigurationRepository;

@Configuration
public class MapJobConfigurationRepositoryConfiguration extends LightminJobConfigurationRepositoryConfigurer {

    @Override
    @Bean
    public JobConfigurationRepository jobConfigurationRepository() {
        return new MapJobConfigurationRepository();
    }

    @Override
    protected void configureJobConfigurationRepository() {
        this.setJobConfigurationRepository(this.jobConfigurationRepository());
    }


}
