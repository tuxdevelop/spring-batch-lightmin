package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception;

public class SchedulerExecutionNotFoundException extends Exception {

    public SchedulerExecutionNotFoundException(final String message) {
        super(message);
    }

    public SchedulerExecutionNotFoundException(final String message, final Throwable t) {
        super(message, t);
    }
}
