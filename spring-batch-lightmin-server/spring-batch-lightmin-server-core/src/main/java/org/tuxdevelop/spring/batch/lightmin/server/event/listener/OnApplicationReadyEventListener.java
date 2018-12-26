package org.tuxdevelop.spring.batch.lightmin.server.event.listener;


import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.LightminServerCoreProperties;
import org.tuxdevelop.spring.batch.lightmin.server.support.ClientApplicationStatusUpdater;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public class OnApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private final ScheduledTaskRegistrar serverScheduledTaskRegistrar;
    private final ClientApplicationStatusUpdater clientApplicationStatusUpdater;
    private final LightminServerCoreProperties lightminServerProperties;

    public OnApplicationReadyEventListener(final ScheduledTaskRegistrar serverScheduledTaskRegistrar,
                                           final ClientApplicationStatusUpdater clientApplicationStatusUpdater,
                                           final LightminServerCoreProperties lightminServerProperties) {
        this.serverScheduledTaskRegistrar = serverScheduledTaskRegistrar;
        this.clientApplicationStatusUpdater = clientApplicationStatusUpdater;
        this.lightminServerProperties = lightminServerProperties;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        this.serverScheduledTaskRegistrar.addFixedRateTask(
                this.clientApplicationStatusUpdater::updateStatusForAllApplications,
                this.lightminServerProperties.getHeartbeatPeriod());
        this.serverScheduledTaskRegistrar.afterPropertiesSet();
    }
}
