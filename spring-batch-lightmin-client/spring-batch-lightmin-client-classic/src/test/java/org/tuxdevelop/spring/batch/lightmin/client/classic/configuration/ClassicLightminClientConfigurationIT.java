package org.tuxdevelop.spring.batch.lightmin.client.classic.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.client.classic.event.OnClientApplicationReadyEventListener;
import org.tuxdevelop.spring.batch.lightmin.client.classic.event.OnContextClosedEventListener;
import org.tuxdevelop.spring.batch.lightmin.client.classic.service.LightminClientApplicationRegistrationService;
import org.tuxdevelop.spring.batch.lightmin.client.classic.service.LightminClientRegistratorService;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.RemoteJobExecutionEventPublisher;
import org.tuxdevelop.test.configuration.ITConfigurationApplication;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {ITConfigurationApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClassicLightminClientConfigurationIT {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testClientConfiguration() {
        final LightminClientRegistratorService lightminClientRegistrator =
                this.applicationContext.getBean(LightminClientRegistratorService.class);
        assertThat(lightminClientRegistrator).isNotNull();
        final LightminClientApplicationRegistrationService lightminClientApplicationRegistrationService =
                this.applicationContext.getBean(LightminClientApplicationRegistrationService.class);
        assertThat(lightminClientApplicationRegistrationService).isNotNull();
        final OnClientApplicationReadyEventListener onClientApplicationReadyEventListener =
                this.applicationContext.getBean(OnClientApplicationReadyEventListener.class);
        assertThat(onClientApplicationReadyEventListener).isNotNull();
        final OnContextClosedEventListener onContextClosedEventListener =
                this.applicationContext.getBean(OnContextClosedEventListener.class);
        assertThat(onContextClosedEventListener).isNotNull();
        final RemoteJobExecutionEventPublisher jobExecutionEventPublisher =
                this.applicationContext.getBean(RemoteJobExecutionEventPublisher.class);
        assertThat(jobExecutionEventPublisher).isNotNull();

    }

}
