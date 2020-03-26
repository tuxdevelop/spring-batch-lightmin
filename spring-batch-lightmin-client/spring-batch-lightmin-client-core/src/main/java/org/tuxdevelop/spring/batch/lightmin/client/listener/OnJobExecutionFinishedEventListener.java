package org.tuxdevelop.spring.batch.lightmin.client.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationListener;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.JobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.MetricEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.StepExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.event.EventTransformer;
import org.tuxdevelop.spring.batch.lightmin.event.JobExecutionEvent;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class OnJobExecutionFinishedEventListener implements ApplicationListener<JobExecutionEvent> {

    private final JobExecutionEventPublisher jobExecutionEventPublisher;

    private final StepExecutionEventPublisher stepExecutionEventPublisher;

    private final MetricEventPublisher metricEventPublisher;

    public OnJobExecutionFinishedEventListener(final JobExecutionEventPublisher jobExecutionEventPublisher, final StepExecutionEventPublisher stepExecutionEventPublisher, MetricEventPublisher metricEventPublisher) {
        this.jobExecutionEventPublisher = jobExecutionEventPublisher;
        this.stepExecutionEventPublisher = stepExecutionEventPublisher;
        this.metricEventPublisher = metricEventPublisher;
    }


    @Override
    public void onApplicationEvent(final JobExecutionEvent jobExecutionEvent) {
        final JobExecution jobExecution = jobExecutionEvent.getJobExecution();
        if (jobExecution != null) {
            final ExitStatus exitStatus = jobExecution.getExitStatus();
            if (exitStatus != null) {
                log.info(jobExecution.getJobInstance().getJobName() + ", Status: " + jobExecution.getStatus().getBatchStatus() +
                        ", Exit: " + jobExecution.getExitStatus().getExitCode());
                final JobExecutionEventInfo jobExecutionEventInfo =
                        EventTransformer.transformToJobExecutionEventInfo(
                                jobExecution,
                                jobExecutionEvent.getApplicationName());
                this.jobExecutionEventPublisher.publishEvent(jobExecutionEventInfo);
                this.metricEventPublisher.publishMetricEvent(jobExecutionEventInfo);
                jobExecution.getStepExecutions()
                        .stream()
                        .map(step -> EventTransformer.transformToStepExecutionEventInfo(step, jobExecutionEvent.getApplicationName()))
                        .forEach(stepInfo -> {
                            this.stepExecutionEventPublisher.publishEvent(stepInfo);
                            this.metricEventPublisher.publishMetricEvent(stepInfo);
                        });
            } else {
                log.debug("could not fire JobExcutionEvent, exitStatus was null");
            }
        } else {
            log.debug("could not fire JobExcutionEvent, jobExecution was null");
        }
    }

}
