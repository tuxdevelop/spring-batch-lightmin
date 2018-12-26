package org.tuxdevelop.spring.batch.lightmin.listener;

import org.tuxdevelop.spring.batch.lightmin.domain.ListenerStatus;

/**
 * @author Marcel Becker
 * @see AbstractListener
 * @since 0.3
 */
public interface Listener {

    /**
     * Starts the {@link org.springframework.integration.dsl.IntegrationFlow} of the Listener
     */
    void start();

    /**
     * Stops the {@link org.springframework.integration.dsl.IntegrationFlow} of the Listener
     */
    void stop();

    /**
     * Retrieves the current {@link ListenerStatus} of the Listener
     *
     * @return the current status
     */
    ListenerStatus getListenerStatus();
}
