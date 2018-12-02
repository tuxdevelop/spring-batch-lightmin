package org.tuxdevelop.spring.batch.lightmin.client.classic.event;


import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.tuxdevelop.spring.batch.lightmin.client.classic.service.LightminClientApplicationRegistrationService;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.util.EventUtil;

public class OnClientApplicationReadyEventListener implements ApplicationListener<ContextRefreshedEvent> {


    private final LightminClientApplicationRegistrationService lightminClientApplicationRegistrationService;
    private final LightminClientProperties lightminClientProperties;

    public OnClientApplicationReadyEventListener(
            final LightminClientApplicationRegistrationService lightminClientApplicationRegistrationService,
            final LightminClientProperties lightminClientProperties) {
        this.lightminClientApplicationRegistrationService = lightminClientApplicationRegistrationService;
        this.lightminClientProperties = lightminClientProperties;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        EventUtil.updatePorts(event, this.lightminClientProperties);
        this.lightminClientApplicationRegistrationService.startRegisterTask();
    }
}
