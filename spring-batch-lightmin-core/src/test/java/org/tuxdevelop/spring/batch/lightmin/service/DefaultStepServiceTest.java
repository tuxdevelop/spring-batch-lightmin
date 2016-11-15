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
import org.springframework.batch.core.explore.JobExplorer;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultStepServiceTest {

    private static final String STEP_NAME = "sampleStep";

    @InjectMocks
    private DefaultStepService stepService;
    @Mock
    private JobExplorer jobExplorer;

    @Test
    public void getStepExecutionTest() {
        final JobExecution jobExecution = TestHelper.createJobExecution(10L);
        final Long stepExecutionId = 20L;
        when(jobExplorer.getStepExecution(jobExecution.getId(), stepExecutionId)).thenReturn(
                TestHelper.createStepExecution(STEP_NAME, jobExecution));
        final StepExecution stepExecution = stepService.getStepExecution(jobExecution, stepExecutionId);
        assertThat(stepExecution).isNotNull();
        assertThat(stepExecution.getStepName()).isEqualTo(STEP_NAME);
    }

    @Test
    public void testAttachStepExecutions() {
        final JobExecution jobExecution = TestHelper.createJobExecution(20L);
        when(jobExplorer.getJobExecution(jobExecution.getId())).thenReturn(jobExecution);
        stepService.attachStepExecutions(jobExecution);
        verify(jobExplorer, times(1)).getJobExecution(jobExecution.getId());
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        stepService = new DefaultStepService(jobExplorer);
    }

}
