package org.tuxdevelop.spring.batch.lightmin.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
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
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;
import org.tuxdevelop.spring.batch.lightmin.service.JobServiceBean;
import org.tuxdevelop.spring.batch.lightmin.service.StepService;
import org.tuxdevelop.spring.batch.lightmin.service.StepServiceBean;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Slf4j
public class DefaultSpringBatchLightminConfigurator implements SpringBatchLightminConfiguration {

    private JobService jobService;
    private StepService stepService;
    private JobOperator jobOperator;
    private JobRegistry jobRegistry;
    private JobExecutionDao jobExecutionDao;
    private JobInstanceDao jobInstanceDao;
    private StepExecutionDao stepExecutionDao;
    private PlatformTransactionManager transactionManager;
    private JobRepository jobRepository;
    private JobLauncher jobLauncher;
    private JobExplorer jobExplorer;
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private String tablePrefix = AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX;

    private DataFieldMaxValueIncrementer incrementer = new AbstractDataFieldMaxValueIncrementer() {
        @Override
        protected long getNextKey() {
            throw new IllegalStateException("JobExplorer is read only.");
        }
    };

    public DefaultSpringBatchLightminConfigurator() {
    }

    public DefaultSpringBatchLightminConfigurator(final String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public DefaultSpringBatchLightminConfigurator(final DataSource dataSource) {
        setDataSource(dataSource);
    }

    public DefaultSpringBatchLightminConfigurator(final DataSource dataSource,
                                                  final String tablePrefix) {
        setDataSource(dataSource);
        this.tablePrefix = tablePrefix;
    }

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
        this.transactionManager = new DataSourceTransactionManager(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public JobService getJobService() {
        return jobService;
    }

    @Override
    public StepService getStepService() {
        return stepService;
    }

    @Override
    public JobOperator getJobOperator() {
        return jobOperator;
    }

    @Override
    public JobRegistry getJobRegistry() {
        return jobRegistry;
    }

    @Override
    public JobExecutionDao getJobExecutionDao() {
        return jobExecutionDao;
    }

    @Override
    public JobInstanceDao getJobInstanceDao() {
        return jobInstanceDao;
    }

    @Override
    public StepExecutionDao getStepExecutionDao() {
        return stepExecutionDao;
    }

    @Override
    public JobRepository getJobRepository() {
        return jobRepository;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    @Override
    public JobLauncher getJobLauncher() {
        return jobLauncher;
    }

    @Override
    public JobExplorer getJobExplorer() {
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
            this.jobRegistry = createJobRegistry();
            this.jobOperator = createJobOperator();
            this.jobService = createJobService();
            this.stepService = createStepService();
        } catch (Exception e) {
            log.error("Error while creating DefaultSpringBatchLightminConfiguration: "
                    + e.getMessage());
            throw new SpringBatchLightminConfigurationException(e,
                    e.getMessage());
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
        JdbcJobInstanceDao dao = new JdbcJobInstanceDao();
        dao.setJdbcTemplate(jdbcTemplate);
        dao.setJobIncrementer(incrementer);
        dao.setTablePrefix(tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

    protected JobExecutionDao createJobExecutionDao() throws Exception {
        JdbcJobExecutionDao dao = new JdbcJobExecutionDao();
        dao.setJdbcTemplate(jdbcTemplate);
        dao.setJobExecutionIncrementer(incrementer);
        dao.setTablePrefix(tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

    protected StepExecutionDao createStepExecutionDao() throws Exception {
        JdbcStepExecutionDao dao = new JdbcStepExecutionDao();
        dao.setJdbcTemplate(jdbcTemplate);
        dao.setStepExecutionIncrementer(incrementer);
        dao.setTablePrefix(tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

    protected JobOperator createJobOperator() throws Exception {
        final SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobExplorer(jobExplorer);
        jobOperator.setJobLauncher(jobLauncher);
        jobOperator.setJobRepository(jobRepository);
        jobOperator.setJobRegistry(jobRegistry);
        jobOperator.afterPropertiesSet();
        return jobOperator;
    }

    protected JobRegistry createJobRegistry() {
        return new MapJobRegistry();
    }

    protected JobService createJobService() throws Exception {
        final JobService jobService = new JobServiceBean(jobOperator,
                jobRegistry, jobInstanceDao, jobExecutionDao);
        jobService.afterPropertiesSet();
        return jobService;
    }

    protected StepService createStepService() throws Exception {
        final StepService stepService = new StepServiceBean(stepExecutionDao);
        stepService.afterPropertiesSet();
        return stepService;
    }

}
