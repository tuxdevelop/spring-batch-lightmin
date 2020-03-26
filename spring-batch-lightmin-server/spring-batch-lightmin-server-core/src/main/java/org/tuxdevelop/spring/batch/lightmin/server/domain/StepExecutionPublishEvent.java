package org.tuxdevelop.spring.batch.lightmin.server.domain;

import org.springframework.context.ApplicationEvent;

public class StepExecutionPublishEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public StepExecutionPublishEvent(Object source) {
        super(source);

    }
}
