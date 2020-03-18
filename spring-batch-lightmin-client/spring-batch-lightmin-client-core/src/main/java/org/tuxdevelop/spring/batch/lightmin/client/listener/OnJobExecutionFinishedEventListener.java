package org.tuxdevelop.spring.batch.lightmin.client.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationListener;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.api.BatchToResourceMapper;
import org.tuxdevelop.spring.batch.lightmin.client.event.JobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.event.MetricEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.service.MetricService;
import org.tuxdevelop.spring.batch.lightmin.event.EventTransformer;
import org.tuxdevelop.spring.batch.lightmin.event.JobExecutionEvent;
import org.tuxdevelop.spring.batch.lightmin.utils.LightminMetricSource;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class OnJobExecutionFinishedEventListener implements ApplicationListener<JobExecutionEvent> {
    private final JobExecutionEventPublisher jobExecutionEventPublisher;

    private final MetricEventPublisher metricEventPublisher;

    private final MetricService metricService;

    public OnJobExecutionFinishedEventListener(final JobExecutionEventPublisher jobExecutionEventPublisher, final MetricEventPublisher metricEventPublisher, final MetricService metricService) {
        this.jobExecutionEventPublisher = jobExecutionEventPublisher;
        this.metricEventPublisher = metricEventPublisher;
        this.metricService = metricService;
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


                this.metricService.measureJobExecution(LightminMetricSource.CLIENT, jobExecutionEventInfo);
                this.metricEventPublisher.publishEvent(jobExecutionEventInfo);

                jobExecution.getStepExecutions()
                        .stream()
                        .map(step -> EventTransformer.transformToStepExecutionEventInfo(step, jobExecutionEvent.getApplicationName()))
                        .forEach(stepInfo -> {
                            this.metricService.measureStepExecution(LightminMetricSource.CLIENT, stepInfo);
                            this.metricEventPublisher.publishEvent(stepInfo);
                        });
            } else {
                log.debug("could not fire JobExcutionEvent, exitStatus was null");
            }
        } else {
            log.debug("could not fire JobExcutionEvent, jobExecution was null");
        }
    }
}
