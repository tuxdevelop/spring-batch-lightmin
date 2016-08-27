package org.tuxdevelop.spring.batch.lightmin.server.job;

import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public interface JobServerService {


    /**
     * @param jobExecutionId
     * @param lightminClientApplication
     * @return
     */
    JobExecution getJobExecution(Long jobExecutionId,
                                 LightminClientApplication lightminClientApplication);

    /**
     * @param jobName
     * @param startIndex
     * @param pageSize
     * @param lightminClientApplication
     * @return
     */
    JobInstancePage getJobInstances(String jobName,
                                    Integer startIndex,
                                    Integer pageSize,
                                    LightminClientApplication lightminClientApplication);

    /**
     * @param jobName
     * @param lightminClientApplication
     * @return
     */
    JobInfo getJobInfo(String jobName,
                       LightminClientApplication lightminClientApplication);

    /**
     * @param jobInstanceId
     * @param startIndex
     * @param pageSize
     * @param lightminClientApplication
     * @return
     */
    JobExecutionPage getJobExecutionPage(final Long jobInstanceId,
                                         final Integer startIndex,
                                         final Integer pageSize,
                                         final LightminClientApplication lightminClientApplication);

    /**
     * @param jobInstanceId
     * @param lightminClientApplication
     * @return
     */
    JobExecutionPage getJobExecutionPage(Long jobInstanceId,
                                         LightminClientApplication lightminClientApplication);

    /**
     * @param jobExecutionId
     * @param lightminClientApplication
     */
    void restartJobExecution(final Long jobExecutionId, final LightminClientApplication lightminClientApplication);

    /**
     * @param jobExecutionId
     * @param lightminClientApplication
     */
    void stopJobExecution(final Long jobExecutionId, final LightminClientApplication lightminClientApplication);

    /**
     * @param jobExecutionId
     * @param stepExecutionId
     * @param lightminClientApplication
     * @return
     */
    StepExecution getStepExecution(Long jobExecutionId, Long stepExecutionId, LightminClientApplication lightminClientApplication);


    /**
     * @param jobLaunch
     * @param lightminClientApplication
     */
    void launchJob(final JobLaunch jobLaunch, final LightminClientApplication lightminClientApplication);

    /**
     * @param jobName
     * @param lightminClientApplication
     * @return
     */
    JobParameters getLastJobParameters(final String jobName, final LightminClientApplication lightminClientApplication);
}
