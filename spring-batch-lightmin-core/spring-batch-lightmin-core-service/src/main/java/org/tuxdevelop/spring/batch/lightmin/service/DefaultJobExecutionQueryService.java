package org.tuxdevelop.spring.batch.lightmin.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.tuxdevelop.spring.batch.lightmin.batch.dao.LightminJobExecutionDao;

import java.util.List;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class DefaultJobExecutionQueryService implements JobExecutionQueryService {

    private static final String ASTERIX = "*";

    private final LightminJobExecutionDao lightminJobExecutionDao;

    public DefaultJobExecutionQueryService(final LightminJobExecutionDao lightminJobExecutionDao) {
        this.lightminJobExecutionDao = lightminJobExecutionDao;
    }

    @Override
    public List<JobExecution> findJobExecutions(final String jobName, final Map<String, Object> queryParameter, final Integer resultSize) {
        final String jobNameQuery = ASTERIX.equals(jobName) ? null : jobName;
        log.debug("Querying with Parameters jobName: {}, queryParameters: {}, resultSize {} ", jobNameQuery, queryParameter, resultSize);
        return this.lightminJobExecutionDao.findJobExecutions(jobNameQuery, queryParameter, resultSize);
    }
}
