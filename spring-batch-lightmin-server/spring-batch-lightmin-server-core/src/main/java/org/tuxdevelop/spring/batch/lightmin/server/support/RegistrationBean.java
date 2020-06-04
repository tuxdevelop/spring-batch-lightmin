package org.tuxdevelop.spring.batch.lightmin.server.support;

import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface RegistrationBean {

    LightminClientApplication register(final LightminClientApplication lightminClientApplication);

    LightminClientApplication deleteRegistration(final String applicationId);

    LightminClientApplication findById(final String applicationId);

    Collection<LightminClientApplication> findAll();

    String getIdByApplicationName(final String applicationName);

    void clear();

    Map<String, Set<LightminClientApplication>> findAllAsMap();

    String getApplicationNameById(final String applicationInstanceId);

    Set<String> getAllApplicationNames();

    String determineApplicationId(final LightminClientApplication lightminClientApplication);
}
