package org.tuxdevelop.spring.batch.lightmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.MapJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.configuration.EnableSpringBatchLightminUI;

@SpringBootApplication
@EnableSpringBatchLightminUI
public class ITConfigurationApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ITConfigurationApplication.class, args);
    }

    @Bean
    public JobConfigurationRepository jobConfigurationRepository() {
        return new MapJobConfigurationRepository();
    }
}
