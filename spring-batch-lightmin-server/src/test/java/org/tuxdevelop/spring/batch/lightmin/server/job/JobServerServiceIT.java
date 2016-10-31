package org.tuxdevelop.spring.batch.lightmin.server.job;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.WebIntegrationTest;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@WebIntegrationTest({"server.port=0", "management.port=0"})
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
        final LightminClientApplication lightminClientApplication = createLightminClientApplication();
        final JobExecution result = getJobServerService().getJobExecution(jobExecution.getId(), lightminClientApplication);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(jobExecution.getId());
    }

    @Test
    public void testGetJobInstances() {
        final LightminClientApplication lightminClientApplication = createLightminClientApplication();
        final JobInstancePage result = getJobServerService().getJobInstances(simpleJob.getName(), 0, 10, lightminClientApplication);
        assertThat(result).isNotNull();
        assertThat(result.getPageSize()).isGreaterThan(0);
        assertThat(result.getTotalJobInstanceCount()).isGreaterThan(0);
        assertThat(result.getJobName()).isEqualTo(simpleJob.getName());
    }

    @Test
    public void testGetJobInfo() {
        final LightminClientApplication lightminClientApplication = createLightminClientApplication();
        final JobInfo result = getJobServerService().getJobInfo(simpleJob.getName(), lightminClientApplication);
        assertThat(result).isNotNull();
        assertThat(result.getJobName()).isEqualTo(simpleJob.getName());
        assertThat(result.getJobInstanceCount()).isGreaterThan(0);
    }

    @Test
    public void testGetJobExecutionPage() {
        final LightminClientApplication lightminClientApplication = createLightminClientApplication();
        final JobExecutionPage result = getJobServerService().getJobExecutionPage(launchedJobInstanceId, lightminClientApplication);
        assertThat(result).isNotNull();
        assertThat(result.getJobInstanceId()).isEqualTo(launchedJobInstanceId);
        assertThat(result.getPageSize()).isEqualTo(1);
        assertThat(result.getTotalJobExecutionCount()).isEqualTo(1);
        assertThat(result.getJobExecutions()).hasSize(1);
    }

    @Test
    public void testGetStepExecution() {
        final LightminClientApplication lightminClientApplication = createLightminClientApplication();
        final StepExecution result = getJobServerService().getStepExecution(launchedJobExecutionId, launchedStepExecutionId, lightminClientApplication);
        assertThat(result).isNotNull();
        assertThat(result.getJobExecutionId()).isEqualTo(launchedJobExecutionId);
        assertThat(result.getId()).isEqualTo(launchedStepExecutionId);
    }

    public abstract JobServerService getJobServerService();

    public abstract LightminClientApplication createLightminClientApplication();

    @Before
    public void init() {
        try {
            final org.springframework.batch.core.JobExecution execution = jobLauncher.run(simpleJob, new JobParametersBuilder().addLong("time", System
                    .currentTimeMillis())
                    .toJobParameters());
            launchedJobExecutionId = execution.getId();
            launchedJobInstanceId = execution.getJobInstance().getId();
            final Collection<org.springframework.batch.core.StepExecution> stepExecutions = execution.getStepExecutions();
            for (final org.springframework.batch.core.StepExecution stepExecution : stepExecutions) {
                launchedStepExecutionId = stepExecution.getId();
            }
            jobExecution = execution;
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

}
