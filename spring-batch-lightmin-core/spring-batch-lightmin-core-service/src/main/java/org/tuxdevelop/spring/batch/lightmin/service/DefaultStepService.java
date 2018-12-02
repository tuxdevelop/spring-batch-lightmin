package org.tuxdevelop.spring.batch.lightmin.service;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

/**
 * Default implementation of {@link StepService}
 *
 * @author Marcel Becker
 * @since 0.1
 */
public class DefaultStepService implements StepService {

    private final JobExplorer jobExplorer;

    public DefaultStepService(final JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }

    @Override
    public StepExecution getStepExecution(final JobExecution jobExecution, final Long stepExecutionId) {
        return this.jobExplorer.getStepExecution(jobExecution.getId(), stepExecutionId);
    }

    @Override
    public void attachStepExecutions(JobExecution jobExecution) {
        jobExecution = this.jobExplorer.getJobExecution(jobExecution.getId());
        if (jobExecution == null) {
            throw new SpringBatchLightminApplicationException("Could not attach StepExecutions for JobExecution");
        }
    }

    @Override
    public void afterPropertiesSet() {
        assert this.jobExplorer != null;
    }
}
