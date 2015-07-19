package org.tuxdevelop.spring.batch.lightmin.admin.domain;

import lombok.Getter;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

public enum JobIncrementer {

    DATE("DATE_INCREMENTER");

    @Getter
    private String incrementerIdentifier;

    private JobIncrementer(final String incrementerIdentifier) {
        this.incrementerIdentifier = incrementerIdentifier;
    }

    public static JobIncrementer getByIdentifier(final String incrementerIdentifier) {
        final JobIncrementer jobIncrementer;
        if (JobIncrementer.DATE.getIncrementerIdentifier().equals(incrementerIdentifier)) {
            jobIncrementer = DATE;
        } else {
            throw new SpringBatchLightminApplicationException(
                    "Unknown JobIncrementer for identifier: " + incrementerIdentifier);
        }
        return jobIncrementer;
    }
}
