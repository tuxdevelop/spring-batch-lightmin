package org.tuxdevelop.spring.batch.lightmin.client.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.client.ITConfigurationApplication;
import org.tuxdevelop.spring.batch.lightmin.client.registration.LightminClientRegistrator;
import org.tuxdevelop.spring.batch.lightmin.client.registration.RegistrationLightminClientApplicationBean;
import org.tuxdevelop.spring.batch.lightmin.client.registration.listener.OnClientApplicationReadyEventListener;
import org.tuxdevelop.spring.batch.lightmin.client.registration.listener.OnContextClosedEventListener;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ITConfigurationApplication.class, LightminClientConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LightminClientConfigurationIT {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testClientConfiguration() {
        final LightminClientRegistrator lightminClientRegistrator = this.applicationContext.getBean(LightminClientRegistrator.class);
        assertThat(lightminClientRegistrator).isNotNull();
        final RegistrationLightminClientApplicationBean registrationLightminClientApplicationBean =
                this.applicationContext.getBean(RegistrationLightminClientApplicationBean.class);
        assertThat(registrationLightminClientApplicationBean).isNotNull();
        final OnClientApplicationReadyEventListener onClientApplicationReadyEventListener = this.applicationContext.getBean
                (OnClientApplicationReadyEventListener.class);
        assertThat(onClientApplicationReadyEventListener).isNotNull();
        final OnContextClosedEventListener onContextClosedEventListener = this.applicationContext.getBean
                (OnContextClosedEventListener.class);
        assertThat(onContextClosedEventListener).isNotNull();

    }

}
