package org.tuxdevelop.spring.batch.lightmin.client.event;

import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;

public class EmbeddedMetricEventPublisher implements MetricEventPublisher {

    private final EventService eventService;

    public EmbeddedMetricEventPublisher(final EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void publishEvent(JobExecutionEventInfo jobExecutionEventInfo) {
        this.eventService.handleMetricEvent(jobExecutionEventInfo);
    }

    @Override
    public void publishEvent(StepExecutionEventInfo stepExecutionEventInfo) {
        this.eventService.handleMetricEvent(stepExecutionEventInfo);
    }
}
