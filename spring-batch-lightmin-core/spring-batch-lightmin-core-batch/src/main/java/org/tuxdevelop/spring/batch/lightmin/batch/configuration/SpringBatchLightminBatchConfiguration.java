package org.tuxdevelop.spring.batch.lightmin.batch.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.batch.core.repository.dao.jdbc.JdbcJobInstanceDao;
import org.springframework.transaction.PlatformTransactionManager;
import org.tuxdevelop.spring.batch.lightmin.batch.dao.JdbcLightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.batch.dao.LightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.batch.dao.MapLightminJobExecutionDao;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(SpringBatchLightminBatchConfigurationProperties.class)
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
    @ConditionalOnMissingBean
    public BasicSpringBatchLightminBatchConfigurer batchInfrastructure(final org.springframework.beans.factory.ObjectProvider<EntityManagerFactory> entityManagerFactory) {
        final EntityManagerFactory emf = entityManagerFactory.getIfAvailable();
        if (this.properties.getRepositoryType() == BatchRepositoryType.JDBC) {
            return emf == null
                    ? new BasicSpringBatchLightminBatchConfigurer(getDataSource(), this.properties.getTablePrefix())
                    : new JpaSpringBatchLightminBatchConfigurer(getDataSource(), this.properties.getTablePrefix(), emf);
        }
        return emf == null ? new BasicSpringBatchLightminBatchConfigurer() : new JpaSpringBatchLightminBatchConfigurer(emf);
    }

    @Bean
    @ConditionalOnMissingBean
    public JobRepository jobRepository(final BasicSpringBatchLightminBatchConfigurer infrastructure) {
        return infrastructure.getJobRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public JobExplorer jobExplorer(final BasicSpringBatchLightminBatchConfigurer infrastructure) {
        return infrastructure.getJobExplorer();
    }

    @Primary
    @Bean(name = "batchTransactionManager")
    @ConditionalOnMissingBean(name = "batchTransactionManager")
    public PlatformTransactionManager batchTransactionManager(final BasicSpringBatchLightminBatchConfigurer infrastructure) {
        return infrastructure.getTransactionManager();
    }

    @Primary
    @Bean(name = "jobLauncher")
    @ConditionalOnMissingBean(name = "jobLauncher")
    public JobLauncher jobLauncher(final BasicSpringBatchLightminBatchConfigurer infrastructure) {
        return infrastructure.getJobLauncher();
    }

    @Bean(name = "defaultAsyncJobLauncher")
    public JobLauncher defaultAsyncJobLauncher(final JobRepository jobRepository) throws Exception {
        final TaskExecutorJobLauncher launcher = new TaskExecutorJobLauncher();
        launcher.setJobRepository(jobRepository);
        launcher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        launcher.afterPropertiesSet();
        return launcher;
    }

    @Bean
    public LightminJobExecutionDao lightminJobExecutionDao(
            final BasicSpringBatchLightminBatchConfigurer infrastructure) throws Exception {
        if (this.properties.getRepositoryType() == BatchRepositoryType.JDBC) {
            return createLightminJobExecutionDao();
        }
        return new MapLightminJobExecutionDao(infrastructure.getJobExplorer());
    }

    @Bean
    @ConditionalOnMissingBean
    public JobOperator jobOperator(final JobExplorer jobExplorer,
                                   @Qualifier("jobLauncher") final JobLauncher jobLauncher,
                                   final JobRepository jobRepository,
                                   final JobRegistry jobRegistry) throws Exception {
        final SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobRepository(jobRepository);
        jobOperator.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobOperator.setJobRegistry(jobRegistry);
        jobOperator.afterPropertiesSet();
        return jobOperator;
    }

    @Bean
    @ConditionalOnMissingBean
    public JobRegistry jobRegistry() {
        return new MapJobRegistry();
    }

    private LightminJobExecutionDao createLightminJobExecutionDao() throws Exception {
        final DataSource dataSource = getDataSource();
        final JdbcLightminJobExecutionDao dao = new JdbcLightminJobExecutionDao(dataSource);
        dao.setJdbcTemplate(new JdbcTemplate(dataSource));
        dao.setJobExecutionIncrementer(this.incrementer);
        dao.setTablePrefix(this.properties.getTablePrefix());
        final JdbcJobInstanceDao jobInstanceDao = new JdbcJobInstanceDao();
        jobInstanceDao.setJdbcTemplate(new JdbcTemplate(dataSource));
        jobInstanceDao.setJobInstanceIncrementer(this.incrementer);
        jobInstanceDao.setTablePrefix(this.properties.getTablePrefix());
        jobInstanceDao.afterPropertiesSet();
        dao.setJobInstanceDao(jobInstanceDao);
        dao.afterPropertiesSet();
        return dao;
    }

    DataSource getDataSource() {
        return this.applicationContext.getBean(this.properties.getDataSourceName(), DataSource.class);
    }
}
