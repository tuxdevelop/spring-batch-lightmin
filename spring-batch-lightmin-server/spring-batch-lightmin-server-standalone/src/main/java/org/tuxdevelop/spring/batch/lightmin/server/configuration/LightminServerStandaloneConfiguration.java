package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.server.event.listener.OnApplicationReadyEventListener;
import org.tuxdevelop.spring.batch.lightmin.server.event.listener.OnLightminClientApplicationRegisteredEventListener;
import org.tuxdevelop.spring.batch.lightmin.server.fe.annotation.EnableLightminServerFrontend;
import org.tuxdevelop.spring.batch.lightmin.server.repository.*;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.annotation.EnableServerSchedulerMapRepository;
import org.tuxdevelop.spring.batch.lightmin.server.service.AdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.service.RemoteAdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.service.RemoteJobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.sheduler.StandaloneSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.support.ClientApplicationStatusUpdater;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Configuration
@EnableLightminServerFrontend
@EnableServerSchedulerMapRepository
@Import(value = {LightminServerStandaloneDiscoveryConfiguration.class, StandaloneSchedulerConfiguration.class})
public class LightminServerStandaloneConfiguration extends BaseStandaloneConfiguration{


    @Bean
    public LightminApplicationRepository lightminApplicationRepository() {
        return new MapLightminApplicationRepository();
    }

    @Bean
    public JobExecutionEventRepository jobExecutionEventRepository(final LightminServerCoreProperties lightminServerCoreProperties) {
        return new MapJobExecutionEventRepository(lightminServerCoreProperties.getEventRepositorySize());
    }

    @Bean
    public JournalRepository journalRepository() {
        return new MapJournalRepository();
    }

    @Bean
    public AdminServerService adminServerService(@Qualifier("clientRestTemplate") final RestTemplate restTemplate) {
        return new RemoteAdminServerService(restTemplate);
    }

    @Bean
    public JobServerService jobServerService(@Qualifier("clientRestTemplate") final RestTemplate restTemplate) {
        return new RemoteJobServerService(restTemplate);
    }

    @Bean
    public ClientApplicationStatusUpdater clientApplicationStatusUpdater(
            @Qualifier("clientRestTemplate") final RestTemplate restTemplate,
            final LightminApplicationRepository lightminApplicationRepository) {
        return new ClientApplicationStatusUpdater(restTemplate, lightminApplicationRepository);
    }

    @Bean
    public OnApplicationReadyEventListener onApplicationReadyEventListener(final ScheduledTaskRegistrar serverScheduledTaskRegistrar,
                                                                           final ClientApplicationStatusUpdater clientApplicationStatusUpdater,
                                                                           final LightminServerCoreProperties lightminServerCoreProperties) {
        return new OnApplicationReadyEventListener(serverScheduledTaskRegistrar, clientApplicationStatusUpdater, lightminServerCoreProperties);
    }

    @Bean
    public OnLightminClientApplicationRegisteredEventListener onLightminClientApplicationRegisteredEventListener(final ClientApplicationStatusUpdater clientApplicationStatusUpdater) {
        return new OnLightminClientApplicationRegisteredEventListener(clientApplicationStatusUpdater);
    }


}
