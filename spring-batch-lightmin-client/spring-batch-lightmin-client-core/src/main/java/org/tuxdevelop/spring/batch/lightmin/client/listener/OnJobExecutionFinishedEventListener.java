package org.tuxdevelop.spring.batch.lightmin.client.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationListener;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.event.JobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.event.EventTransformer;
import org.tuxdevelop.spring.batch.lightmin.event.JobExecutionEvent;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class OnJobExecutionFinishedEventListener implements ApplicationListener<JobExecutionEvent> {
    private final JobExecutionEventPublisher jobExecutionEventPublisher;

    public OnJobExecutionFinishedEventListener(final JobExecutionEventPublisher jobExecutionEventPublisher) {
        this.jobExecutionEventPublisher = jobExecutionEventPublisher;
    }

    @Override
    public void onApplicationEvent(final JobExecutionEvent jobExecutionEvent) {
        final JobExecution jobExecution = jobExecutionEvent.getJobExecution();
        if (jobExecution != null) {
            final ExitStatus exitStatus = jobExecution.getExitStatus();
            if (exitStatus != null) {
                final JobExecutionEventInfo jobExecutionEventInfo =
                        EventTransformer.transformToJobExecutionEventInfo(
                                jobExecution,
                                jobExecutionEvent.getApplicationName());
                this.jobExecutionEventPublisher.publishJobExecutionEvent(jobExecutionEventInfo);
            } else {
                log.debug("could not fire JobExcutionEvent, exitStatus was null");
            }
        } else {
            log.debug("could not fire JobExcutionEvent, jobExecution was null");
        }
    }

}
