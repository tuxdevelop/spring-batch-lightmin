package org.tuxdevelop.spring.batch.lightmin.dao;


import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MapLightminJobExecutionDaoTest {

    private static final Long JOB_INSTANCE_ID = 99L;
    private static final Integer JOB_EXECUTION_COUNT = 10;

    private MapLightminJobExecutionDao mapLightminJobExecutionDao;
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
    public void findJobExecutionsEmptyIT() {
        final JobInstance jobInstance = new JobInstance(9999L, "notExisting");
        final List<JobExecution> jobExecutions = mapLightminJobExecutionDao.findJobExecutions(jobInstance, 0, 10);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).isEmpty();
    }

    @Before
    public void init() {
        mapLightminJobExecutionDao = new MapLightminJobExecutionDao();
        jobInstance = TestHelper.createJobInstance(JOB_INSTANCE_ID, "someJob");
        final List<JobExecution> jobExecutions = TestHelper.createJobExecutions(JOB_EXECUTION_COUNT);
        for (final JobExecution jobExecution : jobExecutions) {
            jobExecution.setId(null);
            jobExecution.setJobInstance(jobInstance);
            mapLightminJobExecutionDao.saveJobExecution(jobExecution);
        }

    }

}
