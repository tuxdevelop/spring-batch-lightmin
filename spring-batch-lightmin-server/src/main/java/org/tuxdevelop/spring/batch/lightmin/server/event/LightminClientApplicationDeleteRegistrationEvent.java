
package org.tuxdevelop.spring.batch.lightmin.server.event;

import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public class LightminClientApplicationDeleteRegistrationEvent extends LightminClientApplicationEvent {
    private static final long serialVersionUID = 1L;

    public LightminClientApplicationDeleteRegistrationEvent(final LightminClientApplication lightminClientApplication) {
        super(lightminClientApplication, EventType.DELETE_REGISTRATION);
    }
}
