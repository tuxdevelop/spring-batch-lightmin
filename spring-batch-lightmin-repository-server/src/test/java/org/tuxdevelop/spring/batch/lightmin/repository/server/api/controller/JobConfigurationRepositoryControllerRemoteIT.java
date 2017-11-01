package org.tuxdevelop.spring.batch.lightmin.repository.server.api.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.RemoteJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.test.util.ITJobConfigurationRepository;
import org.tuxdevelop.test.configuration.RemoteIntegrationTestConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RemoteIntegrationTestConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource(value = {"classpath:application.properties"})
public class JobConfigurationRepositoryControllerRemoteIT extends JobConfigurationRepositoryControllerIT {

    @Autowired
    private SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;
    @Autowired
    private ITJobConfigurationRepository itJobConfigurationRepository;
    @LocalServerPort
    private Integer localPort;
    private JobConfigurationRepository jobConfigurationRepository;

    @Override
    public ITJobConfigurationRepository getITItJdbcJobConfigurationRepository() {
        return this.itJobConfigurationRepository;
    }

    @Override
    @Before
    public void init() {
        super.init();
        this.springBatchLightminConfigurationProperties.setRemoteRepositoryServerUrl("http://localhost:" + this.localPort);
        this.jobConfigurationRepository = new RemoteJobConfigurationRepository(this.springBatchLightminConfigurationProperties);
    }

    @Override
    public JobConfigurationRepository getJobConfigurationRepository() {
        return this.jobConfigurationRepository;
    }
}
