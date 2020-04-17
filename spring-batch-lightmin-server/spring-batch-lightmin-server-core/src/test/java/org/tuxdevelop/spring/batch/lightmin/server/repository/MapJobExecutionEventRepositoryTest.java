package org.tuxdevelop.spring.batch.lightmin.server.repository;

import org.junit.Before;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.LightminServerCoreProperties;

public class MapJobExecutionEventRepositoryTest extends JobExecutionEventRepositoryTest {

    private final LightminServerCoreProperties properties = new LightminServerCoreProperties();

    private JobExecutionEventRepository jobExecutionEventRepository;


    @Override
    @Before
    public void init() {
        this.jobExecutionEventRepository =
                new MapJobExecutionEventRepository(this.properties.getEventRepositorySize());
        super.init();
    }

    @Override
    protected JobExecutionEventRepository getJobExecutionEventRepository() {
        return this.jobExecutionEventRepository;
    }

    @Override
    protected LightminServerCoreProperties getLightminServerCoreProperties() {
        return this.properties;
    }
}
