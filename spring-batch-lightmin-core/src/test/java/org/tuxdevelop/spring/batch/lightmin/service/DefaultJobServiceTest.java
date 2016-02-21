package org.tuxdevelop.spring.batch.lightmin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;
import org.tuxdevelop.spring.batch.lightmin.dao.LightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultJobServiceTest {

    private static final String JOB_NAME = "sampleJob";
    private static final String JOB_NAME_2 = "sampleJob2";
    private static final String JOB_NAME_3 = "sampleJob3";
    private static final String[] JOB_NAMES = new String[]{JOB_NAME, JOB_NAME_2, JOB_NAME_3};

    @InjectMocks
    private DefaultJobService jobService;
    @Mock
    private JobOperator jobOperator;
    @Mock
    private JobRegistry jobRegistry;
    @Mock
    private JobExplorer jobExplorer;
    @Mock
    private LightminJobExecutionDao lightminJobExecutionDao;

    @Test
    public void getJobInstanceCountTest() throws NoSuchJobException {
        final Integer expectedInstanceCount = 10;
        when(jobExplorer.getJobInstanceCount(JOB_NAME)).thenReturn(expectedInstanceCount);
        final Integer instanceCount = jobService.getJobInstanceCount(JOB_NAME);
        assertThat(instanceCount).isEqualTo(expectedInstanceCount);
    }

    @Test
    public void getJobInstanceCountNoSuchJobExceptionTest() throws NoSuchJobException {
        final Integer expectedInstanceCount = 0;
        when(jobExplorer.getJobInstanceCount(JOB_NAME)).thenThrow(new NoSuchJobException("TEST"));
        final Integer instanceCount = jobService.getJobInstanceCount(JOB_NAME);
        assertThat(instanceCount).isEqualTo(expectedInstanceCount);
    }

    @Test
    public void getJobNamesTest() {
        final Collection<String> jobNames = new HashSet<String>(Arrays.asList(JOB_NAMES));
        when(jobRegistry.getJobNames()).thenReturn(jobNames);
        final Set<String> fetchedJobNames = jobService.getJobNames();
        assertThat(fetchedJobNames).contains(JOB_NAME, JOB_NAME_2, JOB_NAME_3);
    }

    @Test
    public void getJobByNameTest() throws NoSuchJobException {
        final Job expectedJob = TestHelper.createJob(JOB_NAME);
        when(jobRegistry.getJob(JOB_NAME)).thenReturn(expectedJob);
        final Job job = jobService.getJobByName(JOB_NAME);
        assertThat(job).isEqualTo(expectedJob);
    }

    @Test
    public void getJobByNameNoSuchJobExecptionTest() throws NoSuchJobException {
        when(jobRegistry.getJob(JOB_NAME)).thenThrow(new NoSuchJobException("TEST"));
        final Job job = jobService.getJobByName(JOB_NAME);
        assertThat(job).isNull();
    }

    @Test
    public void getJobInstancesTest() {
        when(jobExplorer.getJobInstances(JOB_NAME, 0, 10)).thenReturn(TestHelper.createJobInstances(10, JOB_NAME));
        final Collection<JobInstance> jobInstances = jobService.getJobInstances(JOB_NAME, 0, 10);
        assertThat(jobInstances).isNotEmpty();
        assertThat(jobInstances.size()).isEqualTo(10);
    }

    @Test
    public void getJobExecutionsTest() {
        final JobInstance jobInstance = TestHelper.createJobInstance(1l, JOB_NAME);
        when(jobExplorer.getJobExecutions(jobInstance)).thenReturn(TestHelper.createJobExecutions(10));
        final Collection<JobExecution> jobExecutions = jobService.getJobExecutions(jobInstance);
        assertThat(jobExecutions).isNotEmpty();
        assertThat(jobExecutions.size()).isEqualTo(10);
    }

    @Test
    public void getJobExecutionsPageTest() {
        final JobInstance jobInstance = TestHelper.createJobInstance(1l, JOB_NAME);
        when(lightminJobExecutionDao.findJobExecutions(jobInstance, 1, 5)).thenReturn(TestHelper.createJobExecutions(5));
        final Collection<JobExecution> jobExecutions = jobService.getJobExecutions(jobInstance, 1, 5);
        assertThat(jobExecutions).isNotEmpty();
        assertThat(jobExecutions.size()).isEqualTo(5);
    }

    @Test
    public void getJobExecutionTest() {
        final Long jobExecutionId = 10L;
        when(jobExplorer.getJobExecution(jobExecutionId)).thenReturn(TestHelper.createJobExecution(jobExecutionId));
        final JobExecution jobExecution = jobService.getJobExecution(jobExecutionId);
        assertThat(jobExecution).isNotNull();
        assertThat(jobExecution.getId()).isEqualTo(jobExecutionId);
    }

    @Test
    public void getJobInstanceTest() {
        final Long jobInstanceId = 20L;
        when(jobExplorer.getJobInstance(jobInstanceId)).thenReturn(
                TestHelper.createJobInstance(jobInstanceId, JOB_NAME));
        final JobInstance jobInstance = jobService.getJobInstance(jobInstanceId);
        assertThat(jobInstance).isNotNull();
        assertThat(jobInstance.getInstanceId()).isEqualTo(jobInstanceId);
        assertThat(jobInstance.getJobName()).isEqualTo(JOB_NAME);
    }

    @Test
    public void attachJobInstanceTest() {
        final JobExecution jobExecution = TestHelper.createJobExecution(10L);
        final JobInstance jobInstance = TestHelper.createJobInstance(20L, JOB_NAME);
        jobExecution.setJobInstance(jobInstance);
        when(jobExplorer.getJobInstance(jobExecution.getJobInstance().getId())).thenReturn(jobInstance);
        jobService.attachJobInstance(jobExecution);
        assertThat(jobExecution.getJobInstance()).isEqualTo(jobInstance);
    }

    @Test
    public void getJobExecutionCountTest() {
        final Integer count = 10;
        when(lightminJobExecutionDao.getJobExecutionCount(any(JobInstance.class))).thenReturn(count);
        final Integer resultCount = jobService.getJobExecutionCount(new JobInstance(1L, "testjob"));
        assertThat(resultCount).isEqualTo(count);
    }

    @Test
    public void restartJobExecutionTest() {
        final Long jobExecutionId = 10L;
        try {
            jobService.restartJobExecution(jobExecutionId);
            verify(jobOperator, times(1)).restart(jobExecutionId);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void restartJobExecutionExceptionTest() throws JobParametersInvalidException, JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, NoSuchJobException {
        final Long jobExecutionId = 10L;
        when(jobOperator.restart(jobExecutionId)).thenThrow(NoSuchJobExecutionException.class);
        jobService.restartJobExecution(jobExecutionId);
    }

    @Test
    public void stopJobExecutionTest() {
        final Long jobExecutionId = 10L;
        try {
            jobService.stopJobExecution(jobExecutionId);
            verify(jobOperator, times(1)).stop(jobExecutionId);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void stopJobExecutionExceptionTest() throws JobParametersInvalidException, JobRestartException,
            JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, NoSuchJobException, JobExecutionNotRunningException {
        final Long jobExecutionId = 10L;
        when(jobOperator.stop(jobExecutionId)).thenThrow(NoSuchJobExecutionException.class);
        jobService.stopJobExecution(jobExecutionId);
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        jobService = new DefaultJobService(jobOperator, jobRegistry, jobExplorer, lightminJobExecutionDao);
    }
}
