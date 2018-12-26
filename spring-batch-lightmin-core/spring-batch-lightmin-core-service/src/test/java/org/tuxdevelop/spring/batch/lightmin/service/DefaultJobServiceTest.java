package org.tuxdevelop.spring.batch.lightmin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.tuxdevelop.spring.batch.lightmin.batch.dao.LightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
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
        when(this.jobExplorer.getJobInstanceCount(JOB_NAME)).thenReturn(expectedInstanceCount);
        final Integer instanceCount = this.jobService.getJobInstanceCount(JOB_NAME);
        assertThat(instanceCount).isEqualTo(expectedInstanceCount);
    }

    @Test
    public void getJobInstanceCountNoSuchJobExceptionTest() throws NoSuchJobException {
        final Integer expectedInstanceCount = 0;
        when(this.jobExplorer.getJobInstanceCount(JOB_NAME)).thenThrow(new NoSuchJobException("TEST"));
        final Integer instanceCount = this.jobService.getJobInstanceCount(JOB_NAME);
        assertThat(instanceCount).isEqualTo(expectedInstanceCount);
    }

    @Test
    public void getJobNamesTest() {
        final Collection<String> jobNames = new HashSet<>(Arrays.asList(JOB_NAMES));
        when(this.jobRegistry.getJobNames()).thenReturn(jobNames);
        final Set<String> fetchedJobNames = this.jobService.getJobNames();
        assertThat(fetchedJobNames).contains(JOB_NAME, JOB_NAME_2, JOB_NAME_3);
    }

    @Test
    public void getJobByNameTest() throws NoSuchJobException {
        final Job expectedJob = DomainTestHelper.createJob(JOB_NAME);
        when(this.jobRegistry.getJob(JOB_NAME)).thenReturn(expectedJob);
        final Job job = this.jobService.getJobByName(JOB_NAME);
        assertThat(job).isEqualTo(expectedJob);
    }

    @Test
    public void getJobByNameNoSuchJobExecptionTest() throws NoSuchJobException {
        when(this.jobRegistry.getJob(JOB_NAME)).thenThrow(new NoSuchJobException("TEST"));
        final Job job = this.jobService.getJobByName(JOB_NAME);
        assertThat(job).isNull();
    }

    @Test
    public void getJobInstancesTest() {
        when(this.jobExplorer.getJobInstances(JOB_NAME, 0, 10)).thenReturn(DomainTestHelper.createJobInstances(10, JOB_NAME));
        final Collection<JobInstance> jobInstances = this.jobService.getJobInstances(JOB_NAME, 0, 10);
        assertThat(jobInstances).isNotEmpty();
        assertThat(jobInstances.size()).isEqualTo(10);
    }

    @Test
    public void getJobExecutionsTest() {
        final JobInstance jobInstance = DomainTestHelper.createJobInstance(1l, JOB_NAME);
        when(this.jobExplorer.getJobExecutions(jobInstance)).thenReturn(DomainTestHelper.createJobExecutions(10));
        final Collection<JobExecution> jobExecutions = this.jobService.getJobExecutions(jobInstance);
        assertThat(jobExecutions).isNotEmpty();
        assertThat(jobExecutions.size()).isEqualTo(10);
    }

    @Test
    public void getJobExecutionsPageTest() {
        final JobInstance jobInstance = DomainTestHelper.createJobInstance(1l, JOB_NAME);
        when(this.lightminJobExecutionDao.findJobExecutions(jobInstance, 1, 5))
                .thenReturn(DomainTestHelper.createJobExecutions(5));
        final Collection<JobExecution> jobExecutions = this.jobService.getJobExecutions(jobInstance, 1, 5);
        assertThat(jobExecutions).isNotEmpty();
        assertThat(jobExecutions.size()).isEqualTo(5);
    }

    @Test
    public void getJobExecutionTest() {
        final Long jobExecutionId = 10L;
        when(this.jobExplorer.getJobExecution(jobExecutionId)).thenReturn(DomainTestHelper.createJobExecution(jobExecutionId));
        final JobExecution jobExecution = this.jobService.getJobExecution(jobExecutionId);
        assertThat(jobExecution).isNotNull();
        assertThat(jobExecution.getId()).isEqualTo(jobExecutionId);
    }

    @Test
    public void getJobInstanceTest() {
        final Long jobInstanceId = 20L;
        when(this.jobExplorer.getJobInstance(jobInstanceId)).thenReturn(
                DomainTestHelper.createJobInstance(jobInstanceId, JOB_NAME));
        final JobInstance jobInstance = this.jobService.getJobInstance(jobInstanceId);
        assertThat(jobInstance).isNotNull();
        assertThat(jobInstance.getInstanceId()).isEqualTo(jobInstanceId);
        assertThat(jobInstance.getJobName()).isEqualTo(JOB_NAME);
    }

    @Test
    public void attachJobInstanceTest() {
        final JobExecution jobExecution = DomainTestHelper.createJobExecution(10L);
        final JobInstance jobInstance = DomainTestHelper.createJobInstance(20L, JOB_NAME);
        jobExecution.setJobInstance(jobInstance);
        when(this.jobExplorer.getJobInstance(jobExecution.getJobInstance().getId())).thenReturn(jobInstance);
        this.jobService.attachJobInstance(jobExecution);
        assertThat(jobExecution.getJobInstance()).isEqualTo(jobInstance);
    }

    @Test
    public void getJobExecutionCountTest() {
        final Integer count = 10;
        when(this.lightminJobExecutionDao.getJobExecutionCount(any(JobInstance.class))).thenReturn(count);
        final Integer resultCount = this.jobService.getJobExecutionCount(new JobInstance(1L, "testjob"));
        assertThat(resultCount).isEqualTo(count);
    }

    @Test
    public void restartJobExecutionTest() {
        final Long jobExecutionId = 10L;
        try {
            this.jobService.restartJobExecution(jobExecutionId);
            verify(this.jobOperator, times(1)).restart(jobExecutionId);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Test(expected = SpringBatchLightminApplicationException.class)
    public void restartJobExecutionExceptionTest() throws JobParametersInvalidException, JobRestartException,
            JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, NoSuchJobException {
        final Long jobExecutionId = 10L;
        when(this.jobOperator.restart(jobExecutionId)).thenThrow(NoSuchJobExecutionException.class);
        this.jobService.restartJobExecution(jobExecutionId);
    }

    @Test
    public void stopJobExecutionTest() {
        final Long jobExecutionId = 10L;
        try {
            this.jobService.stopJobExecution(jobExecutionId);
            verify(this.jobOperator, times(1)).stop(jobExecutionId);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Test(expected = SpringBatchLightminApplicationException.class)
    public void stopJobExecutionExceptionTest() throws JobParametersInvalidException, JobRestartException,
            JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, NoSuchJobException,
            JobExecutionNotRunningException {
        final Long jobExecutionId = 10L;
        when(this.jobOperator.stop(jobExecutionId)).thenThrow(NoSuchJobExecutionException.class);
        this.jobService.stopJobExecution(jobExecutionId);
    }

    @Test
    public void getLastJobParametersTest() {
        final List<JobExecution> jobExecutions = new LinkedList<>();
        final JobParameters jobParameters = new JobParametersBuilder().addLong("long", 1L).addString("String",
                "someString").toJobParameters();
        final JobExecution jobExecution = new JobExecution(1L, jobParameters, "test");
        jobExecutions.add(jobExecution);
        when(this.lightminJobExecutionDao.getJobExecutions(anyString(), anyInt(), anyInt())).thenReturn(jobExecutions);
        final JobParameters result = this.jobService.getLastJobParameters("test");
        assertThat(result).isEqualTo(jobParameters);
    }

    @Test
    public void getLastJobParametersNotExistingTest() {
        final List<JobExecution> jobExecutions = new LinkedList<>();
        final JobParameters jobParameters = new JobParameters();
        when(this.lightminJobExecutionDao.getJobExecutions(anyString(), anyInt(), anyInt())).thenReturn(jobExecutions);
        final JobParameters result = this.jobService.getLastJobParameters("test");
        assertThat(result).isEqualTo(jobParameters);
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.jobService = new DefaultJobService(this.jobOperator, this.jobRegistry, this.jobExplorer, this.lightminJobExecutionDao);
    }
}
