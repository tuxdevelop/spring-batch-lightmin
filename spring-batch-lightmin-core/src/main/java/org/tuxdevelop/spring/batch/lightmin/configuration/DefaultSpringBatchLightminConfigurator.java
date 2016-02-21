package org.tuxdevelop.spring.batch.lightmin.configuration;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
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
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private String tablePrefix = AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX;

    private final DataFieldMaxValueIncrementer incrementer = new AbstractDataFieldMaxValueIncrementer() {
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
    public LightminJobExecutionDao getLightminJobExecutionDao() {
        return lightminJobExecutionDao;
    }

    @Override
    public String getTablePrefix() {
        return tablePrefix;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        assert batchConfigurer != null;
    }


    @PostConstruct
    public void initialize() {
        try {
            if (this.dataSource != null) {
                createJdbcComponents();
            } else {
                createMapComponents();
            }
            this.jobRegistry = createJobRegistry();
            this.jobOperator = createJobOperator();
            this.jobService = createJobService();
            this.stepService = createStepService();
        } catch (final Exception e) {
            log.error("Error while creating DefaultSpringBatchLightminConfiguration: "
                    + e.getMessage());
            throw new SpringBatchLightminConfigurationException(e,
                    e.getMessage());
        }
    }

    protected void createJdbcComponents() throws Exception {
        this.lightminJobExecutionDao = createLightminJobExecutionDao();
    }

    protected void createMapComponents() throws Exception {
        this.lightminJobExecutionDao = new MapLightminJobExecutionDao(batchConfigurer.getJobExplorer());
    }

    protected LightminJobExecutionDao createLightminJobExecutionDao() throws Exception {
        final JdbcLightminJobExecutionDao dao = new JdbcLightminJobExecutionDao();
        dao.setJdbcTemplate(jdbcTemplate);
        dao.setJobExecutionIncrementer(incrementer);
        dao.setTablePrefix(tablePrefix);
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
