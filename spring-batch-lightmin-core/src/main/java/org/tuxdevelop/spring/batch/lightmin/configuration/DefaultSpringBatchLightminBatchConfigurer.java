package org.tuxdevelop.spring.batch.lightmin.configuration;


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
        setDataSource(dataSource);
        this.tablePrefix = tablePrefix;
    }

    private void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
        this.transactionManager = new DataSourceTransactionManager(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public JobRepository getJobRepository() throws Exception {
        return jobRepository;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() throws Exception {
        return transactionManager;
    }

    @Override
    public JobLauncher getJobLauncher() throws Exception {
        return jobLauncher;
    }

    @Override
    public JobExplorer getJobExplorer() throws Exception {
        return jobExplorer;
    }

    @PostConstruct
    public void initialize() {
        try {
            if (this.dataSource != null) {
                createJdbcComponents();
            } else {
                createMapComponents();
            }
            this.jobLauncher = createJobLauncher();
        } catch (final Exception e) {
            log.error("Error while creating DefaultSpringBatchLightminConfiguration: " + e.getMessage());
            throw new SpringBatchLightminConfigurationException(e, e.getMessage());
        }
    }

    protected void createJdbcComponents() throws Exception {

        // jobExplorer
        final JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
        jobExplorerFactoryBean.setTablePrefix(tablePrefix);
        jobExplorerFactoryBean.setDataSource(this.dataSource);
        jobExplorerFactoryBean.afterPropertiesSet();
        this.jobExplorer = jobExplorerFactoryBean.getObject();

        // jobExecutionDao
        this.jobExecutionDao = createJobExecutionDao();
        // jobInstanceDao
        this.jobInstanceDao = createJobInstanceDao();
        // stepExecutionDao
        this.stepExecutionDao = createStepExecutionDao();
        // jobRepository
        this.jobRepository = createJobRepository();
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
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    protected JobRepository createJobRepository() throws Exception {
        final JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(dataSource);
        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
        jobRepositoryFactoryBean.setTablePrefix(tablePrefix);
        jobRepositoryFactoryBean.afterPropertiesSet();
        return jobRepositoryFactoryBean.getObject();
    }

    protected JobInstanceDao createJobInstanceDao() throws Exception {
        final JdbcJobInstanceDao dao = new JdbcJobInstanceDao();
        dao.setJdbcTemplate(jdbcTemplate);
        dao.setJobIncrementer(incrementer);
        dao.setTablePrefix(tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

    protected JobExecutionDao createJobExecutionDao() throws Exception {
        final JdbcJobExecutionDao dao = new JdbcJobExecutionDao();
        dao.setJdbcTemplate(jdbcTemplate);
        dao.setJobExecutionIncrementer(incrementer);
        dao.setTablePrefix(tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

    protected StepExecutionDao createStepExecutionDao() throws Exception {
        final JdbcStepExecutionDao dao = new JdbcStepExecutionDao();
        dao.setJdbcTemplate(jdbcTemplate);
        dao.setStepExecutionIncrementer(incrementer);
        dao.setTablePrefix(tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

}
