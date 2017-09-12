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
    public List<JobExecutionEventInfo> getAllEvents() {
        return this.jobExecutionEventRepository.findAll();
    }

    @Override
    public List<JobExecutionEventInfo> getAllEventsByExitStatus(final ExitStatus exitStatus) {
        return this.jobExecutionEventRepository.finalByExitStatus(exitStatus);
    }
}
