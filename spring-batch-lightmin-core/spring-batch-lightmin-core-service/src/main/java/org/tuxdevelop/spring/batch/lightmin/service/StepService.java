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
     * Retrieves a {@link StepExecution} for a given
     * {@link JobExecution} and step execution id
     *
     * @param jobExecution    current {@link JobExecution} to get the
     *                        {@link StepExecution} from
     * @param stepExecutionId Id of the {@link StepExecution}
     * @return the matching {@link StepExecution}
     */
    StepExecution getStepExecution(JobExecution jobExecution,
                                   Long stepExecutionId);

    /**
     * attaches the corresponding {@link StepExecution}s to a given
     * {@link JobExecution}
     *
     * @param jobExecution the {@link JobExecution} to attache the
     *                     {@link StepExecution}s
     */
    void attachStepExecutions(JobExecution jobExecution);

}
