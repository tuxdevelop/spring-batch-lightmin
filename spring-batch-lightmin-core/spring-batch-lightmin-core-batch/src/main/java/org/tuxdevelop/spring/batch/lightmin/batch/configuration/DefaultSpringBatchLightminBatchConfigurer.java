package org.tuxdevelop.spring.batch.lightmin.batch.configuration;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.*;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @author Marcel Becker
 * @version 0.2
 */
@Slf4j
public class DefaultSpringBatchLightminBatchConfigurer implements BatchConfigurer {

    @Getter
    private JobInstanceDao jobInstanceDao;
    @Getter
    private JobExecutionDao jobExecutionDao;
    @Getter
    private StepExecutionDao stepExecutionDao;
    private JobRepository jobRepository;
    private JobLauncher jobLauncher;
    private JobExplorer jobExplorer;

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private PlatformTransactionManager transactionManager;

    private String tablePrefix = AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX;

    private final DataFieldMaxValueIncrementer incrementer = new AbstractDataFieldMaxValueIncrementer() {
        @Override
        protected long getNextKey() {
            throw new IllegalStateException("JobExplorer is read only.");
        }
    };

    public DefaultSpringBatchLightminBatchConfigurer() {
    }

    public DefaultSpringBatchLightminBatchConfigurer(final DataSource dataSource,
                                                     final String tablePrefix) {
        assert dataSource != null : "DataSource must not be null";
        assert StringUtils.hasText(tablePrefix) : "tablePrefix must not be null or empty";
        this.setDataSource(dataSource);
        this.tablePrefix = tablePrefix;
    }

    private void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
        this.transactionManager = new DataSourceTransactionManager(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public JobRepository getJobRepository() throws Exception {
        return this.jobRepository;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() throws Exception {
        return this.transactionManager;
    }

    @Override
    public JobLauncher getJobLauncher() throws Exception {
        return this.jobLauncher;
    }

    @Override
    public JobExplorer getJobExplorer() throws Exception {
        return this.jobExplorer;
    }

    @PostConstruct
    public void initialize() {
        try {
            if (this.dataSource != null) {
                this.createJdbcComponents();
            } else {
                this.createMapComponents();
            }
            this.jobLauncher = this.createJobLauncher();
        } catch (final Exception e) {
            log.error("Error while creating DefaultSpringBatchLightminConfiguration: " + e.getMessage());
            throw new SpringBatchLightminConfigurationException(e, e.getMessage());
        }
    }

    protected void createJdbcComponents() throws Exception {

        // jobExplorer
        final JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
        jobExplorerFactoryBean.setTablePrefix(this.tablePrefix);
        jobExplorerFactoryBean.setDataSource(this.dataSource);
        jobExplorerFactoryBean.afterPropertiesSet();
        this.jobExplorer = jobExplorerFactoryBean.getObject();

        // jobExecutionDao
        this.jobExecutionDao = this.createJobExecutionDao();
        // jobInstanceDao
        this.jobInstanceDao = this.createJobInstanceDao();
        // stepExecutionDao
        this.stepExecutionDao = this.createStepExecutionDao();
        // jobRepository
        this.jobRepository = this.createJobRepository();
    }

    protected void createMapComponents() throws Exception {
        if (this.transactionManager == null) {
            this.transactionManager = new ResourcelessTransactionManager();
        }
        // jobRepository
        final MapJobRepositoryFactoryBean jobRepositoryFactory = new MapJobRepositoryFactoryBean(
                this.transactionManager);
        jobRepositoryFactory.afterPropertiesSet();
        this.jobRepository = jobRepositoryFactory.getObject();
        // jobExplorer
        final MapJobExplorerFactoryBean jobExplorerFactory = new MapJobExplorerFactoryBean(
                jobRepositoryFactory);
        jobExplorerFactory.afterPropertiesSet();
        this.jobExplorer = jobExplorerFactory.getObject();
        // jobExecutionDao
        this.jobExecutionDao = new MapJobExecutionDao();
        // jobInstanceDao
        this.jobInstanceDao = new MapJobInstanceDao();
        // stepExecutionDao
        this.stepExecutionDao = new MapStepExecutionDao();
    }


    protected JobLauncher createJobLauncher() throws Exception {
        final SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(this.jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    protected JobRepository createJobRepository() throws Exception {
        final JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(this.dataSource);
        jobRepositoryFactoryBean.setTransactionManager(this.transactionManager);
        jobRepositoryFactoryBean.setTablePrefix(this.tablePrefix);
        jobRepositoryFactoryBean.afterPropertiesSet();
        return jobRepositoryFactoryBean.getObject();
    }

    protected JobInstanceDao createJobInstanceDao() throws Exception {
        final JdbcJobInstanceDao dao = new JdbcJobInstanceDao();
        dao.setJdbcTemplate(this.jdbcTemplate);
        dao.setJobIncrementer(this.incrementer);
        dao.setTablePrefix(this.tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

    protected JobExecutionDao createJobExecutionDao() throws Exception {
        final JdbcJobExecutionDao dao = new JdbcJobExecutionDao();
        dao.setJdbcTemplate(this.jdbcTemplate);
        dao.setJobExecutionIncrementer(this.incrementer);
        dao.setTablePrefix(this.tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

    protected StepExecutionDao createStepExecutionDao() throws Exception {
        final JdbcStepExecutionDao dao = new JdbcStepExecutionDao();
        dao.setJdbcTemplate(this.jdbcTemplate);
        dao.setStepExecutionIncrementer(this.incrementer);
        dao.setTablePrefix(this.tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

}
