package org.tuxdevelop.spring.batch.lightmin.client.event;

import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;

public interface JobExecutionEventPublisher {

    void publishJobExecutionEvent(final JobExecutionEventInfo jobExecutionEventInfo);
}
