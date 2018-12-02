package org.tuxdevelop.spring.batch.lightmin.server.event.listener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplicationStatus;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import java.util.LinkedList;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Slf4j
public class OnApplicationReadyEventEmbeddedListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final int SERVER_PORT_DEFAULT = 8080;
    private final RegistrationBean registrationBean;
    private final JobRegistry jobRegistry;
    private final LightminClientProperties lightminClientProperties;

    public OnApplicationReadyEventEmbeddedListener(final RegistrationBean registrationBean, final JobRegistry jobRegistry, final LightminClientProperties lightminClientProperties) {
        this.registrationBean = registrationBean;
        this.jobRegistry = jobRegistry;
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
        final LightminClientApplication lightminClientApplication = LightminClientApplication.createApplication(new LinkedList<>(jobRegistry.getJobNames()), lightminClientProperties);
        lightminClientApplication.setLightminClientApplicationStatus(LightminClientApplicationStatus.ofUp());
        final LightminClientApplication savedLightminClientApplication = registrationBean.register(lightminClientApplication);
        log.info("Registered local LightminClientApplication: {}", savedLightminClientApplication);
    }


}
