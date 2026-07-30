package org.tuxdevelop.spring.batch.lightmin.test.configuration;

import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.springframework.boot.micrometer.metrics.autoconfigure.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.micrometer.metrics.autoconfigure.MetricsAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.annotation.EnableServerClusterInfinispan;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.CleanUpRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.DefaultCleanUpRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;

import static org.mockito.Mockito.mock;

@Configuration
@EnableServerClusterInfinispan
@Import(value = {MetricsAutoConfiguration.class, CompositeMeterRegistryAutoConfiguration.class})
public class InfinispanITConfiguration {

    @Bean
    public JobServerService jobServerService() {
        return mock(JobServerService.class);
    }

    @Bean
    public GlobalConfiguration globalConfiguration() {
        return new GlobalConfigurationBuilder()
                .clusteredDefault()
                .build();
    }

    @Bean
    public CleanUpRepository cleanUpRepository(final SchedulerConfigurationRepository schedulerConfigurationRepository,
                                               final SchedulerExecutionRepository schedulerExecutionRepository) {
        return new DefaultCleanUpRepository(schedulerConfigurationRepository, schedulerExecutionRepository);
    }
}
