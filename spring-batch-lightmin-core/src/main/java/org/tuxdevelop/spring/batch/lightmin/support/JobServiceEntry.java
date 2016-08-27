package org.tuxdevelop.spring.batch.lightmin.support;

import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public interface JobServiceEntry {

    /**
     * @param jobExecutionId
     * @return
     */
    JobExecution getByJobExecutionId(final Long jobExecutionId);

    /**
     * @param jobInstanceId
     * @return
     */
    JobExecutionPage getJobExecutionPage(final Long jobInstanceId,
                                         final Integer startIndex,
                                         final Integer pageSize);


    /**
     * @param jobInstanceId
     * @return
     */
    JobExecutionPage getJobExecutionPage(final Long jobInstanceId);

    /**
     * @param jobName
     * @param startIndex
     * @param pageSize
     * @return
     */
    JobInstancePage getJobInstancesByJobName(final String jobName,
                                             final int startIndex,
                                             final int pageSize);

    /**
     * @return
     */
    ApplicationJobInfo getApplicationJobInfo();


    /**
     * @param jobName
     * @return
     */
    JobInfo getJobInfo(final String jobName);

    /**
     * @param jobExecutionId
     */
    void restartJobExecution(final Long jobExecutionId);

    /**
     * @param jobExecutionId
     */
    void stopJobExecution(final Long jobExecutionId);

    /**
     * @param jobExecutionId
     * @param stepExecutionId
     * @return
     */
    StepExecution getStepExecution(final Long jobExecutionId, final Long stepExecutionId);

    /**
     * @param jobLaunch
     */
    void launchJob(final JobLaunch jobLaunch);

    /**
     * @param jobName
     * @return
     */
    JobParameters getLastJobParameters(final String jobName);
}
