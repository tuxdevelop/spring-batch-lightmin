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
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
	private JobInstanceDao jobInstanceDao;
	@Mock
	private JobExecutionDao jobExecutionDao;

	@Test
	public void getJobInstanceCountTest() throws NoSuchJobException {
		final Integer expectedInstanceCount = 10;
		when(jobInstanceDao.getJobInstanceCount(JOB_NAME)).thenReturn(expectedInstanceCount);
		final Integer instanceCount = jobService.getJobInstanceCount(JOB_NAME);
		assertThat(instanceCount).isEqualTo(expectedInstanceCount);
	}

	@Test
	public void getJobInstanceCountNoSuchJobExceptionTest() throws NoSuchJobException {
		final Integer expectedInstanceCount = 0;
		when(jobInstanceDao.getJobInstanceCount(JOB_NAME)).thenThrow(new NoSuchJobException("TEST"));
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
		when(jobInstanceDao.getJobInstances(JOB_NAME, 0, 10)).thenReturn(TestHelper.createJobInstances(10, JOB_NAME));
		final Collection<JobInstance> jobInstances = jobService.getJobInstances(JOB_NAME, 0, 10);
		assertThat(jobInstances).isNotEmpty();
		assertThat(jobInstances.size()).isEqualTo(10);
	}

	@Test
	public void getJobExecutionsTest() {
		JobInstance jobInstance = TestHelper.createJobInstance(1l, JOB_NAME);
		when(jobExecutionDao.findJobExecutions(jobInstance)).thenReturn(TestHelper.createJobExecutions(10));
		final Collection<JobExecution> jobExecutions = jobService.getJobExecutions(jobInstance);
		assertThat(jobExecutions).isNotEmpty();
		assertThat(jobExecutions.size()).isEqualTo(10);
	}

	@Test
	public void getJobExecutionTest() {
		final Long jobExecutionId = 10L;
		when(jobExecutionDao.getJobExecution(jobExecutionId)).thenReturn(TestHelper.createJobExecution(jobExecutionId));
		final JobExecution jobExecution = jobService.getJobExecution(jobExecutionId);
		assertThat(jobExecution).isNotNull();
		assertThat(jobExecution.getId()).isEqualTo(jobExecutionId);
	}

	@Test
	public void getJobInstanceTest() {
		final Long jobInstanceId = 20L;
		when(jobInstanceDao.getJobInstance(jobInstanceId)).thenReturn(
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
		when(jobInstanceDao.getJobInstance(jobExecution)).thenReturn(jobInstance);
		jobService.attachJobInstance(jobExecution);
		assertThat(jobExecution.getJobInstance()).isEqualTo(jobInstance);
	}

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		jobService = new DefaultJobService(jobOperator, jobRegistry, jobInstanceDao, jobExecutionDao);
	}
}
