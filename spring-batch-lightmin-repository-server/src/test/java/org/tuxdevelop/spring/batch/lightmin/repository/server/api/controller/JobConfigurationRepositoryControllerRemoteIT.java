package org.tuxdevelop.spring.batch.lightmin.repository.server.api.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.repository.RemoteJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.repository.RemoteJobConfigurationRepositoryLocator;
import org.tuxdevelop.spring.batch.lightmin.repository.configuration.RemoteJobConfigurationRepositoryConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.test.util.ITJobConfigurationRepository;
import org.tuxdevelop.test.configuration.remote.RemoteIntegrationTestConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RemoteIntegrationTestConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource(value = {"classpath:application.properties"})
public class JobConfigurationRepositoryControllerRemoteIT extends JobConfigurationRepositoryControllerIT {

    @Autowired
    private RemoteJobConfigurationRepositoryConfigurationProperties properties;
    @Autowired
    private ITJobConfigurationRepository itJobConfigurationRepository;
    @Autowired
    private RemoteJobConfigurationRepositoryLocator remoteJobConfigurationRepositoryLocator;
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
        this.properties.setServerUrl("http://localhost:" + this.localPort);
        this.jobConfigurationRepository = new RemoteJobConfigurationRepository(
                this.properties,
                new RestTemplate(),
                this.remoteJobConfigurationRepositoryLocator);
    }

    @Override
    public JobConfigurationRepository getJobConfigurationRepository() {
        return this.jobConfigurationRepository;
    }
}
