package org.tuxdevelop.spring.batch.lightmin.exception;


/**
 * @author Marcel Becker
 * @since 0.1
 */
public class NoSuchJobException extends Exception {

    private static final long serialVersionUID = 1L;

    public NoSuchJobException(final String message) {
        super(message);
    }

    public NoSuchJobException(final Throwable t, final String message) {
        super(message, t);
    }
}
