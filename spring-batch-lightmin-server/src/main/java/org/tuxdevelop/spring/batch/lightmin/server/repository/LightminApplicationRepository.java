package org.tuxdevelop.spring.batch.lightmin.server.repository;

import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

import java.util.Collection;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public interface LightminApplicationRepository {


    LightminClientApplication save(final LightminClientApplication lightminClientApplication);

    Collection<LightminClientApplication> findAll();

    LightminClientApplication find(final String id);

    LightminClientApplication delete(final String id);

    void clear();


}
