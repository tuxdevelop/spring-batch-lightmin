
package org.tuxdevelop.spring.batch.lightmin.server.event;

import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplicationStatus;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public class LightminClientApplicationDeleteRegistrationEvent extends LightminClientApplicationEvent {
    private static final long serialVersionUID = 1L;

    public LightminClientApplicationDeleteRegistrationEvent(final LightminClientApplication lightminClientApplication) {
        super(lightminClientApplication,
                LightminClientApplicationStatus.ofUp().toString(),
                LightminClientApplicationStatus.ofOffline().toString(),
                EventType.DELETE_REGISTRATION);
    }
}
