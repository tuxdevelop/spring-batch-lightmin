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
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JdbcJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.MapJobConfigurationRepository;
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
    private DataSource dataSource;
    private DataSource batchDataSource;
    private JdbcTemplate jdbcTemplate;
    private JdbcTemplate batchJdbcTemplate;
    private final String repositoryTablePrefix;
    private final String configurationTablePrefix;
    private BatchRepositoryType batchRepositoryType;
    private LightminRepositoryType lightminRepositoryType;
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
        this.configurationTablePrefix = springBatchLightminConfigurationProperties.getConfigurationTablePrefix();
        this.springBatchLightminConfigurationProperties = springBatchLightminConfigurationProperties;
        this.lightminRepositoryType = springBatchLightminConfigurationProperties.getLightminRepositoryType();
        this.batchRepositoryType = springBatchLightminConfigurationProperties.getBatchRepositoryType();
        this.applicationContext = applicationContext;
    }

    private void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private void setBatchDataSource(final DataSource dataSource) {
        this.batchDataSource = dataSource;
        this.batchJdbcTemplate = new JdbcTemplate(dataSource);
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
    public LightminJobExecutionDao getLightminJobExecutionDao() {
        return lightminJobExecutionDao;
    }

    public String getRepositoryTablePrefix() {
        return repositoryTablePrefix;
    }

    @Override
    public JobConfigurationRepository getJobConfigurationRepository() {
        return jobConfigurationRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        assert batchConfigurer != null;
    }


    @PostConstruct
    public void initialize() {
        try {
            switch (lightminRepositoryType) {
                case JDBC:
                    final DataSource dataSource = applicationContext.getBean(springBatchLightminConfigurationProperties.getDataSourceName(), DataSource.class);
                    setDataSource(dataSource);
                    createJdbcJobConfigurationRepository();
                    break;
                case MAP:
                    createMapJobConfigurationRepository();
                    break;
                default:
                    throw new SpringBatchLightminConfigurationException("Unknown LightminRepositoryType: " + lightminRepositoryType);

            }
            switch (batchRepositoryType) {
                case JDBC:
                    final DataSource dataSource = applicationContext.getBean(springBatchLightminConfigurationProperties.getBatchDataSourceName(), DataSource.class);
                    setBatchDataSource(dataSource);
                    createLightminJdbcJobExecutionDao();
                    break;
                case MAP:
                    createLightminMapJobExecutionDao();
                    break;
                default:
                    throw new SpringBatchLightminConfigurationException("Unknown BatchRepositoryType: " + batchRepositoryType);
            }

            this.jobRegistry = createJobRegistry();
            this.jobOperator = createJobOperator();
            this.jobService = createJobService();
            this.stepService = createStepService();
        } catch (final Exception e) {
            log.error("Error while creating DefaultSpringBatchLightminConfiguration: {} ", e.getMessage());
            throw new SpringBatchLightminConfigurationException(e, e.getMessage());
        }
    }

    protected void createLightminJdbcJobExecutionDao() throws Exception {
        this.lightminJobExecutionDao = createLightminJobExecutionDao();
    }

    protected void createLightminMapJobExecutionDao() throws Exception {
        this.lightminJobExecutionDao = new MapLightminJobExecutionDao(batchConfigurer.getJobExplorer());
    }

    protected void createMapJobConfigurationRepository() {
        this.jobConfigurationRepository = new MapJobConfigurationRepository();
    }

    protected void createJdbcJobConfigurationRepository() {
        this.jobConfigurationRepository = new JdbcJobConfigurationRepository(jdbcTemplate, configurationTablePrefix);
    }

    protected LightminJobExecutionDao createLightminJobExecutionDao() throws Exception {
        final JdbcLightminJobExecutionDao dao = new JdbcLightminJobExecutionDao(batchDataSource);
        dao.setJdbcTemplate(batchJdbcTemplate);
        dao.setJobExecutionIncrementer(incrementer);
        dao.setTablePrefix(repositoryTablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

    protected JobOperator createJobOperator() throws Exception {
        final SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobExplorer(batchConfigurer.getJobExplorer());
        jobOperator.setJobLauncher(batchConfigurer.getJobLauncher());
        jobOperator.setJobRepository(batchConfigurer.getJobRepository());
        jobOperator.setJobRegistry(jobRegistry);
        jobOperator.afterPropertiesSet();
        return jobOperator;
    }

    protected JobRegistry createJobRegistry() {
        return new MapJobRegistry();
    }

    protected JobService createJobService() throws Exception {
        final JobService jobService = new DefaultJobService(
                jobOperator,
                jobRegistry,
                batchConfigurer.getJobExplorer(),
                lightminJobExecutionDao);
        jobService.afterPropertiesSet();
        return jobService;
    }

    protected StepService createStepService() throws Exception {
        final StepService stepService = new DefaultStepService(batchConfigurer.getJobExplorer());
        stepService.afterPropertiesSet();
        return stepService;
    }
}
