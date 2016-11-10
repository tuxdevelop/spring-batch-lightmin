package org.tuxdevelop.spring.batch.lightmin.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.test.configuration.ITPersistenceConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringJUnit4ClassRunner.class)
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

    @Test
    public void getJobExecutionCountIT() {
        init();
        final JobInstance jobInstance = jobExplorer.getJobInstance(1L);
        final int count = jdbcLightminJobExecutionDao.getJobExecutionCount(jobInstance);
        assertThat(count).isEqualTo(JOB_EXECUTION_COUNT);
    }

    @Test
    public void getJobExecutionCountZeroIT() {
        init();
        final JobInstance jobInstance = new JobInstance(9999L, "notExisting");
        final int count = jdbcLightminJobExecutionDao.getJobExecutionCount(jobInstance);
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void findJobExecutionsIT() {
        init();
        final JobInstance jobInstance = jobExplorer.getJobInstance(1L);
        final List<JobExecution> jobExecutions = jdbcLightminJobExecutionDao.findJobExecutions(jobInstance, 0,
                JOB_EXECUTION_COUNT);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).hasSize(JOB_EXECUTION_COUNT);
        for (final JobExecution jobExecution : jobExecutions) {
            assertThat(jobExecution.getJobInstance()).isEqualTo(jobInstance);
        }
    }

    @Test
    public void findJobExecutionsPageIT() {
        init();
        final int count = 5;
        final JobInstance jobInstance = jobExplorer.getJobInstance(1L);
        final List<JobExecution> jobExecutions = jdbcLightminJobExecutionDao.findJobExecutions(jobInstance, 0,
                count);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).hasSize(count);
        for (final JobExecution jobExecution : jobExecutions) {
            assertThat(jobExecution.getJobInstance()).isEqualTo(jobInstance);
        }
    }

    @Test
    public void getJobExecutionsPageIT() {
        init();
        final int count = 5;
        final JobInstance jobInstance = jobExplorer.getJobInstance(1L);
        final List<JobExecution> jobExecutions = jdbcLightminJobExecutionDao.getJobExecutions(jobInstance.getJobName(), 0,
                count);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).hasSize(count);
        for (final JobExecution jobExecution : jobExecutions) {
            final JobExecution fromRepo = jobExplorer.getJobExecution(jobExecution.getId());
            //has to be set to null
            fromRepo.setJobInstance(null);
            assertThat(jobExecution).isEqualTo(fromRepo);
        }
    }

    @Test
    public void findJobExecutionsEmptyIT() {
        init();
        final JobInstance jobInstance = new JobInstance(9999L, "notExisting");
        final List<JobExecution> jobExecutions = jdbcLightminJobExecutionDao.findJobExecutions(jobInstance, 0, 10);
        assertThat(jobExecutions).isNotNull();
        assertThat(jobExecutions).isEmpty();
    }

    private void init() {
        try {
            for (int i = 0; i < JOB_EXECUTION_COUNT; i++) {
                jobLauncher.run(simpleJob, new JobParametersBuilder().toJobParameters());
            }
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }
}
