package org.tuxdevelop.spring.batch.lightmin.admin.repository;

import org.tuxdevelop.spring.batch.lightmin.test.util.ITJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.test.util.ITMapJobConfigurationRepository;

public class MapJobConfigurationRepositoryTest extends JobConfigurationRepositoryIT {

    private final MapJobConfigurationRepository jobConfigurationRepository = new MapJobConfigurationRepository();
    private final ITMapJobConfigurationRepository itMapJobConfigurationRepository = new ITMapJobConfigurationRepository();

    @Override
    JobConfigurationRepository getJobConfigurationRepository() {
        return jobConfigurationRepository;
    }

    @Override
    ITJobConfigurationRepository getITItJdbcJobConfigurationRepository() {
        return itMapJobConfigurationRepository;
    }
}
