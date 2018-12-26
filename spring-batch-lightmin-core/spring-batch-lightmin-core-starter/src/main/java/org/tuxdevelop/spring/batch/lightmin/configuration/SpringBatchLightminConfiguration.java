package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.annotation.EnableLightminService;
import org.tuxdevelop.spring.batch.lightmin.repository.configuration.LightminJobConfigurationRepositoryConfigurer;

@Configuration
@EnableLightminService
public class SpringBatchLightminConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = {LightminJobConfigurationRepositoryConfigurer.class})
    public LightminJobConfigurationRepositoryConfigurer lightminJobConfigurationRepositoryConfigurer() {
        return new LightminJobConfigurationRepositoryConfigurer();
    }

}
