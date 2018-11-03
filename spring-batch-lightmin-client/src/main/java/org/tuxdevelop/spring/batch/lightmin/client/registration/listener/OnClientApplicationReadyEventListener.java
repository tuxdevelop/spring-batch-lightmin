package org.tuxdevelop.spring.batch.lightmin.client.registration.listener;


import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.registration.RegistrationLightminClientApplicationBean;
import org.tuxdevelop.spring.batch.lightmin.client.util.EventUtil;

public class OnClientApplicationReadyEventListener implements ApplicationListener<ContextRefreshedEvent> {


    private final RegistrationLightminClientApplicationBean registrationLightminClientApplicationBean;
    private final LightminClientProperties lightminClientProperties;

    public OnClientApplicationReadyEventListener(
            final RegistrationLightminClientApplicationBean registrationLightminClientApplicationBean,
            final LightminClientProperties lightminClientProperties) {
        this.registrationLightminClientApplicationBean = registrationLightminClientApplicationBean;
        this.lightminClientProperties = lightminClientProperties;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        EventUtil.updatePorts(event, this.lightminClientProperties);
        this.registrationLightminClientApplicationBean.startRegisterTask();
    }
}
