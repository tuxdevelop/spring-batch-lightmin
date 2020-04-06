package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception;

public class SchedulerRepositoryException extends RuntimeException {

    private static final long serialVersionUID = 2898598433587172695L;

    public SchedulerRepositoryException(final Exception e) {
        super(e);
    }

    public SchedulerRepositoryException(final String message) {
        super(message);
    }
}
