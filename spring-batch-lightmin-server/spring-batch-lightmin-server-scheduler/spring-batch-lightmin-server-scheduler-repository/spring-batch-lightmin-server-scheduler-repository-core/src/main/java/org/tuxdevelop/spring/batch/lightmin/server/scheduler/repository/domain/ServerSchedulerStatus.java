package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain;

import lombok.Getter;

public enum ServerSchedulerStatus {


    ACTIVE("ACTIVE"),
    STOPPED("STOPPED");

    @Getter
    private final String value;

    ServerSchedulerStatus(final String value) {
        this.value = value;
    }

    public static ServerSchedulerStatus getByValue(final String value) {
        final ServerSchedulerStatus serverSchedulerStatus;
        if (ACTIVE.getValue().equals(value)) {
            serverSchedulerStatus = ACTIVE;
        } else if (STOPPED.getValue().equals(value)) {
            serverSchedulerStatus = STOPPED;
        } else {
            throw new IllegalArgumentException("Unknown ServerSchedulerStatus : " + value);
        }

        return serverSchedulerStatus;
    }
}
