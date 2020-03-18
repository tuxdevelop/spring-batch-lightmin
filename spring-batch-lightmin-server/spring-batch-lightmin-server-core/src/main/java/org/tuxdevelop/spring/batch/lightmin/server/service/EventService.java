package org.tuxdevelop.spring.batch.lightmin.server.service;

import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;

import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public interface EventService {

    void handleJobExecutionEvent(final JobExecutionEventInfo jobExecutionEventInfo);

    void handleMetricEvent(final JobExecutionEventInfo jobExecutionEventInfo);

    void handleMetricEvent(final StepExecutionEventInfo jobExecutionEventInfo);

    List<JobExecutionEventInfo> getAllJobExecutionEvents(int start, int count);

    List<JobExecutionEventInfo> getAllJobExecutionEventsByExitStatus(final ExitStatus exitStatus, int start, int count);

    int getJobExecutionEventInfoCount();
}
