package org.tuxdevelop.spring.batch.lightmin.address_migrator.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.tuxdevelop.spring.batch.lightmin.address_migrator.domain.Address;
import org.tuxdevelop.spring.batch.lightmin.address_migrator.domain.ProcessingState;
import org.tuxdevelop.spring.batch.lightmin.address_migrator.persistence.dao.AddressDAO;
import org.tuxdevelop.spring.batch.lightmin.address_migrator.persistence.dao.BatchTaskAddressDAO;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Configuration
public class AddressPrinterJobConfiguration {

    private static final String SELECT_ADDRESS_QUERY = "SELECT batch_task_id FROM batch_task_address WHERE processing_state = " +
            ProcessingState.SUCCESS;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job addressPrinterJob() throws Exception {
        return jobBuilderFactory
                .get("addressPrinterJob")
                .start(addressPrinterStep())
                .build();
    }

    @Bean
    public Step addressPrinterStep() throws Exception {
        return stepBuilderFactory
                .get("addressPrinterStep")
                .<Long, Address>chunk(1)
                .reader(addressPrinterReader())
                .processor(addressPrinterProcessor())
                .writer(addressPrinterWriter())
                .build();
    }

    @Bean
    public ItemProcessor<Long, Address> addressPrinterProcessor() {
        return new ItemProcessor<Long, Address>() {

            @Autowired
            private BatchTaskAddressDAO batchTaskAddressDAO;

            @Autowired
            private AddressDAO addressDAO;

            @Override
            public Address process(Long id) throws Exception {
                batchTaskAddressDAO.updateProcessingState(id, ProcessingState.PRINTED);
                return addressDAO.getAddressById(id);
            }
        };
    }

    @Bean
    public ItemWriter<Address> addressPrinterWriter() {
        return new ItemWriter<Address>() {
            @Override
            public void write(List<? extends Address> addresses) throws Exception {
                for (final Address address : addresses) {
                    log.info("Migrated Address: <<<" + address + ">>>");
                }
            }
        };
    }

    @Bean
    public JdbcCursorItemReader<Long> addressPrinterReader() throws Exception {
        final JdbcCursorItemReader<Long> reader = new JdbcCursorItemReader<Long>();
        reader.setSql(SELECT_ADDRESS_QUERY);
        reader.setRowMapper(new SingleColumnRowMapper<Long>());
        reader.setDataSource(dataSource);
        reader.setMaxRows(100);
        reader.afterPropertiesSet();
        return reader;
    }
}
