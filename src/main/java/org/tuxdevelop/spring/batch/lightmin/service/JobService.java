package org.tuxdevelop.spring.batch.lightmin.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;

import java.util.Collection;
import java.util.Set;

/**
 * Created by marbecker on 1/19/15.
 */
public interface JobService {

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
     * @return
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
}
