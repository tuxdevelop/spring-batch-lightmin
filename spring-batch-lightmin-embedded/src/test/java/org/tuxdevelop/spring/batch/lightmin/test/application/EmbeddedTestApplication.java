package org.tuxdevelop.spring.batch.lightmin.test.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.annotation.EnableLightminEmbedded;
import org.tuxdevelop.spring.batch.lightmin.repository.annotation.EnableLightminMapConfigurationRepository;
import org.tuxdevelop.test.configuration.ITJobConfiguration;

import java.util.List;

@SpringBootApplication
@EnableLightminEmbedded
@EnableLightminMapConfigurationRepository
public class EmbeddedTestApplication {


    public static void main(final String[] args) {
        SpringApplication.run(EmbeddedTestApplication.class);
    }

    @Slf4j
    @Configuration
    static class JobConfiguration {

        private final JobBuilderFactory jobBuilderFactory;
        private final StepBuilderFactory stepBuilderFactory;

        @Autowired
        JobConfiguration(final JobBuilderFactory jobBuilderFactory, final StepBuilderFactory stepBuilderFactory) {
            this.jobBuilderFactory = jobBuilderFactory;
            this.stepBuilderFactory = stepBuilderFactory;
        }

        @Bean
        public Job simpleJob() {
            return this.jobBuilderFactory
                    .get("simpleJob")
                    .start(this.simpleStep())
                    .build();
        }

        @Bean
        public Step simpleStep() {
            return this.stepBuilderFactory
                    .get("simpleStep")
                    .<Long, Long>chunk(1)
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
            public void write(final List<? extends Long> list) throws Exception {
                for (final Long value : list) {
                    log.info(String.valueOf(value));
                }
            }

        }
    }
}


