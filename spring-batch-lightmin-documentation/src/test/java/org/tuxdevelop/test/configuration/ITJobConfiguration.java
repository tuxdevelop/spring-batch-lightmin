package org.tuxdevelop.test.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.tuxdevelop.spring.batch.lightmin.batch.annotation.EnableLightminBatch;

@Slf4j
@Configuration
@EnableLightminBatch
public class ITJobConfiguration {

    @Bean
    public Job simpleJob(final JobRepository jobRepository, final Step simpleStep) {
        return new JobBuilder("simpleJob", jobRepository)
                .start(simpleStep)
                .build();
    }

    @Bean
    public Job simpleBlockingJob(final JobRepository jobRepository, final Step simpleBlockingStep) {
        return new JobBuilder("simpleBlockingJob", jobRepository)
                .start(simpleBlockingStep)
                .build();
    }

    @Bean
    public Step simpleStep(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("simpleStep", jobRepository)
                .<Long, Long>chunk(1, transactionManager)
                .reader(new SimpleReader())
                .writer(new SimpleWriter())
                .allowStartIfComplete(Boolean.TRUE)
                .build();
    }

    @Bean
    public Step simpleBlockingStep(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("simpleBlockingStep", jobRepository)
                .<Long, Long>chunk(1, transactionManager)
                .reader(new SimpleBlockingReader())
                .writer(new SimpleWriter())
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

    public static class SimpleBlockingReader implements ItemReader<Long> {

        private static final Long[] values = {1L, 2L, 3L, 4L};
        private int index = 0;

        @Override
        public Long read() throws Exception {
            if (this.index > 3) {
                this.index = 0;
            }
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
