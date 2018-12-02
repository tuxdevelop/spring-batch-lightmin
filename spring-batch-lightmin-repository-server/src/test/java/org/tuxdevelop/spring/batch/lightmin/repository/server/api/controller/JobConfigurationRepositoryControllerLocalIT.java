package org.tuxdevelop.spring.batch.lightmin.repository.server.api.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.test.util.ITJobConfigurationRepository;
import org.tuxdevelop.test.configuration.IntegrationTestConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
public class JobConfigurationRepositoryControllerLocalIT extends JobConfigurationRepositoryControllerIT {

    @Autowired
    private JobConfigurationRepositoryController jobConfigurationRepositoryController;
    @Autowired
    private ITJobConfigurationRepository itJobConfigurationRepository;

    @Override
    public ITJobConfigurationRepository getITItJdbcJobConfigurationRepository() {
        return this.itJobConfigurationRepository;
    }

    @Override
    public JobConfigurationRepository getJobConfigurationRepository() {
        return this.jobConfigurationRepositoryController;
    }
}
