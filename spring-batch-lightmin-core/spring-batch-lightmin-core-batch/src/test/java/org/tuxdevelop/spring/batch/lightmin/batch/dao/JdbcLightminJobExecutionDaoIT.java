package org.tuxdevelop.spring.batch.lightmin.batch.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.job.*;
import org.springframework.batch.core.job.parameters.*;
import org.springframework.batch.core.step.*;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.repository.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.test.configuration.ITPersistenceConfiguration;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ITPersistenceConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class JdbcLightminJobExecutionDaoIT {

    private static final Integer JOB_EXECUTION_COUNT = 10;

    @Autowired
    private LightminJobExecutionDao jdbcLightminJobExecutionDao;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private Job simpleJob;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepository jobRepository;

    @Test
    public void getJobExecutionCountIT() {
        this.init();
        final JobInstance jobInstance = this.jobExplorer.getLastJobInstance("simpleJob");
        final int count = this.jdbcLightminJobExecutionDao.getJobExecutionCount(jobInstance);
        assertThat(count).isEqualTo(JOB_EXECUTION_COUNT);
    }

    @Test
    public void getJobExecutionCountZeroIT() {
        this.init();
        final JobInstance jobInstance = new JobInstance(9999L, "notExisting");
        final int count = this.jdbcLightminJobExecutionDao.getJobExecutionCount(jobInstance);
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void findJobExecutionsIT() {
        this.init();
        final JobInstance jobInstance = this.jobExplorer.getLastJobInstance("simpleJob");
        final List<JobExecution> jobExecutions = this.jdbcLightminJobExecutionDao.findJobExecutions(jobInstance, 0,
                JOB_EXECUTION_COUNT);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).hasSize(JOB_EXECUTION_COUNT);
        for (final JobExecution jobExecution : jobExecutions) {
            assertThat(jobExecution.getJobInstance()).isEqualTo(jobInstance);
        }
    }

    @Test
    public void findJobExecutionsPageIT() {
        this.init();
        final int count = 5;
        final JobInstance jobInstance = this.jobExplorer.getLastJobInstance("simpleJob");
        final List<JobExecution> jobExecutions = this.jdbcLightminJobExecutionDao.findJobExecutions(jobInstance, 0,
                count);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).hasSize(count);
        for (final JobExecution jobExecution : jobExecutions) {
            assertThat(jobExecution.getJobInstance()).isEqualTo(jobInstance);
        }
    }

    @Test
    public void getJobExecutionsPageIT() {
        this.init();
        final int count = 5;
        final JobInstance jobInstance = this.jobExplorer.getLastJobInstance("simpleJob");
        final List<JobExecution> jobExecutions = this.jdbcLightminJobExecutionDao.getJobExecutions(jobInstance.getJobName(), 0,
                count);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).hasSize(count);
        for (final JobExecution jobExecution : jobExecutions) {
            final JobExecution fromRepo = this.jobExplorer.getJobExecution(jobExecution.getId());
            //has to be set to null
            fromRepo.setJobInstance(null);
            assertThat(jobExecution).isEqualTo(fromRepo);
        }
    }

    @Test
    public void findJobExecutionsEmptyIT() {
        this.init();
        final JobInstance jobInstance = new JobInstance(9999L, "notExisting");
        final List<JobExecution> jobExecutions = this.jdbcLightminJobExecutionDao.findJobExecutions(jobInstance, 0, 10);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).isEmpty();
    }

    @Test
    public void testFindJobExecutionsAllQueryParameter() {
        this.init();
        final String jobName = "simpleJob";
        final Integer size = 4;
        final Date startDate = new Date(System.currentTimeMillis() - 100000);
        final Date endDate = new Date(System.currentTimeMillis() + 100000);
        final String exitStatus = ExitStatus.COMPLETED.getExitCode();
        final Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(QueryParameterKey.EXIT_STATUS, exitStatus);
        queryParameters.put(QueryParameterKey.START_DATE, startDate);
        queryParameters.put(QueryParameterKey.END_DATE, endDate);
        final List<JobExecution> result = this.jdbcLightminJobExecutionDao.findJobExecutions(jobName, queryParameters, size);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(size);
    }

    @Test
    public void testFindJobExecutionsAllQueryParameterWithoutJobName() {
        this.init();
        final Integer size = 4;
        final Date startDate = new Date(System.currentTimeMillis() - 100000);
        final Date endDate = new Date(System.currentTimeMillis() + 100000);
        final String exitStatus = ExitStatus.COMPLETED.getExitCode();
        final Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(QueryParameterKey.EXIT_STATUS, exitStatus);
        queryParameters.put(QueryParameterKey.START_DATE, startDate);
        queryParameters.put(QueryParameterKey.END_DATE, endDate);
        final List<JobExecution> result = this.jdbcLightminJobExecutionDao.findJobExecutions(null, queryParameters, size);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(size);
    }

    @Test
    public void testFindJobExecutionsExitStatus() {
        this.init();
        final String jobName = "simpleJob";
        final Integer size = 4;
        final String exitStatus = ExitStatus.COMPLETED.getExitCode();
        final Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(QueryParameterKey.EXIT_STATUS, exitStatus);
        final List<JobExecution> result = this.jdbcLightminJobExecutionDao.findJobExecutions(jobName, queryParameters, size);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(size);
    }

    @Test
    public void testFindJobExecutionsStartDate() {
        this.init();
        final String jobName = "simpleJob";
        final Integer size = 4;
        final Date startDate = new Date(System.currentTimeMillis() - 100000);
        final Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(QueryParameterKey.START_DATE, startDate);
        final List<JobExecution> result = this.jdbcLightminJobExecutionDao.findJobExecutions(jobName, queryParameters, size);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(size);
    }

    @Test
    public void testFindJobExecutionsEndDate() {
        this.init();
        final String jobName = "simpleJob";
        final Integer size = 4;
        final Date endDate = new Date(System.currentTimeMillis() + 100000);
        final Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(QueryParameterKey.END_DATE, endDate);
        final List<JobExecution> result = this.jdbcLightminJobExecutionDao.findJobExecutions(jobName, queryParameters, size);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(size);
    }


    private void init() {
        try {
            final JobParameters parameters = new JobParametersBuilder().toJobParameters();
            JobInstance jobInstance = this.jobRepository.getJobInstance("simpleJob", parameters);
            if (jobInstance == null) {
                jobInstance = this.jobRepository.createJobInstance("simpleJob", parameters);
            }
            for (int i = 0; i < JOB_EXECUTION_COUNT; i++) {
                final JobExecution execution = this.jobRepository.createJobExecution(jobInstance, parameters, new ExecutionContext());
                final java.time.LocalDateTime now = java.time.LocalDateTime.now();
                execution.setStartTime(now.minusSeconds(1));
                execution.setEndTime(now);
                execution.setStatus(BatchStatus.COMPLETED);
                execution.setExitStatus(ExitStatus.COMPLETED);
                this.jobRepository.update(execution);
            }
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }
}
