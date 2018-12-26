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
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;

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
        final JobExecution jobExecution = DomainTestHelper.createJobExecution(10L);
        final Long stepExecutionId = 20L;
        when(this.jobExplorer.getStepExecution(jobExecution.getId(), stepExecutionId)).thenReturn(
                DomainTestHelper.createStepExecution(STEP_NAME, jobExecution));
        final StepExecution stepExecution = this.stepService.getStepExecution(jobExecution, stepExecutionId);
        assertThat(stepExecution).isNotNull();
        assertThat(stepExecution.getStepName()).isEqualTo(STEP_NAME);
    }

    @Test
    public void testAttachStepExecutions() {
        final JobExecution jobExecution = DomainTestHelper.createJobExecution(20L);
        when(this.jobExplorer.getJobExecution(jobExecution.getId())).thenReturn(jobExecution);
        this.stepService.attachStepExecutions(jobExecution);
        verify(this.jobExplorer, times(1)).getJobExecution(jobExecution.getId());
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.stepService = new DefaultStepService(this.jobExplorer);
    }

}
