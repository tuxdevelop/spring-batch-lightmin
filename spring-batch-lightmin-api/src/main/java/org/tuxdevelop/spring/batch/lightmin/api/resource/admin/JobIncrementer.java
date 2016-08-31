package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;


import lombok.Getter;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public enum JobIncrementer {

    DATE("DATE_INCREMENTER"),
    NONE("NONE");

    @Getter
    private String incrementerIdentifier;

    JobIncrementer(final String incrementerIdentifier) {
        this.incrementerIdentifier = incrementerIdentifier;
    }
}
