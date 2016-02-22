package org.tuxdevelop.spring.batch.lightmin;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.MapJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.configuration.EnableSpringBatchLightmin;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;

@Profile("mapOnly")
@Configuration
@EnableSpringBatchLightmin
@EnableConfigurationProperties(value = {SpringBatchLightminConfigurationProperties.class})
@PropertySource("classpath:properties/map.properties")
@Import(value = {ITJobConfiguration.class})
public class ITMapConfiguration {

    @Bean
    public JobConfigurationRepository jobConfigurationRepository() {
        return new MapJobConfigurationRepository();
    }

}
