package org.tuxdevelop.spring.batch.lightmin.test.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.server.annotation.EnableLightminServerCore;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.annotation.EnableServerSchedulerCore;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.CleanUpRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.DefaultCleanUpRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;

@Configuration
@EnableServerSchedulerCore
@EnableLightminServerCore
public class SchedulerCoreITConfiguration {

    @Bean
    public CleanUpRepository cleanUpRepository(final SchedulerExecutionRepository schedulerExecutionRepository,
                                               final SchedulerConfigurationRepository schedulerConfigurationRepository) {
        return new DefaultCleanUpRepository(schedulerConfigurationRepository, schedulerExecutionRepository);
    }
}
