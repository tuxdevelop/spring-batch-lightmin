package org.tuxdevelop.spring.batch.lightmin.server.domain;

import org.springframework.context.ApplicationEvent;

public class JobExecutionPublishEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public JobExecutionPublishEvent(Object source) {
        super(source);

    }
}
