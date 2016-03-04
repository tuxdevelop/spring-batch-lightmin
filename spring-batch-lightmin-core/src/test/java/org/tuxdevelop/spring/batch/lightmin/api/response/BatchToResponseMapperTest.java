package org.tuxdevelop.spring.batch.lightmin.api.response;


import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParametersBuilder;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BatchToResponseMapperTest {

    @Test
    public void testMapExecutions() {
        final int count = 5;
        final Collection<org.springframework.batch.core.JobExecution> jobExecutions =
                new LinkedList<org.springframework.batch.core.JobExecution>();
        for (int i = 0; i < count; i++) {
            final org.springframework.batch.core.JobExecution jobExecution = TestHelper.createJobExecution((long) i);
            final org.springframework.batch.core.JobInstance jobInstance =
                    TestHelper.createJobInstance((long) 1, "test");
            jobExecution.setJobInstance(jobInstance);
            jobExecutions.add(jobExecution);
        }
        final Collection<JobExecution> result = BatchToResponseMapper.mapExecutions(jobExecutions);
        assertThat(result).hasSize(count);
    }

    @Test
    public void testMapExecution() {
        final org.springframework.batch.core.JobExecution jobExecution = TestHelper.createJobExecution((long) 1);
        final org.springframework.batch.core.JobInstance jobInstance = TestHelper.createJobInstance((long) 1, "test");
        jobExecution.setJobInstance(jobInstance);
        final JobExecution result = BatchToResponseMapper.map(jobExecution);
        assertJobExecution(result, jobExecution);
    }

    @Test
    public void testMapStepExecutions() {
        final int count = 5;
        final List<org.springframework.batch.core.StepExecution> stepExecutions =
                new LinkedList<org.springframework.batch.core.StepExecution>();
        for (int i = 0; i < count; i++) {
            final org.springframework.batch.core.StepExecution stepExecution =
                    TestHelper.createStepExecution("testStep", TestHelper.createJobExecution(1L));
            stepExecutions.add(stepExecution);
        }
        final List<StepExecution> result = BatchToResponseMapper.mapStepExecutions(stepExecutions);
        assertThat(result).hasSize(count);
    }

    @Test
    public void testMapStepExecution() {
        final org.springframework.batch.core.StepExecution stepExecution =
                TestHelper.createStepExecution("testStep", TestHelper.createJobExecution(1L));
        final StepExecution result = BatchToResponseMapper.map(stepExecution);
        assertStepExecution(result, stepExecution);
    }

    @Test
    public void testMapJobInstances() {
        final int count = 5;
        final List<org.springframework.batch.core.JobInstance> jobInstances =
                new LinkedList<org.springframework.batch.core.JobInstance>();
        for (int i = 0; i < count; i++) {
            final org.springframework.batch.core.JobInstance jobInstance = TestHelper.createJobInstance(1L, "testJob");
            jobInstances.add(jobInstance);
        }

        final List<JobInstance> result = BatchToResponseMapper.mapInstances(jobInstances);
        assertThat(result).hasSize(count);
    }

    @Test
    public void tesMapJobInstance() {
        final org.springframework.batch.core.JobInstance jobInstance = TestHelper.createJobInstance(1L, "testJob");
        final JobInstance result = BatchToResponseMapper.map(jobInstance);
        assertJobInstance(result, jobInstance);
    }

    @Test
    public void testMapJobParameter() {
        final String parameterName = "Long";
        final Long value = 10L;
        final org.springframework.batch.core.JobParameters jobParameters =
                new JobParametersBuilder().addLong(parameterName, value).toJobParameters();
        final JobParameters result = BatchToResponseMapper.map(jobParameters);
        final Map<String, JobParameter> resultMap = result.getParameters();
        assertThat(resultMap.containsKey(parameterName)).isTrue();
        assertThat(resultMap.get(parameterName).getParameter()).isEqualTo(value);

    }

    @Test
    public void testMapExitStatus() {
        final String exitCode = "exitCode";
        final String exitDescription = "exitDescription";
        final org.springframework.batch.core.ExitStatus exitStatus = new ExitStatus(exitCode, exitDescription);
        final org.tuxdevelop.spring.batch.lightmin.api.response.ExitStatus result =
                BatchToResponseMapper.map(exitStatus);
        assertThat(result.getExitCode()).isEqualTo(exitCode);
        assertThat(result.getExitDescription()).isEqualTo(exitDescription);
    }

    void assertJobExecution(final JobExecution result, final org.springframework.batch.core.JobExecution jobExecution) {
        assertThat(result.getCreateTime()).isEqualTo(jobExecution.getCreateTime());
        assertThat(result.getEndTime()).isEqualTo(jobExecution.getEndTime());
        assertThat(result.getExecutionContext()).isEqualTo(jobExecution.getExecutionContext());
        assertThat(result.getFailureExceptions()).isEqualTo(jobExecution.getFailureExceptions());
        assertThat(result.getId()).isEqualTo(jobExecution.getId());
        assertThat(result.getJobConfigurationName()).isEqualTo(jobExecution.getJobConfigurationName());
        assertThat(result.getLastUpdated()).isEqualTo(jobExecution.getLastUpdated());
        assertThat(result.getStartTime()).isEqualTo(jobExecution.getStartTime());
        assertThat(result.getVersion()).isEqualTo(jobExecution.getVersion());
    }

    void assertStepExecution(final StepExecution result, final org.springframework.batch.core.StepExecution stepExecution) {
        assertThat(result.getStatus()).isEqualTo(stepExecution.getStatus());
        assertThat(result.getCommitCount()).isEqualTo(stepExecution.getCommitCount());
        assertThat(result.getEndTime()).isEqualTo(stepExecution.getEndTime());
        assertThat(result.getExecutionContext()).isEqualTo(stepExecution.getExecutionContext());
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

    void assertJobInstance(final JobInstance result, final org.springframework.batch.core.JobInstance jobInstance) {
        assertThat(result.getId()).isEqualTo(jobInstance.getId());
        assertThat(result.getVersion()).isEqualTo(jobInstance.getVersion());
        assertThat(result.getJobName()).isEqualTo(jobInstance.getJobName());
    }
}
