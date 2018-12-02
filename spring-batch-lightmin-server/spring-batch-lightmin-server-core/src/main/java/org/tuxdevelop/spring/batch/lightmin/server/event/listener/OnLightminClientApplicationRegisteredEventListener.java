package org.tuxdevelop.spring.batch.lightmin.server.event.listener;

import org.springframework.context.ApplicationListener;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.event.LightminClientApplicationRegisteredEvent;
import org.tuxdevelop.spring.batch.lightmin.server.support.ClientApplicationStatusUpdater;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public class OnLightminClientApplicationRegisteredEventListener implements ApplicationListener<LightminClientApplicationRegisteredEvent> {

    private final ClientApplicationStatusUpdater clientApplicationStatusUpdater;

    public OnLightminClientApplicationRegisteredEventListener(
            final ClientApplicationStatusUpdater clientApplicationStatusUpdater) {
        this.clientApplicationStatusUpdater = clientApplicationStatusUpdater;
    }

    @Override
    public void onApplicationEvent(final LightminClientApplicationRegisteredEvent event) {
        this.clientApplicationStatusUpdater.updateStatus((LightminClientApplication) event.getSource());
    }
}
