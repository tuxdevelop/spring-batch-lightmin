package org.tuxdevelop.spring.batch.lightmin.client.util;

import org.springframework.context.event.ContextRefreshedEvent;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public final class EventUtil {

    private static final int SERVER_PORT_DEFAULT = 8080;

    private EventUtil() {
    }

    public static void updatePorts(final ContextRefreshedEvent event, final LightminClientProperties lightminClientProperties) {
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
    }
}
