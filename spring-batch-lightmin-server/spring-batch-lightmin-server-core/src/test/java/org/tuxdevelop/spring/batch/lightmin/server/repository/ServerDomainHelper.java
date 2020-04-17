package org.tuxdevelop.spring.batch.lightmin.server.repository;

import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplicationStatus;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientInformation;

import java.util.UUID;

public final class ServerDomainHelper {

    private ServerDomainHelper() {
    }

    public static LightminClientApplication createLightminClientApplication(final String name) {

        final LightminClientApplication application = new LightminClientApplication();
        application.setId(UUID.randomUUID().toString());
        application.setName(name);
        application.setServiceUrl("http://test.com:8180");
        application.setManagementUrl("http://test.com:8180");
        application.setHealthUrl("http://test.com:8180/health");
        application.setLightminClientInformation(new LightminClientInformation());
        application.setLightminClientApplicationStatus(LightminClientApplicationStatus.ofUp());

        return application;
    }

}
