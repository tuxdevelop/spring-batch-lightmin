package org.tuxdevelop.spring.batch.lightmin.server.event;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

import java.io.Serializable;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@EqualsAndHashCode
public abstract class LightminClientApplicationEvent extends ApplicationEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final LightminClientApplication lightminClientApplication;
    private final EventType eventType;
    @Getter
    private final Long eventDateInMillis;


    public LightminClientApplicationEvent(final LightminClientApplication lightminClientApplication,
                                          final EventType eventType) {
        super(lightminClientApplication);
        this.lightminClientApplication = lightminClientApplication;
        this.eventType = eventType;
        this.eventDateInMillis = System.currentTimeMillis();
    }
}
