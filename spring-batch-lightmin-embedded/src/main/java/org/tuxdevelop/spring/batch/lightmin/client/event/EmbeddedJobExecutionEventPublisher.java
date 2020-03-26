package org.tuxdevelop.spring.batch.lightmin.client.event;

import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.JobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;

public class EmbeddedJobExecutionEventPublisher implements JobExecutionEventPublisher {

    private final EventService eventService;

    public EmbeddedJobExecutionEventPublisher(final EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void publishEvent(final JobExecutionEventInfo jobExecutionEventInfo) {
        this.eventService.handleJobExecutionEvent(jobExecutionEventInfo);
    }
}
