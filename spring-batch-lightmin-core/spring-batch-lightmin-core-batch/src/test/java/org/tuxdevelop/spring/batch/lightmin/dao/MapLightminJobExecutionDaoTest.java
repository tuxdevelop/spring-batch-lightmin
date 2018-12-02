package org.tuxdevelop.spring.batch.lightmin.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.tuxdevelop.spring.batch.lightmin.batch.dao.MapLightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.batch.dao.QueryParameterKey;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MapLightminJobExecutionDaoTest {

    private static final Integer JOB_EXECUTION_COUNT = 10;

    private MapLightminJobExecutionDao mapLightminJobExecutionDao;
    private JobExplorer jobExplorer;
    private JobExecutionDao jobExecutionDao;
    private JobInstanceDao jobInstanceDao;
    private JobInstance jobInstance;

    @Test
    public void getJobExecutionCountTest() {
        final int count = this.mapLightminJobExecutionDao.getJobExecutionCount(this.jobInstance);
        assertThat(count).isEqualTo(JOB_EXECUTION_COUNT);
    }

    @Test
    public void getJobExecutionCountZeroTest() {
        final JobInstance jobInstance = new JobInstance(9999L, "notExisting");
        final int count = this.mapLightminJobExecutionDao.getJobExecutionCount(jobInstance);
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void findJobExecutionsIT() {
        final List<JobExecution> jobExecutions = this.mapLightminJobExecutionDao.findJobExecutions(this.jobInstance, 0,
                JOB_EXECUTION_COUNT);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).hasSize(JOB_EXECUTION_COUNT);
        for (final JobExecution jobExecution : jobExecutions) {
            assertThat(jobExecution.getJobInstance()).isEqualTo(this.jobInstance);
        }
    }

    @Test
    public void findJobExecutionsPageIT() {
        final int count = 5;
        final List<JobExecution> jobExecutions = this.mapLightminJobExecutionDao.findJobExecutions(this.jobInstance, 0,
                count);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).hasSize(count);
        for (final JobExecution jobExecution : jobExecutions) {
            assertThat(jobExecution.getJobInstance()).isEqualTo(this.jobInstance);
        }
    }

    @Test
    public void getJobExecutionsPageIT() {
        final int count = 5;
        final List<JobExecution> jobExecutions = this.mapLightminJobExecutionDao.getJobExecutions(this.jobInstance.getJobName(),
                0,
                count);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).hasSize(count);
        for (final JobExecution jobExecution : jobExecutions) {
            assertThat(jobExecution.getJobInstance()).isEqualTo(this.jobInstance);
        }
    }

    @Test
    public void findJobExecutionsEmptyIT() {
        final JobInstance jobInstance = new JobInstance(9999L, "notExisting");
        final List<JobExecution> jobExecutions = this.mapLightminJobExecutionDao.findJobExecutions(jobInstance, 0, 10);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).isEmpty();
    }

    @Test
    public void testFindJobExecutionsAllQueryParameters() {
        this.createJobExecutionsForQuery();
        final String jobName = "queryJob";
        final Integer size = 4;
        final Date startDate = new Date(System.currentTimeMillis() - 100000);
        final Date endDate = new Date(System.currentTimeMillis() + 100000);
        final String exitStatus = ExitStatus.COMPLETED.getExitCode();
        final Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(QueryParameterKey.EXIT_STATUS, exitStatus);
        queryParameters.put(QueryParameterKey.START_DATE, startDate);
        queryParameters.put(QueryParameterKey.END_DATE, endDate);
        final List<JobExecution> result = this.mapLightminJobExecutionDao.findJobExecutions(jobName, queryParameters, size);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(size);
    }

    @Test
    public void testFindJobExecutionsAllQueryParametersWithOutJobName() {
        this.createJobExecutionsForQuery();
        final Integer size = 4;
        final Date startDate = new Date(System.currentTimeMillis() - 100000);
        final Date endDate = new Date(System.currentTimeMillis() + 100000);
        final String exitStatus = ExitStatus.COMPLETED.getExitCode();
        final Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(QueryParameterKey.EXIT_STATUS, exitStatus);
        queryParameters.put(QueryParameterKey.START_DATE, startDate);
        queryParameters.put(QueryParameterKey.END_DATE, endDate);
        final List<JobExecution> result = this.mapLightminJobExecutionDao.findJobExecutions(null, queryParameters, size);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(size);
    }


    @Test
    public void testFindJobExecutionsExitStatus() {
        this.createJobExecutionsForQuery();
        final String jobName = "queryJob";
        final Integer size = 4;
        final String exitStatus = ExitStatus.COMPLETED.getExitCode();
        final Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(QueryParameterKey.EXIT_STATUS, exitStatus);
        final List<JobExecution> result = this.mapLightminJobExecutionDao.findJobExecutions(jobName, queryParameters, size);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(size);
    }

    @Test
    public void testFindJobExecutionsStartDate() {
        this.createJobExecutionsForQuery();
        final String jobName = "queryJob";
        final Integer size = 4;
        final Date startDate = new Date(System.currentTimeMillis() - 100000);
        final Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(QueryParameterKey.START_DATE, startDate);
        final List<JobExecution> result = this.mapLightminJobExecutionDao.findJobExecutions(jobName, queryParameters, size);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(size);
    }

    @Test
    public void testFindJobExecutionsEndDate() {
        this.createJobExecutionsForQuery();
        final String jobName = "queryJob";
        final Integer size = 4;
        final Date endDate = new Date(System.currentTimeMillis() + 100000);
        final Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(QueryParameterKey.END_DATE, endDate);
        final List<JobExecution> result = this.mapLightminJobExecutionDao.findJobExecutions(jobName, queryParameters, size);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(size);
    }


    @Before
    public void init() throws Exception {
        final MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean = new MapJobRepositoryFactoryBean();
        mapJobRepositoryFactoryBean.getObject();
        this.jobExecutionDao = mapJobRepositoryFactoryBean.getJobExecutionDao();
        this.jobInstanceDao = mapJobRepositoryFactoryBean.getJobInstanceDao();
        final MapJobExplorerFactoryBean mapJobExplorerFactoryBean = new MapJobExplorerFactoryBean(
                mapJobRepositoryFactoryBean);
        this.jobExplorer = mapJobExplorerFactoryBean.getObject();
        this.mapLightminJobExecutionDao = new MapLightminJobExecutionDao(this.jobExplorer);
        this.jobInstance = this.jobInstanceDao.createJobInstance("someJob", new JobParametersBuilder().toJobParameters());
        final List<JobExecution> jobExecutions = DomainTestHelper.createJobExecutions(JOB_EXECUTION_COUNT);
        for (final JobExecution jobExecution : jobExecutions) {
            jobExecution.setId(null);
            jobExecution.setJobInstance(this.jobInstance);
            this.jobExecutionDao.saveJobExecution(jobExecution);
        }
    }

    private void createJobExecutionsForQuery() {
        this.jobInstance = this.jobInstanceDao.createJobInstance("queryJob", new JobParametersBuilder().toJobParameters());
        final List<JobExecution> jobExecutions = DomainTestHelper.createJobExecutions(JOB_EXECUTION_COUNT);
        for (final JobExecution jobExecution : jobExecutions) {
            final Date startTime = new Date(System.currentTimeMillis() - 100);
            final Date endTime = new Date(System.currentTimeMillis() + 100);
            jobExecution.setId(null);
            jobExecution.setJobInstance(this.jobInstance);
            jobExecution.setStartTime(startTime);
            jobExecution.setEndTime(endTime);
            jobExecution.setExitStatus(ExitStatus.COMPLETED);
            this.jobExecutionDao.saveJobExecution(jobExecution);
        }
        for (final JobExecution jobExecution : jobExecutions) {
            final Date startTime = new Date(System.currentTimeMillis() - 100);
            final Date endTime = new Date(System.currentTimeMillis() + 100);
            jobExecution.setId(null);
            jobExecution.setJobInstance(this.jobInstance);
            jobExecution.setStartTime(startTime);
            jobExecution.setEndTime(endTime);
            jobExecution.setExitStatus(ExitStatus.FAILED);
            this.jobExecutionDao.saveJobExecution(jobExecution);
        }
    }

}
