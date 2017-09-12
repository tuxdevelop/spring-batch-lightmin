package org.tuxdevelop.spring.batch.lightmin.server.service.embedded;

import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationListener;
import org.tuxdevelop.spring.batch.lightmin.admin.event.EventTransformer;
import org.tuxdevelop.spring.batch.lightmin.admin.event.JobExecutionEvent;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public class EmbeddedJobExecutionEventListener implements ApplicationListener<JobExecutionEvent> {

    private final EventService eventService;

    public EmbeddedJobExecutionEventListener(final EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void onApplicationEvent(final JobExecutionEvent jobExecutionEvent) {
        final JobExecution jobExecution = jobExecutionEvent.getJobExecution();
        final JobExecutionEventInfo jobExecutionEventInfo = EventTransformer.transformToJobExecutionEventInfo(jobExecution, jobExecutionEvent.getApplicationName());
        this.eventService.handleJobExecutionEvent(jobExecutionEventInfo);
    }
}
