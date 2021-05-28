package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.MapSchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.MapSchedulerExecutionRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;

@Configuration
public class ServerSchedulerMapConfiguration {

    @Bean
    public SchedulerExecutionRepository schedulerExecutionRepository() {
        return new MapSchedulerExecutionRepository();
    }

    @Bean
    public SchedulerConfigurationRepository schedulerConfigurationRepository() {
        return new MapSchedulerConfigurationRepository();
    }
}
