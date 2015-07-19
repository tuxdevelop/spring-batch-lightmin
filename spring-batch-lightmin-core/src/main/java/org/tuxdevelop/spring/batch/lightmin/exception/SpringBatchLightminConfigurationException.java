package org.tuxdevelop.spring.batch.lightmin.exception;

public class SpringBatchLightminConfigurationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SpringBatchLightminConfigurationException(final String message) {
        super(message);
    }

    public SpringBatchLightminConfigurationException(final Throwable t, final String message) {
        super(message, t);
    }

}
