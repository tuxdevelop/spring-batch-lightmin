package org.tuxdevelop.spring.batch.lightmin.client.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.batch.lightmin.client.ITConfigurationApplication;
import org.tuxdevelop.spring.batch.lightmin.client.registration.LightminClientRegistrator;
import org.tuxdevelop.spring.batch.lightmin.client.registration.RegistrationLightminClientApplicationBean;
import org.tuxdevelop.spring.batch.lightmin.client.registration.listener.OnClientApplicationReadyEventListener;
import org.tuxdevelop.spring.batch.lightmin.client.registration.listener.OnContextClosedEventListener;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
@SpringApplicationConfiguration(classes = {ITConfigurationApplication.class, LightminClientConfiguration.class})
public class LightminClientConfigurationIT {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testClientConfiguration() {
        final LightminClientRegistrator lightminClientRegistrator = applicationContext.getBean(LightminClientRegistrator.class);
        assertThat(lightminClientRegistrator).isNotNull();
        final RegistrationLightminClientApplicationBean registrationLightminClientApplicationBean =
                applicationContext.getBean(RegistrationLightminClientApplicationBean.class);
        assertThat(registrationLightminClientApplicationBean).isNotNull();
        final OnClientApplicationReadyEventListener onClientApplicationReadyEventListener = applicationContext.getBean
                (OnClientApplicationReadyEventListener.class);
        assertThat(onClientApplicationReadyEventListener).isNotNull();
        final OnContextClosedEventListener onContextClosedEventListener = applicationContext.getBean
                (OnContextClosedEventListener.class);
        assertThat(onContextClosedEventListener).isNotNull();

    }

}
