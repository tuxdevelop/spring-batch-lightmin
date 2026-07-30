package org.tuxdevelop.spring.batch.lightmin.test.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.PlatformTransactionManager;
import org.tuxdevelop.spring.batch.lightmin.annotation.EnableLightminEmbedded;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.repository.annotation.EnableLightminMapConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ServerSchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.service.ServerSchedulerService;
import org.tuxdevelop.test.configuration.ITJobConfiguration;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableLightminEmbedded
@EnableLightminMapConfigurationRepository
public class EmbeddedTestApplication {


    public static void main(final String[] args) {
        SpringApplication.run(EmbeddedTestApplication.class);
    }

    @Bean
    public ApplicationRunner applicationRunner(final Environment environment,
                                               final ServerSchedulerService serverSchedulerService) {
        return args -> {
            final SchedulerConfiguration schedulerConfiguration = new SchedulerConfiguration();
            schedulerConfiguration.setApplication(environment.getProperty("spring.application.name"));
            schedulerConfiguration.setJobName("simpleJob");
            schedulerConfiguration.setMaxRetries(3);
            schedulerConfiguration.setInstanceExecutionCount(1);
            schedulerConfiguration.setRetryable(Boolean.TRUE);
            schedulerConfiguration.setCronExpression("0/30 * * * * ?");
            schedulerConfiguration.setJobIncrementer(JobIncrementer.DATE);
            schedulerConfiguration.setStatus(ServerSchedulerStatus.ACTIVE);
            final Map<String, Object> jobParameters = new HashMap<>();
            jobParameters.put("my-long-value", 200L);
            jobParameters.put("my-string", "hello");
            schedulerConfiguration.setJobParameters(jobParameters);

            serverSchedulerService.initSchedulerExecution(schedulerConfiguration);


        };
    }

    @Slf4j
    @Configuration
    static class JobConfiguration {

        @Bean
        public Job simpleJob(final JobRepository jobRepository, final Step simpleStep) {
            return new JobBuilder("simpleJob", jobRepository)
                    .start(simpleStep)
                    .build();
        }

        @Bean
        public Step simpleStep(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
            return new StepBuilder("simpleStep", jobRepository)
                    .<Long, Long>chunk(1, transactionManager)
                    .reader(new ITJobConfiguration.SimpleReader())
                    .writer(new ITJobConfiguration.SimpleWriter())
                    .allowStartIfComplete(Boolean.TRUE)
                    .build();
        }

        public static class SimpleReader implements ItemReader<Long> {

            private static final Long[] values = {1L, 2L, 3L, 4L};
            private int index = 0;

            @Override
            public Long read() throws Exception {
                final Long value = this.index >= values.length ? null : values[this.index];
                this.index++;
                return value;
            }

        }

        public static class SimpleWriter implements ItemWriter<Long> {
            @Override
            public void write(final Chunk<? extends Long> items) throws Exception {
                for (final Long value : items) {
                    log.info(String.valueOf(value));
                }
            }

        }
    }
}

