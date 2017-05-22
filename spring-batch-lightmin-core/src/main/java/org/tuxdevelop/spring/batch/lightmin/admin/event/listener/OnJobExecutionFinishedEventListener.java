package org.tuxdevelop.spring.batch.lightmin.admin.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.tuxdevelop.spring.batch.lightmin.admin.event.JobExecutionEvent;
import org.tuxdevelop.spring.batch.lightmin.admin.event.JobExecutionFailedEvent;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class OnJobExecutionFinishedEventListener implements ApplicationListener<JobExecutionEvent> {

    private final ApplicationEventPublisher applicationEventPublisher;

    public OnJobExecutionFinishedEventListener(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onApplicationEvent(final JobExecutionEvent jobExecutionEvent) {
        final JobExecution jobExecution = jobExecutionEvent.getJobExecution();
        if (jobExecution != null) {
            final ExitStatus exitStatus = jobExecution.getExitStatus();
            if (exitStatus != null) {
                if (ExitStatus.FAILED.getExitCode().equals(exitStatus.getExitCode())) {
                    this.applicationEventPublisher.publishEvent(new JobExecutionFailedEvent(jobExecution));
                }
            } else {
                log.debug("could not fire JobExcutionEvent, exitStatus was null");
            }
        } else {
            log.debug("could not fire JobExcutionEvent, jobExecution was null");
        }
    }
}
