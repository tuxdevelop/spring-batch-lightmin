package org.tuxdevelop.spring.batch.lightmin.client.discovery.listener;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.util.EventUtil;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public class DiscoveryListener {

    private final LightminClientProperties lightminClientProperties;

    public DiscoveryListener(final LightminClientProperties lightminClientProperties) {
        this.lightminClientProperties = lightminClientProperties;
    }

    @EventListener(value = {ContextRefreshedEvent.class})
    public void onContextRefreshedEvent(final ContextRefreshedEvent event) {
        EventUtil.updatePorts(event, this.lightminClientProperties);
    }

}
