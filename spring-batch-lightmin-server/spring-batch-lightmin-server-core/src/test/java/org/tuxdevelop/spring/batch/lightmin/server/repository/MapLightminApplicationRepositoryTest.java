package org.tuxdevelop.spring.batch.lightmin.server.repository;

public class MapLightminApplicationRepositoryTest extends LightminApplicationRepositoryTest {

    private final MapLightminApplicationRepository repository = new MapLightminApplicationRepository();

    @Override
    protected LightminApplicationRepository getLightminApplicationRepository() {
        return this.repository;
    }
}
