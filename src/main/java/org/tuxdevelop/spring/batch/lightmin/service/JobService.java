package org.tuxdevelop.spring.batch.lightmin.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Marcel Becker on 1/19/15.
 */
public interface JobService extends InitializingBean{

    /**
     *
     * @param jobName
     * @return
     */
    int getJobInstanceCount(String jobName);

    /**
     *
     * @return
     */
    Set<String> getJobNames();

    /**
     *
     * @param jobName
     * @return the job or null
     */
    Job getJobByName(String jobName);

    /**
     *
     * @param jobName
     * @param startIndex
     * @param pageSize
     * @return
     */
    Collection<JobInstance> getJobInstances(String jobName, int startIndex, int pageSize);

    /**
     *
     * @param jobInstance
     * @return
     */
    Collection<JobExecution> getJobExecutions(JobInstance jobInstance);

    /**
     *
     * @param jobExecutionId
     * @return
     */
	JobExecution getJobExecution(Long jobExecutionId);

	/**
	 *
	 * @param jobInstanceId
	 * @return
	 */
	JobInstance getJobInstance(Long jobInstanceId);
}
