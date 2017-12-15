package org.tuxdevelop.spring.batch.lightmin.client.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.EurekaMetaDataExtender;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.MetaDataExtender;
import org.tuxdevelop.spring.batch.lightmin.test.config.eureka.EurekaITConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EurekaITConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LightminClientDiscoveryConfigurationEurekaIT {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testConfiguration() {
        final MetaDataExtender metaDataExtender = applicationContext.getBean(MetaDataExtender.class);
        assertThat(metaDataExtender instanceof EurekaMetaDataExtender).isTrue();
    }

}

