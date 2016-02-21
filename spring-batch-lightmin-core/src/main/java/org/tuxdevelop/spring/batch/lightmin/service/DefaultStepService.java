package org.tuxdevelop.spring.batch.lightmin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

@Slf4j
public class DefaultStepService implements StepService {

    private final JobExplorer jobExplorer;

    public DefaultStepService(final JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }

    @Override
    public StepExecution getStepExecution(final JobExecution jobExecution, final Long stepExecutionId) {
        return jobExplorer.getStepExecution(jobExecution.getId(), stepExecutionId);
    }

    @Override
    public void attachStepExecutions(JobExecution jobExecution) {
        jobExecution = jobExplorer.getJobExecution(jobExecution.getJobId());
        if (jobExecution == null) {
            throw new SpringBatchLightminApplicationException("Could not attach StepExecutions for JobExecution with " +
                    "id: " + jobExecution.getId());
        }
    }

    @Override
    public void afterPropertiesSet() {
        assert (jobExplorer != null);
    }
}
