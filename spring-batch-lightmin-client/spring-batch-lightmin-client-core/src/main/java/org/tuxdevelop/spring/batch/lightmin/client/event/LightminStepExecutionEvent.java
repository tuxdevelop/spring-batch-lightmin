package org.tuxdevelop.spring.batch.lightmin.client.event;

import org.springframework.context.ApplicationEvent;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;

public class LightminStepExecutionEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public LightminStepExecutionEvent(StepExecutionEventInfo source) {
        super(source);
    }
}
