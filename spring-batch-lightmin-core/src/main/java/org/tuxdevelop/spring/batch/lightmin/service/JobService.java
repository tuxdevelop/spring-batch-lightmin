package org.tuxdevelop.spring.batch.lightmin.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collection;
import java.util.Set;

/**
 * @author Marcel Becker
 * @version 0.1
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
     * Retrieves the names of all registered {@link org.springframework.batch.core.Job}
     *
     * @return a {@link java.util.Set} of job names
     */
    Set<String> getJobNames();

    /**
     *
     *
     * @param jobName
     * @return the {@link org.springframework.batch.core.Job} or null
     */
    Job getJobByName(String jobName);

    /**
     * @param jobName
     * @param startIndex
     * @param pageSize
     * @return
     */
    Collection<JobInstance> getJobInstances(String jobName, int startIndex, int pageSize);

    /**
     * @param jobInstance
     * @return
     */
    Collection<JobExecution> getJobExecutions(JobInstance jobInstance);

    /**
     * @param jobExecutionId
     * @return
     */
    JobExecution getJobExecution(Long jobExecutionId);

    /**
     * @param jobInstanceId
     * @return
     */
    JobInstance getJobInstance(Long jobInstanceId);

    /**
     * @param jobExecution
     */
    void attachJobInstance(JobExecution jobExecution);
}
