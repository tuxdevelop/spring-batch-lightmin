package org.tuxdevelop.spring.batch.lightmin.server.support;

import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;

public class UrlApplicationRegistrationBean extends CommonRegistrationBean {

    public UrlApplicationRegistrationBean(final LightminApplicationRepository lightminApplicationRepository) {
        super(lightminApplicationRepository);
    }

    @Override
    public String determineApplicationId(final LightminClientApplication lightminClientApplication) {
        return ApplicationUrlIdGenerator.generateId(lightminClientApplication);
    }
}
