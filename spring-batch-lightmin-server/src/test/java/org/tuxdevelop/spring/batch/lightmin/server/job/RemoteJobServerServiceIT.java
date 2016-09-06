package org.tuxdevelop.spring.batch.lightmin.server.job;

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
import org.tuxdevelop.spring.batch.lightmin.server.ITRemoteConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ITConfigurationApplication.class, ITRemoteConfiguration.class})
public class RemoteJobServerServiceIT extends JobServerServiceIT {

    @Autowired
    private JobServerService jobServerService;
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
    public JobServerService getJobServerService() {
        assertThat(jobServerService instanceof RemoteJobServerService).isTrue();
        return jobServerService;
    }

    @Override
    public LightminClientApplication createLightminClientApplication() {
        final Collection<LightminClientApplication> allApplications = lightminApplicationRepository.findAll();
        assertThat(allApplications).hasSize(1);
        return allApplications.iterator().next();
    }

    @Before
    public void init() {
        super.init();
        final int port = embeddedWebApplicationContext.getEmbeddedServletContainer().getPort();
        lightminClientProperties.setServiceUrl("http://localhost:" + port);
        lightminClientProperties.setServerPort(port);
        lightminClientProperties.setManagementPort(port);
        lightminProperties.setUrl(new String[]{"http://localhost:" + port});
        lightminClientRegistrator.register();
    }
}
