package org.tuxdevelop.spring.batch.lightmin.server;

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

import java.util.List;

@Slf4j
@Configuration
public class ITJobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory
                .get("simpleJob")
                .start(simpleStep())
                .build();
    }

    @Bean
    public Step simpleStep() {
        return stepBuilderFactory
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
            final Long value = index >= values.length ? null : values[index];
            index++;
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
