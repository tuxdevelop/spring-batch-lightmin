package org.tuxdevelop.spring.batch.lightmin.exception;

/**
 * @author Marcel Becker
 * @since 0.1
 */
public class SpringBatchLightminApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SpringBatchLightminApplicationException(final String message) {
        super(message);
    }

    public SpringBatchLightminApplicationException(final Throwable t, final String message) {
        super(message, t);
    }
}