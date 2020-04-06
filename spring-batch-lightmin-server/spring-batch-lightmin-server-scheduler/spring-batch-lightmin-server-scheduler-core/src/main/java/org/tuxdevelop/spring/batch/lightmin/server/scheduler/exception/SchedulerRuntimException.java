package org.tuxdevelop.spring.batch.lightmin.server.scheduler.exception;

public class SchedulerRuntimException extends RuntimeException {

    private static final long serialVersionUID = -7027456231286013433L;

    public SchedulerRuntimException(final String message, final Throwable t) {
        super(message, t);
    }

    public SchedulerRuntimException(final String message) {
        super(message);
    }

    public SchedulerRuntimException(final Throwable t) {
        super(t);
    }
}

