package org.tuxdevelop.spring.batch.lightmin.admin.event.listener;

import org.springframework.context.ApplicationListener;
import org.tuxdevelop.spring.batch.lightmin.admin.event.JobExecutionFailedEvent;


public class OnJobExecutionFailedEventListener implements ApplicationListener<JobExecutionFailedEvent> {

    @Override
    public void onApplicationEvent(final JobExecutionFailedEvent jobExecutionFailedEvent) {

    }
}
