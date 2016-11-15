package org.tuxdevelop.spring.batch.lightmin.server.admin;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminProperties;
import org.tuxdevelop.spring.batch.lightmin.client.registration.LightminClientRegistrator;
import org.tuxdevelop.spring.batch.lightmin.server.ITConfigurationApplication;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.test.configuration.ITRemoteConfiguration;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ITConfigurationApplication.class, ITRemoteConfiguration.class})
public class RemoteAdminServerServiceIT extends AdminServerServiceIT {

    @Autowired
    private AdminServerService adminServerService;
    @Autowired
    private LightminApplicationRepository lightminApplicationRepository;
    @Autowired
    private LightminClientRegistrator lightminClientRegistrator;
    @Autowired
    private EmbeddedWebApplicationContext embeddedWebApplicationContext;
    @Autowired
    private LightminClientProperties lightminClientProperties;
    @Autowired
    private LightminProperties lightminProperties;

    @Override
    public AdminServerService getAdminServerService() {
        assertThat(adminServerService instanceof RemoteAdminServerService).isTrue();
        return adminServerService;
    }

    @Override
    public LightminClientApplication createLightminClientApplication() {
        final Collection<LightminClientApplication> allApplications = lightminApplicationRepository.findAll();
        assertThat(allApplications).hasSize(1);
        return allApplications.iterator().next();
    }

    @Before
    public void init() {
        final int port = embeddedWebApplicationContext.getEmbeddedServletContainer().getPort();
        lightminClientProperties.setServiceUrl("http://localhost:" + port);
        lightminClientProperties.setServerPort(port);
        lightminClientProperties.setManagementPort(port);
        lightminProperties.setUrl(new String[]{"http://localhost:" + port});
        lightminClientRegistrator.register();
    }
}
