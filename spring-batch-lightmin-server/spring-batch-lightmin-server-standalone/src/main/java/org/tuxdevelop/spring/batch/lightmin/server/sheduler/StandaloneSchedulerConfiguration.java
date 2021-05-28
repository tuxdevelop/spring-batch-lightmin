package org.tuxdevelop.spring.batch.lightmin.server.sheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration.ServerSchedulerCoreConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.service.*;

@Configuration
public class StandaloneSchedulerConfiguration {
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
