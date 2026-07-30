package org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.configuration;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.transaction.PlatformTransactionManager;
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
    public Job addressMigrationJob(final JobRepository jobRepository,
                                   final Step addressMigrationStep) throws Exception {
        return new JobBuilder("addressMigrationJob", jobRepository)
                .start(addressMigrationStep)
                .build();
    }

    @Bean
    public Step addressMigrationStep(final JobRepository jobRepository,
                                     final PlatformTransactionManager transactionManager,
                                     final ItemStreamReader<BatchTaskAddress> addressReader,
                                     final ItemProcessor<BatchTaskAddress, BatchTaskAddress> addressMigrationProcessor,
                                     final ItemWriter<BatchTaskAddress> addressWriter) throws Exception {
        return new StepBuilder("addressMigrationStep", jobRepository)
                .<BatchTaskAddress, BatchTaskAddress>chunk(1, transactionManager)
                .reader(addressReader)
                .processor(addressMigrationProcessor)
                .writer(addressWriter)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<BatchTaskAddress> addressReader(final DataSource dataSource,
                                                                final BatchTaskAddressMapper batchTaskAddressMapper) throws Exception {
        final JdbcCursorItemReader<BatchTaskAddress> reader = new JdbcCursorItemReader<>(dataSource, SELECT_ADDRESS_QUERY, batchTaskAddressMapper);
        reader.setMaxRows(100);
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
