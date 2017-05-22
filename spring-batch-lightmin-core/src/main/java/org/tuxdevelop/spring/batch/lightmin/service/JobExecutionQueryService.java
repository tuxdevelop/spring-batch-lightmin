package org.tuxdevelop.spring.batch.lightmin.service;


import org.springframework.batch.core.JobExecution;

import java.util.List;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public interface JobExecutionQueryService {


    /**
     * Retrieves {@link JobExecution}s for a given Job Name and query parameters
     *
     * @param jobName        name of the job, if null or empty, all known job names will be queried
     * @param queryParameter query parameters
     *                       EXIT_STATUS - String value of the Spring Batch {@link org.springframework.batch.core.ExitStatus}
     *                       START_TIME - Start time of the JobExecution {@link java.util.Date}
     *                       END_TIME - End time of the JobExecution {@link java.util.Date}
     * @param resultSize     maximum size of the result set
     * @return all found {@link JobExecution}s depending on the query Parameters
     */
    List<JobExecution> findJobExecutions(final String jobName, final Map<String, Object> queryParameter, final Integer resultSize);

}
