package org.tuxdevelop.spring.batch.lightmin.domain;


import lombok.Getter;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

public enum ListenerStatus {

    ACTIVE("ACTIVE"),
    STOPPED("STOPPED");

    @Getter
    private final String value;

    ListenerStatus(final String value) {
        this.value = value;
    }

    public static ListenerStatus getByValue(final String value) {
        final ListenerStatus listenerStatus;
        if (ACTIVE.getValue().equals(value)) {
            listenerStatus = ACTIVE;
        } else if (STOPPED.getValue().equals(value)) {
            listenerStatus = STOPPED;
        } else {
            throw new SpringBatchLightminApplicationException("Unknown ListenerStatus : " + value);
        }

        return listenerStatus;
    }
}
