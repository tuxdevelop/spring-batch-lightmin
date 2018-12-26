package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.api.resource.util.ApiParameterParser;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.launcher.JobLauncherModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.test.api.ApiTestHelper;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobLauncherFeServiceTest {

    private JobLauncherFeService jobLauncherFeService;
    @Mock
    private RegistrationBean registrationBean;
    @Mock
    private JobServerService jobServerService;

    @Test
    public void testGetJobLauncherModel() {

        final String jobName = "testJob";
        final String applicationInstanceId = "testApp";

        final String name1 = "longValue";
        final String name2 = "stringValue";
        final JobParameter jobParameter1 = ApiTestHelper.createJobParameter(1L, ParameterType.LONG);
        final JobParameter jobParameter2 = ApiTestHelper.createJobParameter("test", ParameterType.STRING);
        final Map<String, JobParameter> jobParameterMap = new HashMap<>();
        ApiTestHelper.addJobParameter(jobParameterMap, name1, jobParameter1);
        ApiTestHelper.addJobParameter(jobParameterMap, name2, jobParameter2);

        final JobParameters jobParameters = ApiTestHelper.createJobParameters(jobParameterMap);

        final LightminClientApplication applicationInstance = new LightminClientApplication();
        when(this.registrationBean.findById(applicationInstanceId)).thenReturn(applicationInstance);

        when(this.jobServerService.getLastJobParameters(jobName, applicationInstance)).thenReturn(jobParameters);

        final JobLauncherModel result = this.jobLauncherFeService.getJobLauncherModel(jobName, applicationInstanceId);

        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getJobParameters()).isEqualTo(ApiParameterParser.parseParametersToString(jobParameters));
        BDDAssertions.then(result.getApplicationInstanceId()).isEqualTo(applicationInstanceId);
        BDDAssertions.then(result.getJobName()).isEqualTo(jobName);
    }

    @Test
    public void testLaunchJob() {
        final String jobName = "testJob";
        final String applicationInstanceId = "testApp";

        final LightminClientApplication applicationInstance = new LightminClientApplication();
        when(this.registrationBean.findById(applicationInstanceId)).thenReturn(applicationInstance);

        try {
            final JobLauncherModel jobLauncherModel = new JobLauncherModel();
            jobLauncherModel.setApplicationInstanceId(applicationInstanceId);
            jobLauncherModel.setJobName(jobName);
            jobLauncherModel.setJobParameters("test(String)=hello,longValue(long)=1");
            jobLauncherModel.setJobIncrementer("DATE");
            this.jobLauncherFeService.launchJob(jobLauncherModel);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.jobLauncherFeService = new JobLauncherFeService(this.registrationBean, this.jobServerService);
    }

}
