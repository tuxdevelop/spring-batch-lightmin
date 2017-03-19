package org.tuxdevelop.test.configuration;

import org.mockito.Mockito;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.tuxdevelop.spring.batch.lightmin.configuration.EnableSpringBatchLightmin;
import org.tuxdevelop.spring.batch.lightmin.configuration.JobCreationListener;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;

@Configuration
@EnableSpringBatchLightmin
@EnableConfigurationProperties(SpringBatchLightminConfigurationProperties.class)
@PropertySource(value = "classpath:properties/remote.properties")
@Import(value = {ITSchedulerConfiguration.class, ITJobConfiguration.class})
public class ITRemoteConfiguration {

    @Bean
    public JobCreationListener jobCreationListener() {
        return Mockito.mock(JobCreationListener.class);
    }

}
