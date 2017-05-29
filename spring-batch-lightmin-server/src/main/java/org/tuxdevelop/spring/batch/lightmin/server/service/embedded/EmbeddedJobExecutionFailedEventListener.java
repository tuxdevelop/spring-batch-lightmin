package org.tuxdevelop.spring.batch.lightmin.server.service.embedded;

import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationListener;
import org.tuxdevelop.spring.batch.lightmin.admin.event.EventTransformer;
import org.tuxdevelop.spring.batch.lightmin.admin.event.JobExecutionFailedEvent;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public class EmbeddedJobExecutionFailedEventListener implements ApplicationListener<JobExecutionFailedEvent> {

    private final EventService eventService;

    public EmbeddedJobExecutionFailedEventListener(final EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void onApplicationEvent(final JobExecutionFailedEvent jobExecutionFailedEvent) {
        final JobExecution jobExecution = jobExecutionFailedEvent.getJobExecution();
        final JobExecutionEventInfo jobExecutionEventInfo = EventTransformer.transformToJobExecutionEventInfo(jobExecution, jobExecutionFailedEvent.getApplicationName());
        this.eventService.handleJobExecutionFailedEvent(jobExecutionEventInfo);
    }
}
