package org.tuxdevelop.spring.batch.lightmin.client.event;

import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.StepExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;

public class EmbeddedStepJobExecutionEventPublisher implements StepExecutionEventPublisher {

    private final EventService eventService;

    public EmbeddedStepJobExecutionEventPublisher(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void publishEvent(final StepExecutionEventInfo stepExecutionEventInfo) {
        eventService.handleStepExecutionEvent(stepExecutionEventInfo);
    }
}
