package org.tuxdevelop.spring.batch.lightmin.client.registration.listener;


import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.tuxdevelop.spring.batch.lightmin.client.registration.RegistrationLightminClientApplicationBean;

public class OnContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {

    private final RegistrationLightminClientApplicationBean registrationLightminClientApplicationBean;

    public OnContextClosedEventListener(
            final RegistrationLightminClientApplicationBean registrationLightminClientApplicationBean) {
        this.registrationLightminClientApplicationBean = registrationLightminClientApplicationBean;
    }

    @Override
    public void onApplicationEvent(final ContextClosedEvent event) {
        this.registrationLightminClientApplicationBean.stopRegisterTask();
    }
}
