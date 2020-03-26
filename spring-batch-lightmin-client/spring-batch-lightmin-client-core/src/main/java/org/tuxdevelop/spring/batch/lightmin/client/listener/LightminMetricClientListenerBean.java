package org.tuxdevelop.spring.batch.lightmin.client.listener;

import org.springframework.context.event.EventListener;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.event.LightminJobExecutionEvent;
import org.tuxdevelop.spring.batch.lightmin.client.event.LightminStepExecutionEvent;
import org.tuxdevelop.spring.batch.lightmin.service.MetricService;
import org.tuxdevelop.spring.batch.lightmin.utils.LightminMetricSource;


public class LightminMetricClientListenerBean {

    private final MetricService metricService;

    public LightminMetricClientListenerBean(MetricService metricService) {
        this.metricService = metricService;
    }

    @EventListener(classes = LightminJobExecutionEvent.class)
    public void measureJobExecution(LightminJobExecutionEvent lightminJobExecutionEvent) {
        metricService.measureJobExecution(LightminMetricSource.CLIENT, (JobExecutionEventInfo) lightminJobExecutionEvent.getSource());
    }

    @EventListener(classes = LightminStepExecutionEvent.class)
    public void measureStepExecution(LightminStepExecutionEvent lightminStepExecutionEvent) {
        metricService.measureStepExecution(LightminMetricSource.CLIENT, (StepExecutionEventInfo) lightminStepExecutionEvent.getSource());
    }

}
