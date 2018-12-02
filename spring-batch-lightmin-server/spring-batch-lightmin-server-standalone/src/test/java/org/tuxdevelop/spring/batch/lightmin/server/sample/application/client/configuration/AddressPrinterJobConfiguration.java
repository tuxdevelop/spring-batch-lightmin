package org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.domain.Address;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.domain.ProcessingState;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.persistence.dao.AddressDao;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.persistence.dao.BatchTaskAddressDao;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class AddressPrinterJobConfiguration {

    private static final String SELECT_ADDRESS_QUERY =
            "SELECT batch_task_id FROM batch_task_address " +
                    "WHERE processing_state = " + ProcessingState.SUCCESS;

    private static final String SELECT_BATCH_TASK_ADDRESS_TO_DELETE_QUERY =
            "SELECT batch_task_id FROM batch_task_address " +
                    "WHERE processing_state = " + ProcessingState.PRINTED;

    private static final String DELETE_BATCH_TASK_ADDRESS_STATEMENT =
            "DELETE FROM batch_task_address " +
                    "WHERE batch_task_id = :batch_task_id";

    @Bean
    public Job addressPrinterJob(final JobBuilderFactory jobBuilderFactory,
                                 final Step addressPrinterStep,
                                 final Step addressBatchTaskDeletionStep) throws Exception {
        return jobBuilderFactory
                .get("addressPrinterJob")
                .incrementer(new RunIdIncrementer())
                .start(addressPrinterStep)
                .next(addressBatchTaskDeletionStep)
                .build();
    }

    @Bean
    public Step addressPrinterStep(final StepBuilderFactory stepBuilderFactory,
                                   final ItemStreamReader<Long> addressPrinterReader,
                                   final ItemProcessor<Long, Address> addressPrinterProcessor,
                                   final ItemWriter<Address> addressPrinterWriter) throws Exception {
        return stepBuilderFactory
                .get("addressPrinterStep")
                .<Long, Address>chunk(1)
                .reader(addressPrinterReader)
                .processor(addressPrinterProcessor)
                .writer(addressPrinterWriter)
                .allowStartIfComplete(Boolean.TRUE)
                .build();
    }

    @Bean
    public Step addressBatchTaskDeletionStep(final StepBuilderFactory stepBuilderFactory,
                                             final ItemStreamReader<Long> addressBatchTaskDeletionReader,
                                             final ItemWriter<Long> addressBatchTaskDeletionWriter) throws Exception {
        return stepBuilderFactory
                .get("addressBatchTaskDeletionStep")
                .<Long, Long>chunk(1)
                .reader(addressBatchTaskDeletionReader)
                .writer(addressBatchTaskDeletionWriter)
                .allowStartIfComplete(Boolean.TRUE)
                .build();
    }


    @Bean
    public ItemProcessor<Long, Address> addressPrinterProcessor() {
        return new ItemProcessor<Long, Address>() {

            @Autowired
            private BatchTaskAddressDao batchTaskAddressDAO;

            @Autowired
            private AddressDao addressDAO;

            @Override
            public Address process(final Long id) throws Exception {
                this.batchTaskAddressDAO.updateProcessingState(id, ProcessingState.PRINTED);
                return this.addressDAO.getAddressById(id);
            }
        };
    }

    @Bean
    public ItemWriter<Address> addressPrinterWriter() {
        return addresses -> {
            for (final Address address : addresses) {
                log.info("Migrated Address: <<<" + address + ">>>");
            }
        };
    }

    @Bean
    public JdbcBatchItemWriter<Long> addressBatchTaskDeletionWriter(final DataSource dataSource) {
        final JdbcBatchItemWriter<Long> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql(DELETE_BATCH_TASK_ADDRESS_STATEMENT);
        writer.setItemSqlParameterSourceProvider(batchTaskId -> {
            final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            mapSqlParameterSource.addValue("batch_task_id", batchTaskId);
            return mapSqlParameterSource;
        });
        return writer;
    }

    @Bean
    public JdbcCursorItemReader<Long> addressPrinterReader(final DataSource dataSource) throws Exception {
        final JdbcCursorItemReader<Long> reader = new JdbcCursorItemReader<>();
        reader.setSql(SELECT_ADDRESS_QUERY);
        reader.setRowMapper(new SingleColumnRowMapper<>());
        reader.setDataSource(dataSource);
        reader.setMaxRows(100);
        reader.afterPropertiesSet();
        return reader;
    }

    @Bean
    public JdbcCursorItemReader<Long> addressBatchTaskDeletionReader(final DataSource dataSource) throws Exception {
        final JdbcCursorItemReader<Long> reader = new JdbcCursorItemReader<>();
        reader.setSql(SELECT_BATCH_TASK_ADDRESS_TO_DELETE_QUERY);
        reader.setRowMapper(new SingleColumnRowMapper<>());
        reader.setDataSource(dataSource);
        reader.setMaxRows(50);
        reader.afterPropertiesSet();
        return reader;
    }
}
