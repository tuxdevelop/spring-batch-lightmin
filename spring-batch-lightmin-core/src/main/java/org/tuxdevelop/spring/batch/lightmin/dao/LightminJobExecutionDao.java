package org.tuxdevelop.spring.batch.lightmin.dao;

import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;

/**
 * Created by milo on 19/10/15.
 */
public interface LightminJobExecutionDao {

    /**
     * @param jobInstance {@link org.springframework.batch.core.JobInstance} to find {@link org.springframework.batch.core.JobExecution}s for.
     * @param start       index position of the page
     * @param count       size of the page
     * @return List of {@link org.springframework.batch.core.JobExecution}s
     */
    List<JobExecution> findJobExecutions(JobInstance jobInstance, int start, int count);

    /**
     * @param jobInstance {@link org.springframework.batch.core.JobInstance} to the count for
     * @return the count of executions
     */
    int getJobExecutionCount(JobInstance jobInstance);
}
