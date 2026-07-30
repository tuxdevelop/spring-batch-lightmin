package org.tuxdevelop.spring.batch.lightmin.batch.dao;

import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

/**
 * @author Marcel Becker
 * @since 0.5
 */
final class DaoUtil {

    static Date castDate(final Object input) {
        if (input instanceof Date) {
            return (Date) input;
        }
        if (input instanceof Number) {
            return new Date(((Number) input).longValue());
        }
        if (input instanceof String) {
            final String value = (String) input;
            try {
                return Date.from(Instant.parse(value));
            } catch (final DateTimeParseException ignored) {
                try {
                    return Date.from(LocalDateTime.parse(value).atZone(ZoneId.systemDefault()).toInstant());
                } catch (final DateTimeParseException ignoredAgain) {
                    throw new SpringBatchLightminApplicationException("could not parse date value " + value);
                }
            }
        }
        throw new SpringBatchLightminApplicationException("could not parse date for input class " + input.getClass());
    }
}
