package org.tuxdevelop.spring.batch.lightmin.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.tuxdevelop.spring.batch.lightmin.model.JobExecutionModel;
import org.tuxdevelop.spring.batch.lightmin.model.JobInstanceModel;
import org.tuxdevelop.spring.batch.lightmin.service.AdminService;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;

import java.util.Collection;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobControllerTest {

    @Mock
    private JobService jobService;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private JobController jobController;

    @Test
    public void enrichJobExecutionTest() {
        final JobExecutionModel model = new JobExecutionModel();
        final Collection<StepExecution> executions = new LinkedList<StepExecution>();
        executions.add(createStepExecution());
        jobController.enrichJobExecution(model, executions);
        assertThat(model.getStepExecutions()).isNotEmpty();
        assertThat(model.getStepExecutions()).hasSize(1);
    }

    @Test
    public void enrichJobInstanceModelTest() {

        when(jobService.getJobExecutions(any(JobInstance.class))).thenReturn(createJobExecutions());

        final JobInstanceModel model = new JobInstanceModel();
        final JobInstance instance = new JobInstance(1L, "testInstance");
        jobController.enrichJobInstanceModel(model, instance);
        assertThat(model.getJobExecutions()).isNotNull();
        assertThat(model.getJobExecutions()).hasSize(1);
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    StepExecution createStepExecution() {
        return new StepExecution("testStep", new JobExecution(1L));
    }

    Collection<JobExecution> createJobExecutions() {
        final Collection<JobExecution> jobExecutions = new LinkedList<JobExecution>();
        final JobExecution jobExecution = new JobExecution(1L);
        jobExecution.setJobInstance(new JobInstance(1L, "testInstance"));
        jobExecutions.add(jobExecution);
        return jobExecutions;
    }
}
