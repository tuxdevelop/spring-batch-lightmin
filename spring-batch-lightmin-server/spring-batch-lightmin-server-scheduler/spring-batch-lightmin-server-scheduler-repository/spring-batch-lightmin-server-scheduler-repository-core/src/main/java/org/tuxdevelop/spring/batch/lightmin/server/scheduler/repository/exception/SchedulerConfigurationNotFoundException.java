package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception;

public class SchedulerConfigurationNotFoundException extends Exception {

    public SchedulerConfigurationNotFoundException(final String message) {
        super(message);
    }

    public SchedulerConfigurationNotFoundException(final String message, final Throwable t) {
        super(message, t);
    }
}
