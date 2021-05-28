package org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.tuxdevelop.spring.batch.lightmin.server.annotation.EnableLightminServerCore;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.service.*;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;

@Configuration
@EnableScheduling
@EnableLightminServerCore
@ConditionalOnProperty(prefix = "spring.batch.lightmin.server.scheduler", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(value = {ServerSchedulerCoreConfigurationProperties.class})
public class ServerSchedulerCoreConfiguration {

    @Bean
    public ServerSchedulerService executionRunnerService(final SchedulerConfigurationService schedulerConfigurationService,
                                                         final SchedulerExecutionService schedulerExecutionService,
                                                         final JobServerService jobServerService,
                                                         final LightminApplicationRepository lightminApplicationRepository) {
        return new ServerSchedulerService(
                schedulerConfigurationService,
                schedulerExecutionService,
                jobServerService,
                lightminApplicationRepository);
    }

    @Bean
    public SchedulerConfigurationService schedulerConfigurationService(
            final SchedulerConfigurationRepository schedulerConfigurationRepository,
            final SchedulerExecutionRepository schedulerExecutionRepository) {
        return new SchedulerConfigurationService(schedulerConfigurationRepository, schedulerExecutionRepository);
    }

    @Bean
    public SchedulerExecutionService schedulerExecutionService(
            final SchedulerExecutionRepository schedulerExecutionRepository) {
        return new SchedulerExecutionService(schedulerExecutionRepository);
    }

    @Bean
    public ServerPollerService serverPollerService(final ExecutionPollerService executionPollerService,
                                                   final ExecutionCleanUpService executionCleanUpService) {
        return new ServerPollerService(executionPollerService, executionCleanUpService);
    }

    @Bean
    public ExecutionInfoService executionInfoService(final SchedulerExecutionService schedulerExecutionService,
                                                     final SchedulerConfigurationService schedulerConfigurationService) {
        return new ExecutionInfoService(schedulerExecutionService, schedulerConfigurationService);
    }
}
