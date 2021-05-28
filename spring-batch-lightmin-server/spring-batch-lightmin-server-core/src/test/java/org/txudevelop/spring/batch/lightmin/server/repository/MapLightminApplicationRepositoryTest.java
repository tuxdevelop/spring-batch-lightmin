package org.txudevelop.spring.batch.lightmin.server.repository;

import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.MapLightminApplicationRepository;

public class MapLightminApplicationRepositoryTest extends LightminApplicationRepositoryTest {

    private final MapLightminApplicationRepository repository = new MapLightminApplicationRepository();

    @Override
    protected LightminApplicationRepository getLightminApplicationRepository() {
        return this.repository;
    }
}
