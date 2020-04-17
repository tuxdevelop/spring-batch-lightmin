package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;


import lombok.Getter;

import java.io.Serializable;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public enum ListenerStatus implements Serializable {

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
            throw new IllegalArgumentException("Unknown ListenerStatus : " + value);
        }

        return listenerStatus;
    }
}
