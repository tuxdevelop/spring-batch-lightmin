package org.tuxdevelop.spring.batch.lightmin.repository;

import org.tuxdevelop.spring.batch.lightmin.repository.test.ITJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.repository.test.ITMapJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.repository.test.JobConfigurationRepositoryIT;

public class MapJobConfigurationRepositoryTest extends JobConfigurationRepositoryIT {

    private final MapJobConfigurationRepository jobConfigurationRepository = new MapJobConfigurationRepository();
    private final ITMapJobConfigurationRepository itMapJobConfigurationRepository = new ITMapJobConfigurationRepository();

    @Override
    public JobConfigurationRepository getJobConfigurationRepository() {
        return this.jobConfigurationRepository;
    }

    @Override
    public ITJobConfigurationRepository getITItJdbcJobConfigurationRepository() {
        return this.itMapJobConfigurationRepository;
    }
}
