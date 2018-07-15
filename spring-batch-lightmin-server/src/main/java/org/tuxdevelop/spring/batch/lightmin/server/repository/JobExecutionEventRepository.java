package org.tuxdevelop.spring.batch.lightmin.server.repository;

import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;

import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public interface JobExecutionEventRepository {

    void save(final JobExecutionEventInfo jobExecutionEventInfo);

    List<JobExecutionEventInfo> findAll(int start, int count);

    List<JobExecutionEventInfo> finalByExitStatus(final ExitStatus exitStatus, int start, int count);

    int getTotalCount();
}
