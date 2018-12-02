package org.tuxdevelop.spring.batch.lightmin.repository;

import lombok.Getter;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.Date;

/**
 * @author Marcel Becker
 * @since 0.1
 */
enum ParameterType {

    STRING(1L, String.class),
    LONG(2L, Long.class),
    DATE(3L, Date.class),
    DOUBLE(4L, Double.class);

    @Getter
    private final Long id;
    @Getter
    private final Class<?> clazz;

    ParameterType(final Long id, final Class<?> clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    public static ParameterType getById(final Long id) {
        if (STRING.getId().equals(id)) {
            return STRING;
        } else if (LONG.getId().equals(id)) {
            return LONG;
        } else if (DATE.getId().equals(id)) {
            return DATE;
        } else if (DOUBLE.getId().equals(id)) {
            return DOUBLE;
        } else {
            throw new SpringBatchLightminApplicationException("Unknown ParameterType for id: " + id);
        }
    }
}
