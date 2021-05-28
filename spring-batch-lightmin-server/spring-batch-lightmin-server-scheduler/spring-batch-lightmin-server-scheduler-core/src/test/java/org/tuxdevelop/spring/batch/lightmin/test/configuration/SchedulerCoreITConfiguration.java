package org.tuxdevelop.spring.batch.lightmin.test.configuration;

import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.server.annotation.EnableLightminServerCore;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.LightminServerCoreProperties;
import org.tuxdevelop.spring.batch.lightmin.server.repository.*;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.annotation.EnableServerSchedulerCore;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration.ServerSchedulerCoreConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.*;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.service.*;

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

    @Bean
    public LightminApplicationRepository lightminApplicationRepository() {
        return new MapLightminApplicationRepository();
    }

    @Bean
    public JobExecutionEventRepository jobExecutionEventRepository(final LightminServerCoreProperties lightminServerCoreProperties) {
        return new MapJobExecutionEventRepository(lightminServerCoreProperties.getEventRepositorySize());
    }

    @Bean
    public JournalRepository journalRepository() {
        return new MapJournalRepository();
    }

    @Bean
    public SchedulerExecutionRepository schedulerExecutionRepository() {
        return new MapSchedulerExecutionRepository();
    }

    @Bean
    public SchedulerConfigurationRepository schedulerConfigurationRepository() {
        return new MapSchedulerConfigurationRepository();
    }

    @Bean
    public ExecutionPollerService executionPollerService(final ServerSchedulerService serverSchedulerService,
                                                         final SchedulerExecutionService schedulerExecutionService,
                                                         final ServerSchedulerCoreConfigurationProperties properties) {
        return new StandaloneExecutionPollerService(serverSchedulerService, schedulerExecutionService, properties);
    }

    @Bean
    public ExecutionCleanUpService executionCleanUpService(final SchedulerExecutionRepository schedulerExecutionRepository,
                                                           final ServerSchedulerCoreConfigurationProperties properties) {
        return new StandaloneExecutionCleanupService(schedulerExecutionRepository, properties);
    }

}
