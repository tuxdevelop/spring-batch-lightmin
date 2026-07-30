package org.tuxdevelop.spring.batch.lightmin.repository.configuration;

import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.repository.MapJobConfigurationRepository;

@Configuration
public class MapJobConfigurationRepositoryConfiguration extends LightminJobConfigurationRepositoryConfigurer {

    @Override
    protected void configureJobConfigurationRepository() {
        this.setJobConfigurationRepository(new MapJobConfigurationRepository());
    }

}
