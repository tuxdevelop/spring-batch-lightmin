package org.tuxdevelop.spring.batch.lightmin.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Slf4j
public class MapLightminApplicationRepository implements LightminApplicationRepository {

    private final ConcurrentMap<String, LightminClientApplication> applications = new ConcurrentHashMap<>();


    @Override
    public LightminClientApplication save(final LightminClientApplication lightminClientApplication) {
        return this.applications.put(lightminClientApplication.getId(), lightminClientApplication);
    }

    @Override
    public Collection<LightminClientApplication> findAll() {
        return this.applications.values();
    }

    @Override
    public LightminClientApplication find(final String id) {
        return this.applications.get(id);
    }

    @Override
    public Collection<LightminClientApplication> findByApplicationName(final String applicationName) {
        final Collection<LightminClientApplication> allApplications = this.findAll();
        final Collection<LightminClientApplication> lightminClientApplications = new ArrayList<>();
        for (final LightminClientApplication application : allApplications) {
            if (application.getName().equals(applicationName)) {
                lightminClientApplications.add(application);
            } else {
                log.trace("Skipping");
            }
        }
        return lightminClientApplications;
    }

    @Override
    public LightminClientApplication delete(final String id) {
        return this.applications.remove(id);
    }

    @Override
    public void clear() {
        this.applications.clear();
    }
}
