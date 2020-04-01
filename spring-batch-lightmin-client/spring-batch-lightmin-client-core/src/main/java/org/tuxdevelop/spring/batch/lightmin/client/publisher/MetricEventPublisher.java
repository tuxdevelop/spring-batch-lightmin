package org.tuxdevelop.spring.batch.lightmin.client.publisher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.event.LightminJobExecutionEvent;
import org.tuxdevelop.spring.batch.lightmin.client.event.LightminStepExecutionEvent;

public class MetricEventPublisher implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishMetricEvent(JobExecutionEventInfo info) {
        LightminJobExecutionEvent lightminJobExecutionEvent = new LightminJobExecutionEvent(info);
        applicationEventPublisher.publishEvent(lightminJobExecutionEvent);
    }

    public void publishMetricEvent(StepExecutionEventInfo info) {
        LightminStepExecutionEvent lightminStepExecutionEvent = new LightminStepExecutionEvent(info);
        applicationEventPublisher.publishEvent(lightminStepExecutionEvent);
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {

        this.applicationEventPublisher = applicationEventPublisher;
    }
}
