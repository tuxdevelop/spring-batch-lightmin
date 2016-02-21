package org.tuxdevelop.spring.batch.lightmin.api.response;


import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;

import java.util.Collection;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;

public class BatchToResponseMapperTest {

    @Test
    public void testMapExecutions() {
        final int count = 5;
        final Collection<org.springframework.batch.core.JobExecution> jobExecutions = new LinkedList<org.springframework.batch.core.JobExecution>();
        for (int i = 0; i < count; i++) {
            final org.springframework.batch.core.JobExecution jobExecution = TestHelper.createJobExecution((long) i);
            final org.springframework.batch.core.JobInstance jobInstance = TestHelper.createJobInstance((long) 1, "test");
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

    //TODO: implement more tests
}
