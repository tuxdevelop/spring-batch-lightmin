package org.tuxdevelop.spring.batch.lightmin.server.service;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.classic.configuration.LightminClientClassicConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.client.classic.service.LightminClientRegistratorService;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.test.configuration.ITRemoteConfiguration;
import org.tuxdevelop.test.configuration.ITServerConfigurationApplication;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ITServerConfigurationApplication.class, ITRemoteConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RemoteJobServerServiceIT extends JobServerServiceIT {

    @Autowired
    private JobServerService jobServerService;
    @Autowired
    private LightminApplicationRepository lightminApplicationRepository;
    @Autowired
    private LightminClientRegistratorService lightminClientRegistratorService;
    @Autowired
    private LightminClientProperties lightminClientProperties;
    @Autowired
    private LightminClientClassicConfigurationProperties lightminClientClassicConfigurationProperties;

    @LocalServerPort
    private Integer port;

    @Override
    public JobServerService getJobServerService() {
        assertThat(this.jobServerService instanceof RemoteJobServerService).isTrue();
        return this.jobServerService;
    }

    @Override
    public LightminClientApplication createLightminClientApplication() {
        final Collection<LightminClientApplication> allApplications = this.lightminApplicationRepository.findAll();
        assertThat(allApplications).hasSize(1);
        return allApplications.iterator().next();
    }

    @Override
    @Before
    public void init() {
        super.init();
        this.lightminClientProperties.setServiceUrl("http://localhost:" + port);
        this.lightminClientProperties.setServerPort(port);
        this.lightminClientProperties.setManagementPort(port);
        this.lightminClientClassicConfigurationProperties.getServer().setUrl(new String[]{"http://localhost:" + port});
        this.lightminClientRegistratorService.register();
    }
}
