package org.tuxdevelop.spring.batch.lightmin.client.api;


import org.junit.Test;
import org.springframework.batch.core.JobParametersBuilder;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobExecution;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobInstance;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.StepExecution;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BatchToResourceMapperTest {

    @Test
    public void testMapExecutions() {
        final int count = 5;
        final Collection<org.springframework.batch.core.JobExecution> jobExecutions = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            final org.springframework.batch.core.JobExecution jobExecution = DomainTestHelper.createJobExecution((long) i);
            final org.springframework.batch.core.JobInstance jobInstance =
                    DomainTestHelper.createJobInstance((long) 1, "test");
            jobExecution.setJobInstance(jobInstance);
            jobExecutions.add(jobExecution);
        }
        final Collection<JobExecution> result = BatchToResourceMapper.mapExecutions(jobExecutions);
        assertThat(result).hasSize(count);
    }

    @Test
    public void testMapExecution() {
        final org.springframework.batch.core.JobExecution jobExecution = DomainTestHelper.createJobExecution((long) 1);
        final org.springframework.batch.core.JobInstance jobInstance = DomainTestHelper.createJobInstance((long) 1, "test");
        jobExecution.setJobInstance(jobInstance);
        final JobExecution result = BatchToResourceMapper.map(jobExecution);
        this.assertJobExecution(result, jobExecution);
    }

    @Test
    public void testMapStepExecutions() {
        final int count = 5;
        final List<org.springframework.batch.core.StepExecution> stepExecutions = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            final org.springframework.batch.core.StepExecution stepExecution =
                    DomainTestHelper.createStepExecution("testStep", DomainTestHelper.createJobExecution(1L));
            stepExecutions.add(stepExecution);
        }
        final List<StepExecution> result = BatchToResourceMapper.mapStepExecutions(stepExecutions);
        assertThat(result).hasSize(count);
    }

    @Test
    public void testMapStepExecution() {
        final org.springframework.batch.core.StepExecution stepExecution =
                DomainTestHelper.createStepExecution("testStep", DomainTestHelper.createJobExecution(1L));
        final StepExecution result = BatchToResourceMapper.map(stepExecution);
        this.assertStepExecution(result, stepExecution);
    }

    @Test
    public void testMapJobInstances() {
        final int count = 5;
        final List<org.springframework.batch.core.JobInstance> jobInstances = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            final org.springframework.batch.core.JobInstance jobInstance = DomainTestHelper.createJobInstance(1L, "testJob");
            jobInstances.add(jobInstance);
        }

        final List<JobInstance> result = BatchToResourceMapper.mapInstances(jobInstances);
        assertThat(result).hasSize(count);
    }

    @Test
    public void tesMapJobInstance() {
        final org.springframework.batch.core.JobInstance jobInstance = DomainTestHelper.createJobInstance(1L, "testJob");
        final JobInstance result = BatchToResourceMapper.map(jobInstance);
        this.assertJobInstance(result, jobInstance);
    }

    @Test
    public void testMapJobParameter() {
        final String parameterName = "Long";
        final Long value = 10L;
        final org.springframework.batch.core.JobParameters jobParameters =
                new JobParametersBuilder().addLong(parameterName, value).toJobParameters();
        final JobParameters result = BatchToResourceMapper.map(jobParameters);
        final Map<String, JobParameter> resultMap = result.getParameters();
        assertThat(resultMap.containsKey(parameterName)).isTrue();
        assertThat(resultMap.get(parameterName).getParameter()).isEqualTo(value);

    }

    @Test
    public void testMapExitStatus() {
        final String exitCode = "exitCode";
        final String exitDescription = "exitDescription";
        final org.springframework.batch.core.ExitStatus exitStatus = new org.springframework.batch.core.ExitStatus(exitCode, exitDescription);
        final ExitStatus result =
                BatchToResourceMapper.map(exitStatus);
        assertThat(result.getExitCode()).isEqualTo(exitCode);
        assertThat(result.getExitDescription()).isEqualTo(exitDescription);
    }

    private void assertJobExecution(final JobExecution result, final org.springframework.batch.core.JobExecution jobExecution) {
        assertThat(result.getCreateTime()).isEqualTo(jobExecution.getCreateTime());
        assertThat(result.getEndTime()).isEqualTo(jobExecution.getEndTime());
        assertThat(result.getFailureExceptions()).isEqualTo(jobExecution.getFailureExceptions());
        assertThat(result.getId()).isEqualTo(jobExecution.getId());
        assertThat(result.getJobConfigurationName()).isEqualTo(jobExecution.getJobConfigurationName());
        assertThat(result.getLastUpdated()).isEqualTo(jobExecution.getLastUpdated());
        assertThat(result.getStartTime()).isEqualTo(jobExecution.getStartTime());
        assertThat(result.getVersion()).isEqualTo(jobExecution.getVersion());
    }

    private void assertStepExecution(final StepExecution result, final org.springframework.batch.core.StepExecution stepExecution) {
        assertThat(result.getStatus()).isEqualTo(BatchToResourceMapper.map(stepExecution.getStatus()));
        assertThat(result.getCommitCount()).isEqualTo(stepExecution.getCommitCount());
        assertThat(result.getEndTime()).isEqualTo(stepExecution.getEndTime());
        assertThat(result.getFailureExceptions()).isEqualTo(stepExecution.getFailureExceptions());
        assertThat(result.getFilterCount()).isEqualTo(stepExecution.getFilterCount());
        assertThat(result.getId()).isEqualTo(stepExecution.getId());
        assertThat(result.getJobExecutionId()).isEqualTo(stepExecution.getJobExecution().getId());
        assertThat(result.getLastUpdated()).isEqualTo(stepExecution.getLastUpdated());
        assertThat(result.getProcessSkipCount()).isEqualTo(stepExecution.getProcessSkipCount());
        assertThat(result.getReadCount()).isEqualTo(stepExecution.getReadCount());
        assertThat(result.getReadSkipCount()).isEqualTo(stepExecution.getReadSkipCount());
        assertThat(result.getRollbackCount()).isEqualTo(stepExecution.getRollbackCount());
        assertThat(result.getStartTime()).isEqualTo(stepExecution.getStartTime());
        assertThat(result.getStepName()).isEqualTo(stepExecution.getStepName());
        assertThat(result.getVersion()).isEqualTo(stepExecution.getVersion());
        assertThat(result.getWriteCount()).isEqualTo(stepExecution.getWriteCount());
        assertThat(result.getWriteSkipCount()).isEqualTo(stepExecution.getWriteSkipCount());
    }

    private void assertJobInstance(final JobInstance result, final org.springframework.batch.core.JobInstance jobInstance) {
        assertThat(result.getId()).isEqualTo(jobInstance.getId());
        assertThat(result.getVersion()).isEqualTo(jobInstance.getVersion());
        assertThat(result.getJobName()).isEqualTo(jobInstance.getJobName());
    }
}
