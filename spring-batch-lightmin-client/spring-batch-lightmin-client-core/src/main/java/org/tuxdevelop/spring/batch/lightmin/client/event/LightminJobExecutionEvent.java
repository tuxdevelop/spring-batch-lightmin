package org.tuxdevelop.spring.batch.lightmin.client.event;

import org.springframework.context.ApplicationEvent;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;

public class LightminJobExecutionEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public LightminJobExecutionEvent(JobExecutionEventInfo source) {
        super(source);
    }
}
