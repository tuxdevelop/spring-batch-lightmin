package org.tuxdevelop.spring.batch.lightmin.server.event;


import lombok.Getter;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public enum EventType {

    ADD_REGISTRATION("ADD_REGISTRATION"),
    DELETE_REGISTRATION("DELETE_REGISTRATION"),
    CHANGE_REGISTRATION("CHANGE_REGISTRATION");

    @Getter
    private final String description;

    EventType(final String description) {
        this.description = description;
    }
}
