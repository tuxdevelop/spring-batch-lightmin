package org.tuxdevelop.spring.batch.lightmin.test.configuration;

import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.server.annotation.EnableLightminServerCore;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.annotation.EnableServerSchedulerCore;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.CleanUpRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.DefaultCleanUpRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;

@Configuration
@EnableServerSchedulerCore
@EnableLightminServerCore
@Import(value = {MetricsAutoConfiguration.class, CompositeMeterRegistryAutoConfiguration.class})
public class SchedulerCoreITConfiguration {

    @Bean
    public CleanUpRepository cleanUpRepository(final SchedulerExecutionRepository schedulerExecutionRepository,
                                               final SchedulerConfigurationRepository schedulerConfigurationRepository) {
        return new DefaultCleanUpRepository(schedulerConfigurationRepository, schedulerExecutionRepository);
    }
}
