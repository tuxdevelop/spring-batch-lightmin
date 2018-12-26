package org.tuxdevelop.spring.batch.lightmin.service;

import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;

import java.util.List;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public interface JobServiceEntry {

    /**
     * Retrieves a {@link JobExecution} for a given id
     *
     * @param jobExecutionId the id of the jobExecution
     * @return the JobExecution
     */
    JobExecution getByJobExecutionId(final Long jobExecutionId);

    /**
     * Retrieves {@link JobExecutionPage} containing {@link JobExecution}s for a given
     * {@link org.springframework.batch.core.JobInstance} id
     *
     * @param jobInstanceId the id of the JobInstance
     * @param startIndex    the index position of the page
     * @param pageSize      the size of the page
     * @return the JobExecutionPage
     */
    JobExecutionPage getJobExecutionPage(final Long jobInstanceId,
                                         final Integer startIndex,
                                         final Integer pageSize);


    /**
     * Retrieves {@link JobExecutionPage} containing {@link JobExecution}s for a given
     * {@link org.springframework.batch.core.JobInstance} id
     *
     * @param jobInstanceId the id of the JobInstance
     * @return the JobExecutionPage
     */
    JobExecutionPage getJobExecutionPage(final Long jobInstanceId);

    /**
     * Retrieves {@link JobInstancePage} containing {@link JobInstance}s for a given name of a
     * {@link org.springframework.batch.core.Job}
     *
     * @param jobName    the name of the Spring Batch Job
     * @param startIndex the index position of the page
     * @param pageSize   the size of the page
     * @return the JobInstancePage
     */
    JobInstancePage getJobInstancesByJobName(final String jobName,
                                             final int startIndex,
                                             final int pageSize);

    /**
     * Retrieves high level {@link ApplicationJobInfo} of the Application
     *
     * @return the ApplicationJobInfo
     */
    ApplicationJobInfo getApplicationJobInfo();


    /**
     * Retrieves high level {@link JobInfo} of a {@link org.springframework.batch.core.Job} for a given job name
     *
     * @param jobName the name of the Spring Batch Job
     * @return the JobInfo
     */
    JobInfo getJobInfo(final String jobName);

    /**
     * Restarts a {@link org.springframework.batch.core.JobExecution} of a given id
     *
     * @param jobExecutionId the id of the JobExecution
     */
    void restartJobExecution(final Long jobExecutionId);

    /**
     * Stops a {@link org.springframework.batch.core.JobExecution} of a given id
     *
     * @param jobExecutionId the id of the JobExecution
     */
    void stopJobExecution(final Long jobExecutionId);

    /**
     * Retrieves a {@link StepExecution} of a {@link org.springframework.batch.core.JobExecution}
     *
     * @param jobExecutionId  the id of the {@link org.springframework.batch.core.JobExecution}
     * @param stepExecutionId the id of the {@link org.springframework.batch.core.StepExecution}
     * @return the StepExecution
     */
    StepExecution getStepExecution(final Long jobExecutionId, final Long stepExecutionId);

    /**
     * Lauches a {@link org.springframework.batch.core.Job} with the given values of the {@link JobLaunch} parameter
     *
     * @param jobLaunch the launch information for the Job
     */
    void launchJob(final JobLaunch jobLaunch);

    /**
     * Retrieves the last used {@link JobParameters} of a {@link org.springframework.batch.core.Job} run
     *
     * @param jobName the name of Spring Batch Job
     * @return the JobParamaters
     */
    JobParameters getLastJobParameters(final String jobName);

    /**
     * Retrieves {@link JobExecution}s for a given Job Name and query parameters
     *
     * @param jobName        name of the job, if null or empty, all known job names will be queried
     * @param queryParameter query parameters
     *                       EXIT_STATUS - String value of the Spring Batch {@link ExitStatus}
     *                       START_TIME - Start time of the JobExecution {@link java.util.Date}
     *                       END_TIME - End time of the JobExecution {@link java.util.Date}
     * @param resultSize     maximum size of the result set
     * @return all found {@link JobExecution}s depending on the query Parameters
     */
    List<JobExecution> findJobExecutions(final String jobName, final Map<String, Object> queryParameter, final Integer resultSize);

}
