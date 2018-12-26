package org.tuxdevelop.spring.batch.lightmin.service;

import org.junit.runner.RunWith;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.test.configuration.ITEmbeddedConfiguration;
import org.tuxdevelop.test.configuration.ITEmbeddedConfigurationApplication;

import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {ITEmbeddedConfigurationApplication.class, ITEmbeddedConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmbeddedJobServerServiceIT extends JobServerServiceIT {

    @Autowired
    private JobServerService jobServerService;
    @Autowired
    private JobRegistry jobRegistry;
    @Autowired
    private LightminClientProperties lightminClientProperties;
    @Autowired
    private RegistrationBean registrationBean;

    @Override
    public JobServerService getJobServerService() {
        assertThat(this.jobServerService instanceof EmbeddedJobServerService).isTrue();
        return this.jobServerService;
    }

    @Override
    public LightminClientApplication createLightminClientApplication() {
        final LightminClientApplication lightminClientApplication =
                LightminClientApplication.createApplication(
                        new LinkedList<>(this.jobRegistry.getJobNames()),
                        this.lightminClientProperties);
        this.registrationBean.register(lightminClientApplication);
        return lightminClientApplication;
    }
}
