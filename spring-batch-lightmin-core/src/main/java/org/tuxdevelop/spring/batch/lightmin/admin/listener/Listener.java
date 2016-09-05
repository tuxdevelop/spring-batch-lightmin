package org.tuxdevelop.spring.batch.lightmin.admin.listener;

import org.tuxdevelop.spring.batch.lightmin.admin.domain.ListenerStatus;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public interface Listener {

    void start();

    void stop();

    ListenerStatus getListenerStatus();
}
