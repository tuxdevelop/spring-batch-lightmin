package org.tuxdevelop.spring.batch.lightmin.server.service;

import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JobExecutionEventRepository;

import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public class EventServiceBean implements EventService {

    private final JobExecutionEventRepository jobExecutionFailedEventRepository;

    public EventServiceBean(final JobExecutionEventRepository jobExecutionFailedEventRepository) {
        this.jobExecutionFailedEventRepository = jobExecutionFailedEventRepository;
    }

    @Override
    public void handleJobExecutionFailedEvent(final JobExecutionEventInfo jobExecutionEventInfo) {
        this.jobExecutionFailedEventRepository.save(jobExecutionEventInfo);
    }

    @Override
    public List<JobExecutionEventInfo> getAllFailedEvents() {
        return this.jobExecutionFailedEventRepository.findAll();
    }
}
