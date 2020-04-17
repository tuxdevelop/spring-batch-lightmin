package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;


import lombok.Getter;

import java.io.Serializable;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public enum JobIncrementer implements Serializable {

    DATE("DATE_INCREMENTER"),
    NONE("NONE");

    @Getter
    private final String incrementerIdentifier;

    JobIncrementer(final String incrementerIdentifier) {
        this.incrementerIdentifier = incrementerIdentifier;
    }
}
