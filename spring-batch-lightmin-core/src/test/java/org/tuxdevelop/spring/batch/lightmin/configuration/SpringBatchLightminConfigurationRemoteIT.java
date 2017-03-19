package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.RemoteJobConfigurationRepository;
import org.tuxdevelop.test.configuration.ITRemoteConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ITRemoteConfiguration.class)
public class SpringBatchLightminConfigurationRemoteIT {

    @Autowired
    private SpringBatchLightminConfigurator configurator;

    @Test
    public void initRemote() throws Exception {
        assertThat(this.configurator.getJobConfigurationRepository()).isInstanceOf(RemoteJobConfigurationRepository.class);
    }

}
