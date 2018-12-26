package org.tuxdevelop.spring.batch.lightmin.server.repository;

import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

import java.util.Collection;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public interface LightminApplicationRepository {


    /**
     * @param lightminClientApplication
     * @return
     */
    LightminClientApplication save(final LightminClientApplication lightminClientApplication);

    /**
     * @return
     */
    Collection<LightminClientApplication> findAll();

    /**
     * @param id
     * @return
     */
    LightminClientApplication find(final String id);

    /**
     * @param id
     * @return
     */
    LightminClientApplication delete(final String id);

    /**
     *
     */
    void clear();


}
