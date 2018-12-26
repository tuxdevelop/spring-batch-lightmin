package org.tuxdevelop.spring.batch.lightmin.domain;

import lombok.Getter;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

/**
 * @author Marcel Becker
 * @version 0.1
 */
public enum TaskExecutorType {

    SYNCHRONOUS("SYNCHRONOUS", 1L),
    ASYNCHRONOUS("ASYNCHRONOUS", 2L);

    @Getter
    private final String value;
    @Getter
    private final Long id;

    TaskExecutorType(final String value, final Long id) {
        this.value = value;
        this.id = id;
    }

    public static TaskExecutorType getById(final Long id) {
        final TaskExecutorType type;
        if (SYNCHRONOUS.getId().equals(id)) {
            type = SYNCHRONOUS;
        } else if (ASYNCHRONOUS.getId().equals(id)) {
            type = ASYNCHRONOUS;
        } else {
            throw new SpringBatchLightminApplicationException("Unknown TaskExecutorType for id: " + id);
        }
        return type;
    }
}
