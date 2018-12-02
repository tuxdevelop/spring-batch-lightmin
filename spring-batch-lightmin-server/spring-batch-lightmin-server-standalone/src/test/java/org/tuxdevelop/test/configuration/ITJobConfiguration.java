package org.tuxdevelop.test.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.batch.annotation.EnableLightminBatch;

import java.util.List;

@Slf4j
@Configuration
@EnableLightminBatch
public class ITJobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

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
        public void write(final List<? extends Long> list) throws Exception {
            for (final Long value : list) {
                log.info(String.valueOf(value));
            }
        }

    }

}
