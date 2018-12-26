
package org.tuxdevelop.spring.batch.lightmin.server.event;

import lombok.EqualsAndHashCode;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@EqualsAndHashCode(callSuper = true)
public class LightminClientApplicationChangedEvent extends LightminClientApplicationEvent {
    private static final long serialVersionUID = 1L;

    public LightminClientApplicationChangedEvent(final LightminClientApplication lightminClientApplication,
                                                 final String oldStatus,
                                                 final String newStatus) {
        super(lightminClientApplication, oldStatus, newStatus, EventType.CHANGE_REGISTRATION);
    }
}
