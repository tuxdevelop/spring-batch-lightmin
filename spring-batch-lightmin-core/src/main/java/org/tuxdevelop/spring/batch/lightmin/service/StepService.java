package org.tuxdevelop.spring.batch.lightmin.service;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Marcel Becker
 * @since 0.1
 */
public interface StepService extends InitializingBean {

    /**
     * Retrieves a {@link org.springframework.batch.core.StepExecution} for a given
     * {@link org.springframework.batch.core.JobExecution} and step execution id
     *
     * @param jobExecution    current {@link org.springframework.batch.core.JobExecution} to get the
     *                        {@link org.springframework.batch.core.StepExecution} from
     * @param stepExecutionId Id of the {@link org.springframework.batch.core.StepExecution}
     * @return the matching {@link org.springframework.batch.core.StepExecution}
     */
    StepExecution getStepExecution(JobExecution jobExecution,
                                   Long stepExecutionId);

    /**
     * attaches the corresponding {@link org.springframework.batch.core.StepExecution}s to a given
     * {@link org.springframework.batch.core.JobExecution}
     *
     * @param jobExecution the {@link org.springframework.batch.core.JobExecution} to attache the
     *                     {@link org.springframework.batch.core.StepExecution}s
     */
    void attachStepExecutions(JobExecution jobExecution);

}
