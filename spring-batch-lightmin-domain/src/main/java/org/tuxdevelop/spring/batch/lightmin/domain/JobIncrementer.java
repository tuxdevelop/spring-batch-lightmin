package org.tuxdevelop.spring.batch.lightmin.domain;

import lombok.Getter;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

/**
 * @author Marcel Becker
 * @version 0.1
 */
public enum JobIncrementer {

    DATE("DATE_INCREMENTER"),
    NONE("NONE");


    @Getter
    private final String incrementerIdentifier;

    JobIncrementer(final String incrementerIdentifier) {
        this.incrementerIdentifier = incrementerIdentifier;
    }

    public static JobIncrementer getByIdentifier(final String incrementerIdentifier) {
        final JobIncrementer jobIncrementer;
        if (JobIncrementer.DATE.getIncrementerIdentifier().equals(incrementerIdentifier)) {
            jobIncrementer = DATE;
        } else if (JobIncrementer.NONE.getIncrementerIdentifier().equals(incrementerIdentifier)) {
            jobIncrementer = NONE;
        } else {
            throw new SpringBatchLightminApplicationException(
                    "Unknown JobIncrementer for identifier: " + incrementerIdentifier);
        }
        return jobIncrementer;
    }
}
