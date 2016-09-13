
package org.tuxdevelop.spring.batch.lightmin.server.event;

import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public class LightminClientApplicationRegisteredEvent extends LightminClientApplicationEvent {
    private static final long serialVersionUID = 1L;

    public LightminClientApplicationRegisteredEvent(final LightminClientApplication lightminClientApplication) {
        super(lightminClientApplication, EventType.ADD_REGISTRATION);
    }
}
