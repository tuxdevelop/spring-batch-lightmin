package org.tuxdevelop.spring.batch.lightmin.client.event.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationListener;
import org.tuxdevelop.spring.batch.lightmin.admin.event.EventTransformer;
import org.tuxdevelop.spring.batch.lightmin.admin.event.JobExecutionFailedEvent;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.event.JobExecutionEventPublisher;


public class OnJobExecutionFailedEventListener implements ApplicationListener<JobExecutionFailedEvent> {

    private final JobExecutionEventPublisher jobExecutionEventPublisher;

    public OnJobExecutionFailedEventListener(final JobExecutionEventPublisher jobExecutionEventPublisher) {
        this.jobExecutionEventPublisher = jobExecutionEventPublisher;
    }

    @Override
    public void onApplicationEvent(final JobExecutionFailedEvent jobExecutionFailedEvent) {
        final JobExecution jobExecution = jobExecutionFailedEvent.getJobExecution();
        final JobExecutionEventInfo jobExecutionEventInfo = EventTransformer.transformToJobExecutionEventInfo(jobExecution, jobExecutionFailedEvent.getApplicationName());
        this.jobExecutionEventPublisher.publishJobExecutionFailedEvent(jobExecutionEventInfo);
    }
}
