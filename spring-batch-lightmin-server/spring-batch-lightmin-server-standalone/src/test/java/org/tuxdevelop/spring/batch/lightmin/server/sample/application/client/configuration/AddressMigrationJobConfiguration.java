package org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.domain.BatchTaskAddress;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.domain.ProcessingState;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.mapper.BatchTaskAddressMapper;

import javax.sql.DataSource;

@Configuration
public class AddressMigrationJobConfiguration {

    private static final String SELECT_ADDRESS_QUERY = "SELECT * FROM batch_task_address WHERE processing_state = " +
            ProcessingState.INIT;
    private static final String UPDATE_ADDRESS_STATEMENT =
            "UPDATE batch_task_address SET processing_state = :processing_state WHERE batch_task_id = :batch_task_id";

    @Bean
    public Job addressMigrationJob(final JobBuilderFactory jobBuilderFactory,
                                   final Step addressMigrationStep) throws Exception {
        return jobBuilderFactory.get("addressMigrationJob")
                .start(addressMigrationStep)
                .build();
    }

    @Bean
    public Step addressMigrationStep(final StepBuilderFactory stepBuilderFactory,
                                     final ItemStreamReader<BatchTaskAddress> addressReader,
                                     final ItemProcessor<BatchTaskAddress, BatchTaskAddress> addressMigrationProcessor,
                                     final ItemWriter<BatchTaskAddress> addressWriter) throws Exception {
        return stepBuilderFactory.get("addressMigrationStep")
                .<BatchTaskAddress, BatchTaskAddress>chunk(1)
                .reader(addressReader)
                .processor(addressMigrationProcessor)
                .writer(addressWriter)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<BatchTaskAddress> addressReader(final DataSource dataSource,
                                                                final BatchTaskAddressMapper batchTaskAddressMapper) throws Exception {
        final JdbcCursorItemReader<BatchTaskAddress> reader = new JdbcCursorItemReader<>();
        reader.setSql(SELECT_ADDRESS_QUERY);
        reader.setRowMapper(batchTaskAddressMapper);
        reader.setDataSource(dataSource);
        reader.setMaxRows(100);
        reader.afterPropertiesSet();
        return reader;
    }

    @Bean
    public JdbcBatchItemWriter<BatchTaskAddress> addressWriter(final DataSource dataSource) {
        final JdbcBatchItemWriter<BatchTaskAddress> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql(UPDATE_ADDRESS_STATEMENT);
        writer.setItemSqlParameterSourceProvider(item -> {
            final MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
            sqlParameterSource.addValue("processing_state", item.getProcessingState());
            sqlParameterSource.addValue("batch_task_id", item.getBatchTaskId());
            return sqlParameterSource;
        });
        writer.afterPropertiesSet();
        return writer;
    }
}
