package org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.configuration;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.domain.BatchTaskAddress;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.domain.ProcessingState;
import org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.persistence.dao.BatchTaskAddressDao;

import java.io.File;

@Configuration
public class AddressImportJobConfiguration {

    @Bean
    public Job addressImportJob(final Step addressImportStep,
                                final Step deleteFileStep,
                                final JobRepository jobRepository) {
        return new JobBuilder("addressImportJob", jobRepository)
                .start(addressImportStep)
                .next(deleteFileStep)
                .build();
    }

    @Bean
    public Step deleteFileStep(final Tasklet deleteFileTasklet,
                               final JobRepository jobRepository,
                               final PlatformTransactionManager transactionManager) {
        return new StepBuilder("deleteFileStep", jobRepository)
                .tasklet(deleteFileTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step addressImportStep(final FlatFileItemReader<BatchTaskAddress> fileItemReader,
                                  final ItemWriter<BatchTaskAddress> addressDatabaseWriter,
                                  final JobRepository jobRepository,
                                  final PlatformTransactionManager transactionManager) {
        return new StepBuilder("addressImportStep", jobRepository)
                .<BatchTaskAddress, BatchTaskAddress>chunk(1, transactionManager)
                .reader(fileItemReader)
                .writer(addressDatabaseWriter)
                .build();
    }

    @Bean
    @JobScope
    public FlatFileItemReader<BatchTaskAddress> fileItemReader(@Value("#{jobParameters['fileSource']}") final String pathToFile,
                                                               final LineMapper<BatchTaskAddress> lineMapper) throws
            Exception {
        final FlatFileItemReader<BatchTaskAddress> reader = new FlatFileItemReader<>(lineMapper);
        reader.setEncoding("utf-8");
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(pathToFile));
        return reader;
    }

    @Bean
    public ItemWriter<BatchTaskAddress> addressDatabaseWriter(final BatchTaskAddressDao batchTaskAddressDAO) {
        return items -> {
            for (final BatchTaskAddress item : items) {
                batchTaskAddressDAO.add(item);
            }
        };
    }

    @Bean
    public LineMapper<BatchTaskAddress> lineMapper() {
        final DefaultLineMapper<BatchTaskAddress> lineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(";");
        tokenizer.setNames(new String[]{"city", "zip_code", "street", "house_number"});
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
            final BatchTaskAddress batchTaskAddress = new BatchTaskAddress();
            batchTaskAddress.setCity(fieldSet.readString("city"));
            batchTaskAddress.setZipCode(fieldSet.readString("zip_code"));
            batchTaskAddress.setStreet(fieldSet.readString("street"));
            batchTaskAddress.setHouseNumber(fieldSet.readString("house_number"));
            batchTaskAddress.setProcessingState(ProcessingState.INIT);
            return batchTaskAddress;
        });
        lineMapper.afterPropertiesSet();
        return lineMapper;
    }

    @Bean
    @JobScope
    public Tasklet deleteFileTasklet(@Value("#{jobParameters['fileSource']}") final String pathToFile) {
        return (contribution, chunkContext) -> {
            final File file = new File(pathToFile);
            final boolean deleted = file.delete();
            final RepeatStatus repeatStatus;
            if (deleted) {
                repeatStatus = RepeatStatus.FINISHED;
            } else {
                repeatStatus = RepeatStatus.CONTINUABLE;
            }
            return repeatStatus;
        };
    }

}
