package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobExecution;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobExecutionPage;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobInstancePage;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ApplicationContextModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ContentPageModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch.JobExecutionDetailsModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch.JobExecutionModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.test.api.ApiTestHelper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JobExecutionFeServiceTest {

    private static final String APPLICATION_INSTANCE_ID = "instance_test";
    private static final Long JOB_EXECUTION_ID = 1L;

    private JobExecutionFeService jobExecutionFeService;

    @Mock
    private RegistrationBean registrationBean;
    @Mock
    private JobServerService jobServerService;
    @Mock
    private LightminClientProperties lightminClientProperties;

    @Test
    public void testGetJobExecutionModelPage() {
        final int pageSize = 5;
        final int startIndex = 0;
        final Long jobInstanceId = 1L;
        final LightminClientApplication applicationInstance = new LightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(applicationInstance);
        final JobExecutionPage jobExecutionPage = ApiTestHelper.createJobExecutionPage(pageSize);
        when(this.jobServerService.
                getJobExecutionPage(jobInstanceId, applicationInstance)).thenReturn(jobExecutionPage);
        final JobInstancePage jobInstancePage = ApiTestHelper.createJobInstancePage(1);
        when(this.jobServerService.getJobInstances("test_job", 0, pageSize, applicationInstance)).thenReturn(jobInstancePage);
        final ContentPageModel<List<JobExecutionModel>> result =
                this.jobExecutionFeService.getJobExecutionModelPage(APPLICATION_INSTANCE_ID, "test_job", startIndex, pageSize);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getValue()).isNotNull();
        BDDAssertions.then(result.getValue().size()).isEqualTo(pageSize);
    }

    @Test
    public void testGetJobExecutionDetailsModel() {

        final LightminClientApplication applicationInstance = new LightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(applicationInstance);

        final JobExecution jobExecution = ApiTestHelper.createJobExecution(JOB_EXECUTION_ID);
        when(this.jobServerService.getJobExecution(JOB_EXECUTION_ID, applicationInstance)).thenReturn(jobExecution);

        final JobExecutionDetailsModel result =
                this.jobExecutionFeService.getJobExecutionDetailsModel(JOB_EXECUTION_ID, APPLICATION_INSTANCE_ID);

        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getId()).isEqualTo(jobExecution.getId());

    }

    @Test
    public void testRestartJobExecution() {

        final LightminClientApplication applicationInstance = new LightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(applicationInstance);
        this.jobExecutionFeService.restartJobExecution(JOB_EXECUTION_ID, APPLICATION_INSTANCE_ID);
        verify(this.jobServerService, times(1))
                .restartJobExecution(JOB_EXECUTION_ID, applicationInstance);

    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void testRestartJobExecutionError() {

        final LightminClientApplication applicationInstance = new LightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(applicationInstance);
        doThrow(new RuntimeException("Test Exception")).when(this.jobServerService).restartJobExecution(JOB_EXECUTION_ID, applicationInstance);
        this.jobExecutionFeService.restartJobExecution(JOB_EXECUTION_ID, APPLICATION_INSTANCE_ID);
        verify(this.jobServerService, times(1))
                .restartJobExecution(JOB_EXECUTION_ID, applicationInstance);

    }

    @Test
    public void testStopJobExecution() {
        final LightminClientApplication applicationInstance = new LightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(applicationInstance);
        this.jobExecutionFeService.stopJobExecution(JOB_EXECUTION_ID, APPLICATION_INSTANCE_ID);
        verify(this.jobServerService, times(1))
                .stopJobExecution(JOB_EXECUTION_ID, applicationInstance);
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void testStopJobExecutionError() {

        final LightminClientApplication applicationInstance = new LightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(applicationInstance);
        doThrow(new RuntimeException("Test Exception")).when(this.jobServerService).stopJobExecution(JOB_EXECUTION_ID, applicationInstance);
        this.jobExecutionFeService.stopJobExecution(JOB_EXECUTION_ID, APPLICATION_INSTANCE_ID);
        verify(this.jobServerService, times(1))
                .stopJobExecution(JOB_EXECUTION_ID, applicationInstance);

    }

    /*
     * Test for CommonFeService
     */
    @Test
    public void testGetApplicationContextModel() {

        when(this.lightminClientProperties.getHealthUrl()).thenReturn("http://localhost:8180/health");
        when(this.lightminClientProperties.getName()).thenReturn("test-app");
        when(this.lightminClientProperties.getServiceUrl()).thenReturn("http://localhost:8180");
        when(this.lightminClientProperties.getServiceUrl()).thenReturn("http://localhost:8180");

        final List<String> jobNames = Arrays.asList("test-job1", "test-job2");

        final LightminClientApplication lightminClientApplication =
                LightminClientApplication.createApplication(jobNames, this.lightminClientProperties);

        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(lightminClientApplication);

        final ApplicationContextModel result = this.jobExecutionFeService.getApplicationContextModel(APPLICATION_INSTANCE_ID);

        BDDAssertions.then(result).isNotNull();
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.jobExecutionFeService = new JobExecutionFeService(this.registrationBean, this.jobServerService);
    }
}
