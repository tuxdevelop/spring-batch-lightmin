package org.tuxdevelop.spring.batch.lightmin.client.registration.listener;


import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.registration.RegistrationLightminClientApplicationBean;

public class OnApplicationReadyEventListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final int SERVER_PORT_DEFAULT = 8080;
    private final RegistrationLightminClientApplicationBean registrationLightminClientApplicationBean;
    private final LightminClientProperties lightminClientProperties;

    public OnApplicationReadyEventListener(final RegistrationLightminClientApplicationBean registrationLightminClientApplicationBean, final LightminClientProperties lightminClientProperties) {
        this.registrationLightminClientApplicationBean = registrationLightminClientApplicationBean;
        this.lightminClientProperties = lightminClientProperties;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        final Integer serverPortEnv = event.getApplicationContext().getEnvironment().getProperty("server.port", Integer.class);
        final Integer serverPort;
        if (serverPortEnv == null) {
            serverPort = SERVER_PORT_DEFAULT;
        } else {
            serverPort = serverPortEnv;
        }
        final Integer managementPort = event.getApplicationContext().getEnvironment().getProperty("management.port", Integer.class, serverPort);
        lightminClientProperties.setServerPort(serverPort);
        lightminClientProperties.setManagementPort(managementPort);
        registrationLightminClientApplicationBean.startRegisterTask();
    }
}
