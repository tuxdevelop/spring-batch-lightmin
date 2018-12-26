package org.tuxdevelop.spring.batch.lightmin.client.service;

import org.springframework.batch.core.configuration.JobRegistry;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;

import java.util.LinkedList;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public class LightminClientApplicationService {

    private final JobRegistry jobRegistry;
    private final LightminClientProperties lightminClientProperties;

    public LightminClientApplicationService(final JobRegistry jobRegistry,
                                            final LightminClientProperties lightminClientProperties) {
        this.jobRegistry = jobRegistry;
        this.lightminClientProperties = lightminClientProperties;
    }

    public LightminClientApplication getLightminClientApplication() {
        return LightminClientApplication.
                createApplication(new LinkedList<>(this.jobRegistry.getJobNames()), this.lightminClientProperties);
    }
}
