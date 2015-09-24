package org.tuxdevelop.spring.batch.lightmin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultStepServiceTest {

	private static final String STEP_NAME = "sampleStep";

	@InjectMocks
	private DefaultStepService stepService;
	@Mock
	private StepExecutionDao stepExecutionDao;

	@Test
	public void getStepExecutionTest() {
		final JobExecution jobExecution = TestHelper.createJobExecution(10L);
		final Long stepExecutionId = 20L;
		when(stepExecutionDao.getStepExecution(jobExecution, stepExecutionId)).thenReturn(
				TestHelper.createStepExecution(STEP_NAME, jobExecution));
		final StepExecution stepExecution = stepService.getStepExecution(jobExecution, stepExecutionId);
		assertThat(stepExecution).isNotNull();
		assertThat(stepExecution.getStepName()).isEqualTo(STEP_NAME);
	}

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		stepService = new DefaultStepService(stepExecutionDao);
	}

}
