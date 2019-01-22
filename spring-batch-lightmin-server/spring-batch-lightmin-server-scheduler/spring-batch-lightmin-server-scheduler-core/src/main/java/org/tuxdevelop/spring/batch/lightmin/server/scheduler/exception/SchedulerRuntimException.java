package org.tuxdevelop.spring.batch.lightmin.server.scheduler.exception;

public class SchedulerRuntimException extends RuntimeException {

    public SchedulerRuntimException(final String message, final Throwable t) {
        super(message, t);
    }
}
