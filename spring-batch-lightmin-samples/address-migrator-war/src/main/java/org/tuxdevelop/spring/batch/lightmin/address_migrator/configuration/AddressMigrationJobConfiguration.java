package org.tuxdevelop.spring.batch.lightmin.address_migrator.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.tuxdevelop.spring.batch.lightmin.address_migrator.domain.BatchTaskAddress;
import org.tuxdevelop.spring.batch.lightmin.address_migrator.domain.ProcessingState;
import org.tuxdevelop.spring.batch.lightmin.address_migrator.mapper.BatchTaskAddressMapper;
import org.tuxdevelop.spring.batch.lightmin.address_migrator.processor.AddressMigrationProcessor;

import javax.sql.DataSource;

@Configuration
public class AddressMigrationJobConfiguration {

    private static final String SELECT_ADDRESS_QUERY = "SELECT * FROM batch_task_address WHERE processing_state = " +
            ProcessingState.INIT;
    private static final String UPDATE_ADDRESS_STATEMENT =
            "UPDATE batch_task_address SET processing_state = :processing_state WHERE batch_task_id = :batch_task_id";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Bean
    public Job addressMigrationJob() throws Exception {
        return jobBuilderFactory.get("addressMigrationJob")
                .start(addressMigrationStep())
                .build();
    }

    @Bean
    public Step addressMigrationStep() throws Exception {
        return stepBuilderFactory.get("addressMigrationStep")
                .<BatchTaskAddress, BatchTaskAddress>chunk(1)
                .reader(addressReader())
                .processor(addressMigrationProcessor())
                .writer(addressWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<BatchTaskAddress> addressReader() throws Exception {
        final JdbcCursorItemReader<BatchTaskAddress> reader = new JdbcCursorItemReader<BatchTaskAddress>();
        reader.setSql(SELECT_ADDRESS_QUERY);
        reader.setRowMapper(new BatchTaskAddressMapper());
        reader.setDataSource(dataSource);
        reader.setMaxRows(100);
        reader.afterPropertiesSet();
        return reader;
    }

    @Bean
    public AddressMigrationProcessor addressMigrationProcessor() {
        return new AddressMigrationProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<BatchTaskAddress> addressWriter() {
        final JdbcBatchItemWriter<BatchTaskAddress> writer = new JdbcBatchItemWriter<BatchTaskAddress>();
        writer.setDataSource(dataSource);
        writer.setSql(UPDATE_ADDRESS_STATEMENT);
        writer.setItemSqlParameterSourceProvider(new ItemSqlParameterSourceProvider<BatchTaskAddress>() {
            @Override
            public SqlParameterSource createSqlParameterSource(final BatchTaskAddress item) {
                final MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
                sqlParameterSource.addValue("processing_state", item.getProcessingState());
                sqlParameterSource.addValue("batch_task_id", item.getBatchTaskId());
                return sqlParameterSource;
            }
        });
        writer.afterPropertiesSet();
        return writer;
    }
}
