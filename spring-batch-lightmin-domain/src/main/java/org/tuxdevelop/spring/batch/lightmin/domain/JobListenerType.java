package org.tuxdevelop.spring.batch.lightmin.domain;

import lombok.Getter;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public enum JobListenerType {

    LOCAL_FOLDER_LISTENER(1L);

    @Getter
    private final Long id;

    JobListenerType(final Long id) {
        this.id = id;
    }

    public static JobListenerType getById(final Long id) {
        final JobListenerType type;
        if (LOCAL_FOLDER_LISTENER.getId().equals(id)) {
            type = LOCAL_FOLDER_LISTENER;
        } else {
            throw new SpringBatchLightminConfigurationException("Unknown id for JobSchedulerConfiguration:" + id);
        }
        return type;
    }
}
