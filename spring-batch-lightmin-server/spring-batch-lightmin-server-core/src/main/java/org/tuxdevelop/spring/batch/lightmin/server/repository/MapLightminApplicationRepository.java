package org.tuxdevelop.spring.batch.lightmin.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Slf4j
public class MapLightminApplicationRepository extends KVLightminApplicationRepository<ConcurrentHashMap<String, LightminClientApplication>> {

    public MapLightminApplicationRepository() {
        super(new ConcurrentHashMap<>());
    }

}
