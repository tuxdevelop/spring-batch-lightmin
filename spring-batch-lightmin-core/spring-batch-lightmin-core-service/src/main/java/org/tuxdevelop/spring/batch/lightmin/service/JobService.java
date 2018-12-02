package org.tuxdevelop.spring.batch.lightmin.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collection;
import java.util.Set;

/**
 * @author Marcel Becker
 * @since 0.1
 */
public interface JobService extends InitializingBean {

    /**
     * Retrieves the current count of job instances of a job
     *
     * @param jobName name of the job
     * @return the count of job instances
     */
    int getJobInstanceCount(String jobName);

    /**
     * Retrieves the current count of job executions of a {@link JobInstance}
     *
     * @param jobInstance current jobInstance
     * @return the count of job executions
     */
    int getJobExecutionCount(JobInstance jobInstance);

    /**
     * Retrieves the names of all registered {@link Job}
     *
     * @return a {@link Set} of job names
     */
    Set<String> getJobNames();

    /**
     * Retrieves a {@link Job} for a given job Name
     *
     * @param jobName the name of the job to get
     * @return the {@link Job} or null
     */
    Job getJobByName(String jobName);

    /**
     * Retrieves all  {@link JobInstance}s for a given job name
     * starting from a given index and limited by a page size
     *
     * @param jobName    name of the job
     * @param startIndex start index of the page
     * @param pageSize   size of the page
     * @return all {@link JobInstance}s of the job name within the page
     */
    Collection<JobInstance> getJobInstances(String jobName, int startIndex, int pageSize);

    /**
     * Retrieves all {@link JobExecution}s for a given {@link org.springframework
     * .batch.core.JobInstance}
     *
     * @param jobInstance the {@link JobInstance} the get the executions for.
     * @return a {@link Collection} of {@link JobExecution}s
     */
    Collection<JobExecution> getJobExecutions(JobInstance jobInstance);

    /**
     * Retrieves all {@link JobExecution}s for a given {@link org.springframework
     * .batch.core.JobInstance}
     *
     * @param jobInstance the {@link JobInstance} the get the executions for.
     * @param start       index of the {@link JobExecution}s
     * @param count       count of {@link JobExecution}s to fetch
     * @return a {@link Collection} of {@link JobExecution}s
     */
    Collection<JobExecution> getJobExecutions(JobInstance jobInstance, int start, int count);

    /**
     * Retrieves a {@link JobExecution} for a give jobExceutionId
     *
     * @param jobExecutionId Id of the requested {@link JobExecution}
     * @return the {@link JobExecution} for the given id or null
     */
    JobExecution getJobExecution(Long jobExecutionId);

    /**
     * Retrieves a {@link JobInstance} for a give jobInstanceId
     *
     * @param jobInstanceId Id of the requested {@link JobInstance}
     * @return the {@link JobInstance} for the given id or null
     */
    JobInstance getJobInstance(Long jobInstanceId);

    /**
     * attaches the corresponding {@link JobInstance} to a given {@link org
     * .springframework.batch.core.JobExecution}
     *
     * @param jobExecution the {@link JobExecution} to attache the {@link JobInstance}
     */
    void attachJobInstance(JobExecution jobExecution);

    /**
     * restarts a jobExecution for a given id
     *
     * @param jobExecutionId id of the {@link JobExecution}
     */
    void restartJobExecution(Long jobExecutionId);

    /**
     * stops a jobExecution for a given id
     *
     * @param jobExecutionId id of the {@link JobExecution}
     */
    void stopJobExecution(Long jobExecutionId);

    /**
     * returns the last set {@link JobParameters} for a given jobName
     *
     * @param jobName the name of the {@link Job}
     * @return the {@link JobParameters} of the job
     */
    JobParameters getLastJobParameters(String jobName);
}
