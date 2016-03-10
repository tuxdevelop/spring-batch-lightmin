package org.tuxdevelop.spring.batch.lightmin.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;

public class MapLightminJobExecutionDaoTest {

    private static final Integer JOB_EXECUTION_COUNT = 10;

    private MapLightminJobExecutionDao mapLightminJobExecutionDao;
    private JobExplorer jobExplorer;
    private JobExecutionDao jobExecutionDao;
    private JobInstanceDao jobInstanceDao;
    private JobInstance jobInstance;

    @Test
    public void getJobExecutionCountTest() {
        final int count = mapLightminJobExecutionDao.getJobExecutionCount(jobInstance);
        assertThat(count).isEqualTo(JOB_EXECUTION_COUNT);
    }

    @Test
    public void getJobExecutionCountZeroTest() {
        final JobInstance jobInstance = new JobInstance(9999L, "notExisting");
        final int count = mapLightminJobExecutionDao.getJobExecutionCount(jobInstance);
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void findJobExecutionsIT() {
        final List<JobExecution> jobExecutions = mapLightminJobExecutionDao.findJobExecutions(jobInstance, 0,
                JOB_EXECUTION_COUNT);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).hasSize(JOB_EXECUTION_COUNT);
        for (final JobExecution jobExecution : jobExecutions) {
            assertThat(jobExecution.getJobInstance()).isEqualTo(jobInstance);
        }
    }

    @Test
    public void findJobExecutionsPageIT() {
        final int count = 5;
        final List<JobExecution> jobExecutions = mapLightminJobExecutionDao.findJobExecutions(jobInstance, 0,
                count);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).hasSize(count);
        for (final JobExecution jobExecution : jobExecutions) {
            assertThat(jobExecution.getJobInstance()).isEqualTo(jobInstance);
        }
    }

    @Test
    public void getJobExecutionsPageIT() {
        final int count = 5;
        final List<JobExecution> jobExecutions = mapLightminJobExecutionDao.getJobExecutions(jobInstance.getJobName(),
                0,
                count);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).hasSize(count);
        for (final JobExecution jobExecution : jobExecutions) {
            assertThat(jobExecution.getJobInstance()).isEqualTo(jobInstance);
        }
    }

    @Test
    public void findJobExecutionsEmptyIT() {
        final JobInstance jobInstance = new JobInstance(9999L, "notExisting");
        final List<JobExecution> jobExecutions = mapLightminJobExecutionDao.findJobExecutions(jobInstance, 0, 10);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).isEmpty();
    }

    @Before
    public void init() throws Exception {
        final MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean = new MapJobRepositoryFactoryBean();
        mapJobRepositoryFactoryBean.getObject();
        jobExecutionDao = mapJobRepositoryFactoryBean.getJobExecutionDao();
        jobInstanceDao = mapJobRepositoryFactoryBean.getJobInstanceDao();
        final MapJobExplorerFactoryBean mapJobExplorerFactoryBean = new MapJobExplorerFactoryBean(
                mapJobRepositoryFactoryBean);
        jobExplorer = mapJobExplorerFactoryBean.getObject();
        mapLightminJobExecutionDao = new MapLightminJobExecutionDao(jobExplorer);
        jobInstance = jobInstanceDao.createJobInstance("someJob", new JobParametersBuilder().toJobParameters());
        final List<JobExecution> jobExecutions = TestHelper.createJobExecutions(JOB_EXECUTION_COUNT);
        for (final JobExecution jobExecution : jobExecutions) {
            jobExecution.setId(null);
            jobExecution.setJobInstance(jobInstance);
            jobExecutionDao.saveJobExecution(jobExecution);
        }

    }

}
