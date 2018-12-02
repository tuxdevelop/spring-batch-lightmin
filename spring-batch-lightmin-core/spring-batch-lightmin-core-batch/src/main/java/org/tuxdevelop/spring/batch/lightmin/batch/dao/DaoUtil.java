package org.tuxdevelop.spring.batch.lightmin.batch.dao;

import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.Date;

/**
 * @author Marcel Becker
 * @since 0.5
 */
final class DaoUtil {

    static Date castDate(final Object input) {
        final Date date;
        if (input instanceof Date) {
            date = (Date) input;
        } else if (input instanceof Long) {
            date = new Date((Long) input);
        } else {
            throw new SpringBatchLightminApplicationException("could not parse date for input class " + input.getClass());
        }
        return date;
    }
}
