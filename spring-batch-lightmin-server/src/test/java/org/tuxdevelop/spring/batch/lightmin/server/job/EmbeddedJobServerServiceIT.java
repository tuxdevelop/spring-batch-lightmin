package org.tuxdevelop.spring.batch.lightmin.server.job;

import org.junit.runner.RunWith;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.server.ITConfigurationApplication;
import org.tuxdevelop.spring.batch.lightmin.server.ITConfigurationEmbedded;

import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ITConfigurationApplication.class, ITConfigurationEmbedded.class})
public class EmbeddedJobServerServiceIT extends JobServerServiceIT {

    @Autowired
    private JobServerService jobServerService;
    @Autowired
    private JobRegistry jobRegistry;
    @Autowired
    private LightminClientProperties lightminClientProperties;

    @Override
    public JobServerService getJobServerService() {
        assertThat(jobServerService instanceof EmbeddedJobServerService).isTrue();
        return jobServerService;
    }

    @Override
    public LightminClientApplication createLightminClientApplication() {
        return LightminClientApplication.createApplication(new LinkedList<>(jobRegistry.getJobNames()), lightminClientProperties);
    }
}
