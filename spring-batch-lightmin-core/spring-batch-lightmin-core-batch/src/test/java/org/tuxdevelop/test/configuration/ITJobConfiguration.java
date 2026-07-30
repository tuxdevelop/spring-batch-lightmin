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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.repository.annotation.EnableLightminMapConfigurationRepository;

import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@EnableLightminMapConfigurationRepository
public class ITJobConfiguration {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager batchTransactionManager;

    @Bean
    public Job simpleJob() {
        return new JobBuilder("simpleJob", this.jobRepository)
                .start(this.simpleStep())
                .build();
    }

    @Bean
    public Step simpleStep() {
        return new StepBuilder("simpleStep", this.jobRepository)
                .<Long, Long>chunk(1, this.batchTransactionManager)
                .reader(new SimpleReader())
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

    public static class SimpleWriter implements ItemWriter<Long> {
        @Override
        public void write(final Chunk<? extends Long> chunk) throws Exception {
            for (final Long value : chunk) {
                log.info(String.valueOf(value));
            }
        }

    }

}
