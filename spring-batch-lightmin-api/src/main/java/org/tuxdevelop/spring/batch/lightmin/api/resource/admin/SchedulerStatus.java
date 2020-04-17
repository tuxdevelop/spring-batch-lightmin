package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public enum SchedulerStatus implements Serializable {

    INITIALIZED("INITIALIZED"),
    RUNNING("RUNNING"),
    STOPPED("STOPPED"),
    IN_TERMINATION("IN TERMINATION");

    @Getter
    private final String value;

    SchedulerStatus(final String value) {
        this.value = value;
    }

    public static SchedulerStatus getByValue(final String value) {
        final SchedulerStatus schedulerStatus;
        if (INITIALIZED.getValue().equals(value)) {
            schedulerStatus = INITIALIZED;
        } else if (RUNNING.getValue().equals(value)) {
            schedulerStatus = RUNNING;
        } else if (STOPPED.getValue().equals(value)) {
            schedulerStatus = STOPPED;
        } else if (IN_TERMINATION.getValue().equals(value)) {
            schedulerStatus = IN_TERMINATION;
        } else {
            throw new IllegalArgumentException("Unknown SchedulerStatus : " + value);
        }

        return schedulerStatus;
    }
}
