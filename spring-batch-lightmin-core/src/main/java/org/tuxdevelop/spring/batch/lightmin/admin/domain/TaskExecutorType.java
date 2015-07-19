package org.tuxdevelop.spring.batch.lightmin.admin.domain;

import lombok.Getter;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.Arrays;
import java.util.List;

public enum TaskExecutorType {

    SYNCHRONOUS("SYNCHRONOUS", 1L),
    ASYNCHRONOUS("ASYNCHRONOUS", 2L);

    @Getter
    private String value;
    @Getter
    private Long id;

    private TaskExecutorType(final String value, final Long id) {
        this.value = value;
        this.id = id;
    }

    public List<TaskExecutorType> getAll() {
        return Arrays.asList(TaskExecutorType.values());
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
