package org.tuxdevelop.spring.batch.lightmin.domain;

import lombok.Getter;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

/**
 * @author Marcel Becker
 * @version 0.1
 */
public enum JobSchedulerType {

    CRON(1L),
    PERIOD(2L);

    @Getter
    private final Long id;

    JobSchedulerType(final Long id) {
        this.id = id;
    }

    public static JobSchedulerType getById(final Long id) {
        final JobSchedulerType type;
        if (CRON.getId().equals(id)) {
            type = CRON;
        } else if (PERIOD.getId().equals(id)) {
            type = PERIOD;
        } else {
            throw new SpringBatchLightminConfigurationException("Unknown id for JobSchedulerConfiguration:" + id);
        }
        return type;
    }

}
