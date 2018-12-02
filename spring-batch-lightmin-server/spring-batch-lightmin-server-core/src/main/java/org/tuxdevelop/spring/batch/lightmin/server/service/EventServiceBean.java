package org.tuxdevelop.spring.batch.lightmin.server.service;

import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JobExecutionEventRepository;

import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public class EventServiceBean implements EventService {

    private final JobExecutionEventRepository jobExecutionEventRepository;

    public EventServiceBean(final JobExecutionEventRepository jobExecutionEventRepository) {
        this.jobExecutionEventRepository = jobExecutionEventRepository;
    }

    @Override
    public void handleJobExecutionEvent(final JobExecutionEventInfo jobExecutionEventInfo) {
        this.jobExecutionEventRepository.save(jobExecutionEventInfo);
    }

    @Override
    public List<JobExecutionEventInfo> getAllEvents(final int start, final int count) {
        return this.jobExecutionEventRepository.findAll(start, count);
    }

    @Override
    public List<JobExecutionEventInfo> getAllEventsByExitStatus(
            final ExitStatus exitStatus,
            final int start,
            final int count) {
        return this.jobExecutionEventRepository.finalByExitStatus(exitStatus, start, count);
    }

    @Override
    public int getJobExecutionEventInfoCount() {
        return this.jobExecutionEventRepository.getTotalCount();
    }
}
