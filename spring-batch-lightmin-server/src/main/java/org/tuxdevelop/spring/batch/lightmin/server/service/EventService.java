package org.tuxdevelop.spring.batch.lightmin.server.service;

import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;

import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public interface EventService {

    void handleJobExecutionFailedEvent(final JobExecutionEventInfo jobExecutionEventInfo);

    List<JobExecutionEventInfo> getAllFailedEvents();
}
