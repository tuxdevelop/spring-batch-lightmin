package org.tuxdevelop.spring.batch.lightmin.server.repository;

import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public class MapLightminApplicationRepository implements LightminApplicationRepository {

    private final ConcurrentMap<String, LightminClientApplication> applications = new ConcurrentHashMap<>();


    @Override
    public LightminClientApplication save(final LightminClientApplication lightminClientApplication) {
        return applications.put(lightminClientApplication.getId(), lightminClientApplication);
    }

    @Override
    public Collection<LightminClientApplication> findAll() {
        return applications.values();
    }

    @Override
    public LightminClientApplication find(final String id) {
        return applications.get(id);
    }

    @Override
    public LightminClientApplication delete(final String id) {
        return applications.remove(id);
    }

    @Override
    public void clear() {
        applications.clear();
    }
}
