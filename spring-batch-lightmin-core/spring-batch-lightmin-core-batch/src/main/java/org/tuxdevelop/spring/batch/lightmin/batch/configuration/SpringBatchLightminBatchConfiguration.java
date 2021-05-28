package org.tuxdevelop.spring.batch.lightmin.batch.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.tuxdevelop.spring.batch.lightmin.batch.dao.JdbcLightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.batch.dao.LightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.batch.dao.MapLightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@EnableConfigurationProperties(value = {SpringBatchLightminBatchConfigurationProperties.class})
public class SpringBatchLightminBatchConfiguration {

    private final SpringBatchLightminBatchConfigurationProperties properties;
    private final ApplicationContext applicationContext;
    private final DataFieldMaxValueIncrementer incrementer = new AbstractDataFieldMaxValueIncrementer() {
        @Override
        protected long getNextKey() {
            throw new IllegalStateException("JobExplorer is read only.");
        }
    };

    @Autowired
    public SpringBatchLightminBatchConfiguration(final SpringBatchLightminBatchConfigurationProperties properties,
                                                 final ApplicationContext applicationContext) {
        this.properties = properties;
        this.applicationContext = applicationContext;
    }
    
    @Bean
    @ConditionalOnMissingBean(value = BatchConfigurer.class)
    public BatchConfigurer batchConfigurer(final ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        final BasicSpringBatchLightminBatchConfigurer batchConfigurer;
        final BatchRepositoryType batchRepositoryType = SpringBatchLightminBatchConfiguration.this.properties.getRepositoryType();
        switch (batchRepositoryType) {
            case JDBC:
                final DataSource dataSource = SpringBatchLightminBatchConfiguration.this.getDataSource();
                final String tablePrefix = SpringBatchLightminBatchConfiguration.this.properties.getTablePrefix();
                batchConfigurer = new BasicSpringBatchLightminBatchConfigurer(transactionManagerCustomizers.getIfAvailable(), dataSource, tablePrefix);
                break;
            case MAP:
                batchConfigurer = new BasicSpringBatchLightminBatchConfigurer(transactionManagerCustomizers.getIfAvailable());
                break;
            default:
                throw new SpringBatchLightminConfigurationException("Unknown BatchRepositoryType: " + batchRepositoryType);

        }
        return batchConfigurer;
    }

    @Bean
    @ConditionalOnMissingBean(value = {JobRepository.class})
    public JobRepository jobRepository(final BatchConfigurer batchConfigurer) throws Exception {
        return batchConfigurer.getJobRepository();
    }

    /*
     * TODO: check why it is needed
     */
    @Bean(name = "defaultAsyncJobLauncher")
    public JobLauncher defaultAsyncJobLauncher(final JobRepository jobRepository) {
        final SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return jobLauncher;
    }

    @Bean
    @ConditionalOnMissingBean(value = {JobLauncher.class})
    public JobExplorer jobExplorer(final BatchConfigurer batchConfigurer) throws Exception {
        return batchConfigurer.getJobExplorer();
    }

    @Bean
    @ConditionalOnMissingBean(value = {StepBuilderFactory.class})
    public StepBuilderFactory stepBuilderFactory(final BatchConfigurer batchConfigurer) throws Exception {
        return new StepBuilderFactory(batchConfigurer.getJobRepository(), batchConfigurer.getTransactionManager());
    }

    @Bean
    @ConditionalOnMissingBean(value = {JobBuilderFactory.class})
    public JobBuilderFactory jobBuilderFactory(final JobRepository jobRepository) {
        return new JobBuilderFactory(jobRepository);
    }

    @Bean
    public LightminJobExecutionDao lightminJobExecutionDao(final BatchConfigurer batchConfigurer) throws Exception {
        final BatchRepositoryType batchRepositoryType = this.properties.getRepositoryType();
        final LightminJobExecutionDao lightminJobExecutionDao;
        switch (batchRepositoryType) {
            case JDBC:
                lightminJobExecutionDao = this.createLightminJobExecutionDao();
                break;
            case MAP:
                lightminJobExecutionDao = new MapLightminJobExecutionDao(batchConfigurer.getJobExplorer());
                break;
            default:
                throw new SpringBatchLightminConfigurationException("Unknown BatchRepositoryType: " + batchRepositoryType);

        }
        return lightminJobExecutionDao;
    }

    @Bean
    @ConditionalOnMissingBean(value = {JobOperator.class})
    public JobOperator jobOperator(final JobExplorer jobExplorer,
                                   @Qualifier("jobLauncher") final JobLauncher jobLauncher,
                                   final JobRepository jobRepository,
                                   final JobRegistry jobRegistry) throws Exception {
        final SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobExplorer(jobExplorer);
        jobOperator.setJobLauncher(jobLauncher);
        jobOperator.setJobRepository(jobRepository);
        jobOperator.setJobRegistry(jobRegistry);
        jobOperator.afterPropertiesSet();
        return jobOperator;
    }

    @Bean
    @ConditionalOnMissingBean(value = {JobRegistry.class})
    public JobRegistry jobRegistry() {
        return new MapJobRegistry();
    }

    private LightminJobExecutionDao createLightminJobExecutionDao() throws Exception {
        final DataSource dataSource = this.getDataSource();
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        final JdbcLightminJobExecutionDao dao = new JdbcLightminJobExecutionDao(dataSource);
        dao.setJdbcTemplate(jdbcTemplate);
        dao.setJobExecutionIncrementer(this.incrementer);
        dao.setTablePrefix(this.properties.getTablePrefix());
        dao.afterPropertiesSet();
        return dao;
    }

    DataSource getDataSource() {
        return this.applicationContext.getBean(this.properties.getDataSourceName(), DataSource.class);
    }


}
