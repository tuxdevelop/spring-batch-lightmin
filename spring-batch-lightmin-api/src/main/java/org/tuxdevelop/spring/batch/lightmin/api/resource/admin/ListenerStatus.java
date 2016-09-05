package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;


import lombok.Getter;

public enum ListenerStatus {

    ACTIVE("ACTIVE"),
    STOPPED("STOPPED");

    @Getter
    private String value;

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
