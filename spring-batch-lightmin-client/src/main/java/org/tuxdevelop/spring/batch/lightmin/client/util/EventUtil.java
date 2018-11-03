package org.tuxdevelop.spring.batch.lightmin.client.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public final class EventUtil {

    private static final int SERVER_PORT_DEFAULT = 8080;

    private EventUtil() {
    }

    public static void updatePorts(
            final ContextRefreshedEvent event,
            final LightminClientProperties lightminClientProperties) {
        final Integer serverPort;
        final Integer managementPort;
        if (lightminClientProperties.getServerPort() != null) {
            serverPort = lightminClientProperties.getServerPort();
        } else {

            final Integer serverPortEnv = event
                    .getApplicationContext()
                    .getEnvironment()
                    .getProperty("server.port", Integer.class);

            if (serverPortEnv == null) {
                serverPort = SERVER_PORT_DEFAULT;
            } else {
                serverPort = serverPortEnv;
            }
        }
        log.info("Using server port {} for lightmin  client application registration", serverPort);
        lightminClientProperties.setServerPort(serverPort);

        if (lightminClientProperties.getManagementPort() != null) {
            managementPort = lightminClientProperties.getManagementPort();
        } else {
            managementPort = event
                    .getApplicationContext()
                    .getEnvironment()
                    .getProperty("management.port", Integer.class, serverPort);
        }
        log.info("Using management port {} for lightmin  client application registration", serverPort);
        lightminClientProperties.setManagementPort(managementPort);
    }
}
