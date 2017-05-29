package org.tuxdevelop.spring.batch.lightmin.server.repository;

import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;

import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public interface JobExecutionEventRepository {

    void save(final JobExecutionEventInfo jobExecutionEventInfo);

    List<JobExecutionEventInfo> findAll();
}
