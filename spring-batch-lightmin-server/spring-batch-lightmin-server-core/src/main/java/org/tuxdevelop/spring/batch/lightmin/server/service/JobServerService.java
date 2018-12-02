package org.tuxdevelop.spring.batch.lightmin.server.service;

import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

import java.util.List;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public interface JobServerService {


    /**
     * Retrieves a {@link JobExecution} for a given id
     * for a given {@link LightminClientApplication}
     *
     * @param jobExecutionId            the id of the jobExecution
     * @param lightminClientApplication the LightminClientApplication
     * @return the JobExecution
     */
    JobExecution getJobExecution(Long jobExecutionId,
                                 LightminClientApplication lightminClientApplication);

    /**
     * Retrieves {@link JobInstancePage} containing {@link JobInstance}s for a given name of a
     * {@link org.springframework.batch.core.Job}
     * for a given {@link LightminClientApplication}
     *
     * @param jobName                   the name of the Spring Batch Job
     * @param startIndex                the index position of the page
     * @param pageSize                  the size of the page
     * @param lightminClientApplication the LightminClientApplication
     * @return the JobInstancePage
     */
    JobInstancePage getJobInstances(String jobName,
                                    Integer startIndex,
                                    Integer pageSize,
                                    LightminClientApplication lightminClientApplication);

    /**
     * Retrieves high level {@link JobInfo} of a {@link org.springframework.batch.core.Job} for a given job name
     * for a given {@link LightminClientApplication}
     *
     * @param jobName                   the name of the Spring Batch Job
     * @param lightminClientApplication the LightminClientApplication
     * @return the JobInfo
     */
    JobInfo getJobInfo(String jobName,
                       LightminClientApplication lightminClientApplication);

    /**
     * Retrieves {@link JobExecutionPage} containing {@link JobExecution}s for a given
     * {@link org.springframework.batch.core.JobInstance} id
     * for a given {@link LightminClientApplication}
     *
     * @param jobInstanceId             the id of the JobInstance
     * @param startIndex                the index position of the page
     * @param pageSize                  the size of the page
     * @param lightminClientApplication the LightminClientApplication
     * @return the JobExecutionPage
     */
    JobExecutionPage getJobExecutionPage(final Long jobInstanceId,
                                         final Integer startIndex,
                                         final Integer pageSize,
                                         final LightminClientApplication lightminClientApplication);

    /**
     * Retrieves {@link JobExecutionPage} containing {@link JobExecution}s for a given
     * {@link org.springframework.batch.core.JobInstance} id
     * for a given {@link LightminClientApplication}
     *
     * @param jobInstanceId             the id of the JobInstance
     * @param lightminClientApplication the LightminClientApplication
     * @return the JobExecutionPage
     */
    JobExecutionPage getJobExecutionPage(Long jobInstanceId,
                                         LightminClientApplication lightminClientApplication);

    /**
     * Restarts a {@link org.springframework.batch.core.JobExecution} of a given id
     * for a given {@link LightminClientApplication}
     *
     * @param jobExecutionId            the id of the JobExecution
     * @param lightminClientApplication the LightminClientApplication
     */
    void restartJobExecution(final Long jobExecutionId, final LightminClientApplication lightminClientApplication);

    /**
     * Stops a {@link org.springframework.batch.core.JobExecution} of a given id
     * for a given {@link LightminClientApplication}
     *
     * @param jobExecutionId            the id of the JobExecution
     * @param lightminClientApplication the LightminClientApplication
     */
    void stopJobExecution(final Long jobExecutionId, final LightminClientApplication lightminClientApplication);

    /**
     * Retrieves a {@link StepExecution} of a {@link org.springframework.batch.core.JobExecution}
     * for a given {@link LightminClientApplication}
     *
     * @param jobExecutionId            the id of the {@link org.springframework.batch.core.JobExecution}
     * @param stepExecutionId           the id of the {@link org.springframework.batch.core.StepExecution}
     * @param lightminClientApplication the LightminClientApplication
     * @return the StepExecution
     */
    StepExecution getStepExecution(Long jobExecutionId, Long stepExecutionId, LightminClientApplication lightminClientApplication);


    /**
     * Lauches a {@link org.springframework.batch.core.Job} with the given values of the {@link JobLaunch} parameter
     * for a given {@link LightminClientApplication}
     *
     * @param jobLaunch                 the launch information for the Job
     * @param lightminClientApplication the LightminClientApplication
     */
    void launchJob(final JobLaunch jobLaunch, final LightminClientApplication lightminClientApplication);

    /**
     * Retrieves the last used {@link JobParameters} of a {@link org.springframework.batch.core.Job} run
     * for a given {@link LightminClientApplication}
     *
     * @param jobName                   the name of Spring Batch Job
     * @param lightminClientApplication the LightminClientApplication
     * @return the JobParameters
     */
    JobParameters getLastJobParameters(final String jobName, final LightminClientApplication lightminClientApplication);

    /**
     * Retrieves {@link JobExecution}s for a given Job Name and query parameters
     *
     * @param jobName                   name of the job, if null or empty, all known job names will be queried
     * @param lightminClientApplication the LightminClientApplication
     * @param queryParameter            query parameters
     *                                  EXIT_STATUS - String value of the Spring Batch {@link ExitStatus}
     *                                  START_TIME - Start time of the JobExecution {@link java.util.Date}
     *                                  END_TIME - End time of the JobExecution {@link java.util.Date}
     * @param resultSize                maximum size of the result set
     * @return all found {@link JobExecution}s depending on the query Parameters
     */
    List<JobExecution> findJobExecutions(final String jobName, final LightminClientApplication lightminClientApplication, final Map<String, Object> queryParameter, final Integer resultSize);

}
