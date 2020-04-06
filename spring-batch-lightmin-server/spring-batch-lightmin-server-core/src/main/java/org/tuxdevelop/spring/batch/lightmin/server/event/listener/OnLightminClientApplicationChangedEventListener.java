package org.tuxdevelop.spring.batch.lightmin.server.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplicationStatus;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.LightminServerCoreProperties;
import org.tuxdevelop.spring.batch.lightmin.server.event.LightminClientApplicationChangedEvent;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;

@Slf4j
public class OnLightminClientApplicationChangedEventListener {

    private final LightminApplicationRepository lightminApplicationRepository;
    private final LightminServerCoreProperties properties;

    public OnLightminClientApplicationChangedEventListener(final LightminApplicationRepository lightminApplicationRepository,
                                                           final LightminServerCoreProperties properties) {
        this.lightminApplicationRepository = lightminApplicationRepository;
        this.properties = properties;
    }

    @EventListener(value = {LightminClientApplicationChangedEvent.class})
    public void onLightminClientApplicationChangedEvent(final LightminClientApplicationChangedEvent event) {
        if (LightminClientApplicationStatus.ofOffline().getStatus().equals(event.getNewStatus())) {
            if (this.properties.getRemoveOfflineClients()) {
                final LightminClientApplication lightminClientApplication = (LightminClientApplication) event.getSource();
                final String id = lightminClientApplication.getId();
                this.lightminApplicationRepository.delete(id);
            } else {
                log.trace("remove offline clients is set to false, skipping");
            }
        } else {
            log.trace("Nothing todo skip");
        }
    }

}
