
package org.tuxdevelop.spring.batch.lightmin.server.event;

import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplicationStatus;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public class LightminClientApplicationRegisteredEvent extends LightminClientApplicationEvent {
    private static final long serialVersionUID = 1L;

    public LightminClientApplicationRegisteredEvent(final LightminClientApplication lightminClientApplication) {
        super(lightminClientApplication,
                LightminClientApplicationStatus.ofUnknown().toString(),
                LightminClientApplicationStatus.ofUp().toString(),
                EventType.ADD_REGISTRATION);
    }
}
