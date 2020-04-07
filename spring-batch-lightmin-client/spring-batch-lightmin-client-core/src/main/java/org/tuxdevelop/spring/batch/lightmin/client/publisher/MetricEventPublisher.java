package org.tuxdevelop.spring.batch.lightmin.client.publisher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.event.LightminMetricJobExecutionEvent;
import org.tuxdevelop.spring.batch.lightmin.client.event.LightminMetricStepExecutionEvent;
import org.tuxdevelop.spring.batch.lightmin.event.EventTransformer;
import org.tuxdevelop.spring.batch.lightmin.event.JobExecutionEvent;

@Slf4j
public class MetricEventPublisher implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    @EventListener(value = JobExecutionEvent.class)
    public void listen(final JobExecutionEvent jobExecutionEvent) {
        final JobExecution jobExecution = jobExecutionEvent.getJobExecution();
        if (jobExecution != null) {
            final JobExecutionEventInfo jobExecutionEventInfo =
                    EventTransformer.transformToJobExecutionEventInfo(
                            jobExecution,
                            jobExecutionEvent.getApplicationName());
            this.publishMetricEvent(jobExecutionEventInfo);
            jobExecution.getStepExecutions()
                    .stream()
                    .map(step -> EventTransformer.transformToStepExecutionEventInfo(step, jobExecutionEvent.getApplicationName()))
                    .forEach(this::publishMetricEvent);
        } else {
            log.debug("JobExecutionEvent source was null, nothing todo!");
        }

    }

    public void publishMetricEvent(final JobExecutionEventInfo info) {
        final LightminMetricJobExecutionEvent lightminJobExecutionEvent = new LightminMetricJobExecutionEvent(info);
        this.applicationEventPublisher.publishEvent(lightminJobExecutionEvent);
    }

    public void publishMetricEvent(final StepExecutionEventInfo info) {
        final LightminMetricStepExecutionEvent lightminStepExecutionEvent = new LightminMetricStepExecutionEvent(info);
        this.applicationEventPublisher.publishEvent(lightminStepExecutionEvent);
    }


    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
