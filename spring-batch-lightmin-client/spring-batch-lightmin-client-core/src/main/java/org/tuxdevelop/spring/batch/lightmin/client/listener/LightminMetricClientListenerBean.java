package org.tuxdevelop.spring.batch.lightmin.client.listener;

import org.springframework.context.event.EventListener;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.event.LightminJobExecutionEvent;
import org.tuxdevelop.spring.batch.lightmin.client.event.LightminMetricJobExecutionEvent;
import org.tuxdevelop.spring.batch.lightmin.client.event.LightminMetricStepExecutionEvent;
import org.tuxdevelop.spring.batch.lightmin.client.event.LightminStepExecutionEvent;
import org.tuxdevelop.spring.batch.lightmin.service.MetricService;
import org.tuxdevelop.spring.batch.lightmin.utils.LightminMetricSource;


public class LightminMetricClientListenerBean {

    private final MetricService metricService;

    public LightminMetricClientListenerBean(final MetricService metricService) {
        this.metricService = metricService;
    }

    @EventListener(classes = LightminMetricJobExecutionEvent.class)
    public void measureJobExecution(final LightminJobExecutionEvent lightminMetricJobExecutionEvent) {
        this.metricService.measureJobExecution(LightminMetricSource.CLIENT, (JobExecutionEventInfo) lightminMetricJobExecutionEvent.getSource());
    }

    @EventListener(classes = LightminMetricStepExecutionEvent.class)
    public void measureStepExecution(final LightminStepExecutionEvent lightminMetricStepExecutionEvent) {
        this.metricService.measureStepExecution(LightminMetricSource.CLIENT, (StepExecutionEventInfo) lightminMetricStepExecutionEvent.getSource());
    }

}
