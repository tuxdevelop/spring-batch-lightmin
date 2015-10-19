package org.tuxdevelop.spring.batch.lightmin.dao;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.repository.dao.JobExecutionDao;

import java.util.List;

/**
 * Created by milo on 19/10/15.
 */
public interface LightminJobExecutionDao extends JobExecutionDao {

    /**
     * @param job
     * @param start
     * @param count
     * @return
     */
    List<JobExecution> findJobExecutions(JobInstance job, int start, int count);

    int getJobExecutionCount(JobInstance jobInstance);
}
