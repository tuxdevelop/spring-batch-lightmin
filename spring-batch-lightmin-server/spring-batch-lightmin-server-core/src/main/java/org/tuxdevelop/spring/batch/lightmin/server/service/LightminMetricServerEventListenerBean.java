package org.tuxdevelop.spring.batch.lightmin.server.service;


import org.springframework.context.event.EventListener;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.domain.JobExecutionPublishEvent;
import org.tuxdevelop.spring.batch.lightmin.server.domain.StepExecutionPublishEvent;
import org.tuxdevelop.spring.batch.lightmin.service.MetricService;
import org.tuxdevelop.spring.batch.lightmin.utils.LightminMetricSource;

public class LightminMetricServerEventListenerBean {

    private final MetricService metricServiceBean;


    public LightminMetricServerEventListenerBean(MetricService metricServiceBean) {
        this.metricServiceBean = metricServiceBean;
    }

    @EventListener(classes = JobExecutionPublishEvent.class)
    public void handleEvent(final JobExecutionPublishEvent event) {
        metricServiceBean.measureJobExecution(LightminMetricSource.SERVER, (JobExecutionEventInfo) event.getSource());
    }

    @EventListener(classes = StepExecutionPublishEvent.class)
    public void handleEvent(final StepExecutionPublishEvent event) {
        metricServiceBean.measureStepExecution(LightminMetricSource.SERVER, (StepExecutionEventInfo) event.getSource());
    }
}
