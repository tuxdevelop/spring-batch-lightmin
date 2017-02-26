package org.tuxdevelop.spring.batch.lightmin.server.admin;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminProperties;
import org.tuxdevelop.spring.batch.lightmin.client.registration.LightminClientRegistrator;
import org.tuxdevelop.spring.batch.lightmin.server.ITConfigurationApplication;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.test.configuration.ITRemoteConfiguration;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ITConfigurationApplication.class, ITRemoteConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RemoteAdminServerServiceIT extends AdminServerServiceIT {

    @Autowired
    private AdminServerService adminServerService;
    @Autowired
    private LightminApplicationRepository lightminApplicationRepository;
    @Autowired
    private LightminClientRegistrator lightminClientRegistrator;
    @Autowired
    private LightminClientProperties lightminClientProperties;
    @Autowired
    private LightminProperties lightminProperties;
    @LocalServerPort
    private Integer serverPort;

    @Override
    public AdminServerService getAdminServerService() {
        assertThat(this.adminServerService instanceof RemoteAdminServerService).isTrue();
        return this.adminServerService;
    }

    @Override
    public LightminClientApplication createLightminClientApplication() {
        final Collection<LightminClientApplication> allApplications = this.lightminApplicationRepository.findAll();
        assertThat(allApplications).hasSize(1);
        return allApplications.iterator().next();
    }

    @Before
    public void init() {
        this.lightminClientProperties.setServiceUrl("http://localhost:" + this.serverPort);
        this.lightminClientProperties.setServerPort(this.serverPort);
        this.lightminClientProperties.setManagementPort(this.serverPort);
        this.lightminProperties.setUrl(new String[]{"http://localhost:" + this.serverPort});
        this.lightminClientRegistrator.register();
    }
}
