package org.tuxdevelop.spring.batch.lightmin.server.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobExecution;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobExecutionPage;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobInstance;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.StepExecution;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.model.JobExecutionModel;
import org.tuxdevelop.spring.batch.lightmin.model.JobInstanceModel;
import org.tuxdevelop.spring.batch.lightmin.server.job.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobControllerTest {

    //TODO: fix me

    @Mock
    private JobServerService jobServerService;
    @Mock
    private RegistrationBean registrationBean;

    @Mock
    private LightminClientApplication lightminClientApplication;

    @InjectMocks
    private JobController jobController;

    @Test
    public void enrichJobExecutionTest() {
        final JobExecutionModel model = new JobExecutionModel();
        final Collection<StepExecution> executions = new LinkedList<>();
        executions.add(createStepExecution());
        jobController.enrichJobExecution(model, executions);
        assertThat(model.getStepExecutions()).isNotEmpty();
        assertThat(model.getStepExecutions()).hasSize(1);
    }

    @Test
    public void enrichJobInstanceModelTest() {
        when(jobServerService.getJobExecutionPage(anyLong(), any(LightminClientApplication.class))).thenReturn(createJobExecutionPage());
        final JobInstanceModel model = new JobInstanceModel();
        final JobInstance jobInstance = new JobInstance();
        jobInstance.setId(1L);
        jobInstance.setJobName("testInstance");
        jobController.enrichJobInstanceModel(model, jobInstance, lightminClientApplication);
        assertThat(model.getJobExecutions()).isNotNull();
        assertThat(model.getJobExecutions()).hasSize(1);
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        jobController = new JobController(jobServerService, registrationBean);
    }

    StepExecution createStepExecution() {
        final StepExecution stepExecution = new StepExecution();
        stepExecution.setId(1L);
        stepExecution.setStepName("testStepExecution");
        return stepExecution;
    }

    JobExecutionPage createJobExecutionPage() {
        final List<JobExecution> jobExecutions = new LinkedList<>();
        final JobExecution jobExecution = new JobExecution();
        jobExecution.setId(1L);
        final JobInstance jobInstance = new JobInstance();
        jobInstance.setId(1L);
        jobInstance.setJobName("testInstance");
        jobExecution.setJobInstance(jobInstance);
        jobExecutions.add(jobExecution);
        final JobExecutionPage jobExecutionPage = new JobExecutionPage();
        jobExecutionPage.setJobExecutions(jobExecutions);
        return jobExecutionPage;
    }
}
