package org.tuxdevelop.spring.batch.lightmin.execption;


public class NoSuchJobConfigurationException extends Exception {

    private static final long serialVersionUID = 1L;

    public NoSuchJobConfigurationException(final String message) {
        super(message);
    }

    public NoSuchJobConfigurationException(final Throwable t, final String message) {
        super(message, t);
    }
}
