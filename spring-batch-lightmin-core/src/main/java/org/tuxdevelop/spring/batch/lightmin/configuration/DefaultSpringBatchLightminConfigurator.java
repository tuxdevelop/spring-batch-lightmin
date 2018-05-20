package org.tuxdevelop.spring.batch.lightmin.configuration;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.*;
import org.tuxdevelop.spring.batch.lightmin.dao.JdbcLightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.dao.LightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.dao.MapLightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.service.DefaultJobService;
import org.tuxdevelop.spring.batch.lightmin.service.DefaultStepService;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;
import org.tuxdevelop.spring.batch.lightmin.service.StepService;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Slf4j
public class DefaultSpringBatchLightminConfigurator implements SpringBatchLightminConfigurator, InitializingBean {

    @Setter
    private BatchConfigurer batchConfigurer;
    private JobService jobService;
    private StepService stepService;
    private JobOperator jobOperator;
    private JobRegistry jobRegistry;
    private LightminJobExecutionDao lightminJobExecutionDao;
    private JobConfigurationRepository jobConfigurationRepository;
    private final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;
    private DataSource batchDataSource;
    private JdbcTemplate jdbcTemplate;
    private JdbcTemplate batchJdbcTemplate;
    private final String repositoryTablePrefix;
    private final BatchRepositoryType batchRepositoryType;
    private final LightminRepositoryType lightminRepositoryType;
    private final ApplicationContext applicationContext;

    private final DataFieldMaxValueIncrementer incrementer = new AbstractDataFieldMaxValueIncrementer() {
        @Override
        protected long getNextKey() {
            throw new IllegalStateException("JobExplorer is read only.");
        }
    };

    public DefaultSpringBatchLightminConfigurator(final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties,
                                                  final ApplicationContext applicationContext) {
        this.repositoryTablePrefix = springBatchLightminConfigurationProperties.getRepositoryTablePrefix();
        this.springBatchLightminConfigurationProperties = springBatchLightminConfigurationProperties;
        this.lightminRepositoryType = springBatchLightminConfigurationProperties.getLightminRepositoryType();
        this.batchRepositoryType = springBatchLightminConfigurationProperties.getBatchRepositoryType();
        this.applicationContext = applicationContext;
    }

    private void setDataSource(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private void setBatchDataSource(final DataSource dataSource) {
        this.batchDataSource = dataSource;
        this.batchJdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public JobService getJobService() {
        return this.jobService;
    }

    @Override
    public StepService getStepService() {
        return this.stepService;
    }

    @Override
    public JobOperator getJobOperator() {
        return this.jobOperator;
    }

    @Override
    public JobRegistry getJobRegistry() {
        return this.jobRegistry;
    }

    @Override
    public LightminJobExecutionDao getLightminJobExecutionDao() {
        return this.lightminJobExecutionDao;
    }

    @Override
    public String getRepositoryTablePrefix() {
        return this.repositoryTablePrefix;
    }

    @Override
    public JobConfigurationRepository getJobConfigurationRepository() {
        return this.jobConfigurationRepository;
    }

    @Override
    public void afterPropertiesSet() {
        assert this.batchConfigurer != null;
    }


    @PostConstruct
    public void initialize() {
        try {
            switch (this.lightminRepositoryType) {
                case JDBC:
                    final DataSource dataSource = this.applicationContext.getBean(this.springBatchLightminConfigurationProperties.getDataSourceName(), DataSource.class);
                    this.setDataSource(dataSource);
                    this.createJdbcJobConfigurationRepository();
                    break;
                case MAP:
                    this.createMapJobConfigurationRepository();
                    break;
                case REMOTE:
                    this.createRemoteJobConfigurationRepository();
                    break;
                default:
                    throw new SpringBatchLightminConfigurationException("Unknown LightminRepositoryType: " + this.lightminRepositoryType);

            }
            switch (this.batchRepositoryType) {
                case JDBC:
                    final DataSource dataSource = this.applicationContext.getBean(this.springBatchLightminConfigurationProperties.getBatchDataSourceName(), DataSource.class);
                    this.setBatchDataSource(dataSource);
                    this.createLightminJdbcJobExecutionDao();
                    break;
                case MAP:
                    this.createLightminMapJobExecutionDao();
                    break;
                default:
                    throw new SpringBatchLightminConfigurationException("Unknown BatchRepositoryType: " + this.batchRepositoryType);
            }

            this.jobRegistry = this.createJobRegistry();
            this.jobOperator = this.createJobOperator();
            this.jobService = this.createJobService();
            this.stepService = this.createStepService();
        } catch (final Exception e) {
            log.error("Error while creating DefaultSpringBatchLightminConfiguration: {} ", e.getMessage());
            throw new SpringBatchLightminConfigurationException(e, e.getMessage());
        }
    }

    protected void createLightminJdbcJobExecutionDao() throws Exception {
        this.lightminJobExecutionDao = this.createLightminJobExecutionDao();
    }

    protected void createLightminMapJobExecutionDao() throws Exception {
        this.lightminJobExecutionDao = new MapLightminJobExecutionDao(this.batchConfigurer.getJobExplorer());
    }

    protected void createMapJobConfigurationRepository() {
        this.jobConfigurationRepository = new MapJobConfigurationRepository();
    }

    protected void createJdbcJobConfigurationRepository() {
        this.jobConfigurationRepository = new JdbcJobConfigurationRepository(this.jdbcTemplate, this.springBatchLightminConfigurationProperties);
    }

    protected void createRemoteJobConfigurationRepository() {
        final RemoteJobConfigurationRepositoryLocator remoteJobConfigurationRepositoryLocator =
                this.applicationContext.getBean(RemoteJobConfigurationRepositoryLocator.class);
        this.jobConfigurationRepository = new RemoteJobConfigurationRepository(
                this.springBatchLightminConfigurationProperties,
                remoteJobConfigurationRepositoryLocator);
    }

    protected LightminJobExecutionDao createLightminJobExecutionDao() throws Exception {
        final JdbcLightminJobExecutionDao dao = new JdbcLightminJobExecutionDao(this.batchDataSource);
        dao.setJdbcTemplate(this.batchJdbcTemplate);
        dao.setJobExecutionIncrementer(this.incrementer);
        dao.setTablePrefix(this.repositoryTablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

    protected JobOperator createJobOperator() throws Exception {
        final SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobExplorer(this.batchConfigurer.getJobExplorer());
        jobOperator.setJobLauncher(this.batchConfigurer.getJobLauncher());
        jobOperator.setJobRepository(this.batchConfigurer.getJobRepository());
        jobOperator.setJobRegistry(this.jobRegistry);
        jobOperator.afterPropertiesSet();
        return jobOperator;
    }

    protected JobRegistry createJobRegistry() {
        return new MapJobRegistry();
    }

    protected JobService createJobService() throws Exception {
        final JobService jobService = new DefaultJobService(
                this.jobOperator,
                this.jobRegistry,
                this.batchConfigurer.getJobExplorer(),
                this.lightminJobExecutionDao);
        jobService.afterPropertiesSet();
        return jobService;
    }

    protected StepService createStepService() throws Exception {
        final StepService stepService = new DefaultStepService(this.batchConfigurer.getJobExplorer());
        stepService.afterPropertiesSet();
        return stepService;
    }
}
