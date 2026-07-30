package org.tuxdevelop.spring.batch.lightmin.batch.configuration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.core.repository.dao.jdbc.JdbcJobExecutionDao;
import org.springframework.batch.core.repository.dao.jdbc.JdbcJobInstanceDao;
import org.springframework.batch.core.repository.dao.jdbc.JdbcStepExecutionDao;
import org.springframework.batch.core.repository.explore.JobExplorer;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.infrastructure.support.transaction.ResourcelessTransactionManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.transaction.PlatformTransactionManager;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import javax.sql.DataSource;

/**
 * Creates the Spring Batch infrastructure used by Lightmin.
 *
 * <p>Spring Batch 6 no longer provides map-backed repository and explorer
 * factory beans.  The MAP option therefore uses a private, in-memory H2
 * database with the standard Batch schema.  It preserves the ephemeral,
 * process-local behaviour while using the supported JDBC infrastructure.</p>
 */
@Slf4j
public class BasicSpringBatchLightminBatchConfigurer {

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
    private String tablePrefix;

    private final DataFieldMaxValueIncrementer incrementer = new AbstractDataFieldMaxValueIncrementer() {
        @Override
        protected long getNextKey() {
            throw new IllegalStateException("JobExplorer is read only.");
        }
    };

    public BasicSpringBatchLightminBatchConfigurer() {
        this(null, "BATCH_");
    }

    public BasicSpringBatchLightminBatchConfigurer(final DataSource dataSource, final String tablePrefix) {
        this.dataSource = dataSource;
        this.tablePrefix = tablePrefix;
    }

    public JobRepository getJobRepository() {
        initialize();
        return this.jobRepository;
    }

    public PlatformTransactionManager getTransactionManager() {
        initialize();
        return this.transactionManager;
    }

    public JobLauncher getJobLauncher() {
        initialize();
        return this.jobLauncher;
    }

    public JobExplorer getJobExplorer() {
        initialize();
        return this.jobExplorer;
    }

    public synchronized void initialize() {
        if (this.jobRepository != null) {
            return;
        }
        try {
            if (this.dataSource == null) {
                this.dataSource = createInMemoryDataSource();
                this.tablePrefix = "BATCH_";
            }
            this.jdbcTemplate = new JdbcTemplate(this.dataSource);
            this.transactionManager = createTransactionManager();
            createJdbcComponents();
            this.jobLauncher = createJobLauncher();
        } catch (final Exception e) {
            log.error("Error while creating Spring Batch infrastructure", e);
            throw new SpringBatchLightminConfigurationException(e, e.getMessage());
        }
    }

    protected DataSource createInMemoryDataSource() {
        final EmbeddedDatabase database = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .build();
        return database;
    }

    protected void createJdbcComponents() throws Exception {
        this.jobInstanceDao = createJobInstanceDao();
        this.jobExecutionDao = createJobExecutionDao();
        this.stepExecutionDao = createStepExecutionDao();
        this.jobRepository = createJobRepository();
        this.jobExplorer = this.jobRepository;
    }

    protected JobLauncher createJobLauncher() throws Exception {
        final TaskExecutorJobLauncher launcher = new TaskExecutorJobLauncher();
        launcher.setJobRepository(this.jobRepository);
        launcher.afterPropertiesSet();
        return launcher;
    }

    protected JobRepository createJobRepository() throws Exception {
        final JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(this.dataSource);
        factory.setTransactionManager(this.transactionManager);
        factory.setTablePrefix(this.tablePrefix);
        factory.setJobInstanceIncrementerName("JOB_INSTANCE_SEQ");
        final String isolationLevel = determineIsolationLevel();
        if (isolationLevel != null) {
            factory.setIsolationLevelForCreate(isolationLevel);
        }
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    protected String determineIsolationLevel() {
        return null;
    }

    protected PlatformTransactionManager createTransactionManager() {
        return new DataSourceTransactionManager(this.dataSource);
    }

    protected JobInstanceDao createJobInstanceDao() throws Exception {
        final JdbcJobInstanceDao dao = new JdbcJobInstanceDao();
        dao.setJdbcTemplate(this.jdbcTemplate);
        dao.setJobInstanceIncrementer(this.incrementer);
        dao.setTablePrefix(this.tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

    protected JobExecutionDao createJobExecutionDao() throws Exception {
        final JdbcJobExecutionDao dao = new JdbcJobExecutionDao();
        dao.setJdbcTemplate(this.jdbcTemplate);
        dao.setJobExecutionIncrementer(this.incrementer);
        dao.setJobInstanceDao((JdbcJobInstanceDao) this.jobInstanceDao);
        dao.setTablePrefix(this.tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }

    protected StepExecutionDao createStepExecutionDao() throws Exception {
        final JdbcStepExecutionDao dao = new JdbcStepExecutionDao();
        dao.setJdbcTemplate(this.jdbcTemplate);
        dao.setStepExecutionIncrementer(this.incrementer);
        dao.setJobExecutionDao((JdbcJobExecutionDao) this.jobExecutionDao);
        dao.setTablePrefix(this.tablePrefix);
        dao.afterPropertiesSet();
        return dao;
    }
}
