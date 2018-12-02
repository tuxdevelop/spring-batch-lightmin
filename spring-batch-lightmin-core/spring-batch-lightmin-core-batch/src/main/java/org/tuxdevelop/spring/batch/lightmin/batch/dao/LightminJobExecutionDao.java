package org.tuxdevelop.spring.batch.lightmin.batch.dao;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;

import java.util.List;
import java.util.Map;

public interface LightminJobExecutionDao {

    /**
     * @param jobInstance {@link JobInstance} to find {@link JobExecution}s for.
     * @param start       index position of the page
     * @param count       size of the page
     * @return List of {@link JobExecution}s
     */
    List<JobExecution> findJobExecutions(JobInstance jobInstance, int start, int count);

    /**
     * @param jobInstance {@link JobInstance} to the count for
     * @return the count of executions
     */
    int getJobExecutionCount(JobInstance jobInstance);

    /**
     * @param jobName name of the {@link org.springframework.batch.core.Job}
     * @param start   start index of the page
     * @param count   size of the page
     * @return a Page of {@link JobExecution}s
     */
    List<JobExecution> getJobExecutions(String jobName, int start, int count);

    /**
     * @param jobName
     * @param queryParameter
     * @param size
     * @return
     */
    List<JobExecution> findJobExecutions(final String jobName, final Map<String, Object> queryParameter, final Integer size);
}
