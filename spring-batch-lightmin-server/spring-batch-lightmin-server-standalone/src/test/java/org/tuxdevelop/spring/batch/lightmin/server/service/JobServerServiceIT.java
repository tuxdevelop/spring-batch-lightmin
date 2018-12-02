package org.tuxdevelop.spring.batch.lightmin.server.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

public abstract class JobServerServiceIT {

    @Autowired
    private Job simpleJob;
    @Autowired
    private JobLauncher jobLauncher;

    private Long launchedJobExecutionId;
    private Long launchedJobInstanceId;
    private Long launchedStepExecutionId;
    private org.springframework.batch.core.JobExecution jobExecution;

    @Test
    public void testGetJobExecution() {
        final LightminClientApplication lightminClientApplication = this.createLightminClientApplication();
        final JobExecution result = this.getJobServerService().getJobExecution(this.jobExecution.getId(), lightminClientApplication);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(this.jobExecution.getId());
    }

    @Test
    public void testGetJobInstances() {
        final LightminClientApplication lightminClientApplication = this.createLightminClientApplication();
        final JobInstancePage result = this.getJobServerService().getJobInstances(this.simpleJob.getName(), 0, 10, lightminClientApplication);
        assertThat(result).isNotNull();
        assertThat(result.getPageSize()).isGreaterThan(0);
        assertThat(result.getTotalJobInstanceCount()).isGreaterThan(0);
        assertThat(result.getJobName()).isEqualTo(this.simpleJob.getName());
    }

    @Test
    public void testGetJobInfo() {
        final LightminClientApplication lightminClientApplication = this.createLightminClientApplication();
        final JobInfo result = this.getJobServerService().getJobInfo(this.simpleJob.getName(), lightminClientApplication);
        assertThat(result).isNotNull();
        assertThat(result.getJobName()).isEqualTo(this.simpleJob.getName());
        assertThat(result.getJobInstanceCount()).isGreaterThan(0);
    }

    @Test
    public void testGetJobExecutionPage() {
        final LightminClientApplication lightminClientApplication = this.createLightminClientApplication();
        final JobExecutionPage result = this.getJobServerService().getJobExecutionPage(this.launchedJobInstanceId, lightminClientApplication);
        assertThat(result).isNotNull();
        assertThat(result.getJobInstanceId()).isEqualTo(this.launchedJobInstanceId);
        assertThat(result.getPageSize()).isEqualTo(1);
        assertThat(result.getTotalJobExecutionCount()).isEqualTo(1);
        assertThat(result.getJobExecutions()).hasSize(1);
    }

    @Test
    public void testGetStepExecution() {
        final LightminClientApplication lightminClientApplication = this.createLightminClientApplication();
        final StepExecution result = this.getJobServerService().getStepExecution(this.launchedJobExecutionId, this.launchedStepExecutionId, lightminClientApplication);
        assertThat(result).isNotNull();
        assertThat(result.getJobExecutionId()).isEqualTo(this.launchedJobExecutionId);
        assertThat(result.getId()).isEqualTo(this.launchedStepExecutionId);
    }

    @Test
    public void testLaunchJob() {
        final JobParameters jobParameters = new JobParameters();
        final JobParameter jobParameter = new JobParameter();
        jobParameter.setParameter(10.1);
        jobParameter.setParameterType(ParameterType.DOUBLE);
        final JobParameter jobParameterDate = new JobParameter();
        jobParameterDate.setParameter("2017/02/10 13:42:00:001");
        jobParameterDate.setParameterType(ParameterType.DATE);
        final JobParameter jobParameterLong = new JobParameter();
        jobParameterLong.setParameter(10L);
        jobParameterLong.setParameterType(ParameterType.LONG);
        final JobParameter jobParameterInteger = new JobParameter();
        jobParameterInteger.setParameter(10);
        jobParameterInteger.setParameterType(ParameterType.LONG);
        final JobParameter jobParameterString = new JobParameter();
        jobParameterString.setParameter("testString");
        jobParameterString.setParameterType(ParameterType.STRING);
        final JobParameter jobParameterInc = new JobParameter();
        jobParameterInc.setParameter(System.currentTimeMillis());
        jobParameterInc.setParameterType(ParameterType.LONG);
        final Map<String, JobParameter> map = new HashMap<>();
        map.put("doubleValue", jobParameter);
        map.put("dateValue", jobParameterDate);
        map.put("longValue", jobParameterLong);
        map.put("integerValue", jobParameterInteger);
        map.put("stringValue", jobParameterString);
        map.put("incrementer", jobParameterInc);
        jobParameters.setParameters(map);
        final JobLaunch jobLaunch = new JobLaunch();
        jobLaunch.setJobName("simpleJob");
        jobLaunch.setJobParameters(jobParameters);
        try {
            this.getJobServerService().launchJob(jobLaunch, this.createLightminClientApplication());
        } catch (final Exception e) {
            fail(e.getMessage());
        }

    }

    public abstract JobServerService getJobServerService();

    public abstract LightminClientApplication createLightminClientApplication();

    @Before
    public void init() {
        try {
            final org.springframework.batch.core.JobExecution execution = this.jobLauncher.run(this.simpleJob, new JobParametersBuilder().addLong("time", System
                    .currentTimeMillis())
                    .toJobParameters());
            this.launchedJobExecutionId = execution.getId();
            this.launchedJobInstanceId = execution.getJobInstance().getId();
            final Collection<org.springframework.batch.core.StepExecution> stepExecutions = execution.getStepExecutions();
            for (final org.springframework.batch.core.StepExecution stepExecution : stepExecutions) {
                this.launchedStepExecutionId = stepExecution.getId();
            }
            this.jobExecution = execution;
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

}
