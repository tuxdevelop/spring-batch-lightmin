package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.server.annotation.EnableLightminServerCore;
import org.tuxdevelop.spring.batch.lightmin.server.api.controller.JobExecutionEventController;
import org.tuxdevelop.spring.batch.lightmin.server.api.controller.RegistrationController;
import org.tuxdevelop.spring.batch.lightmin.server.api.controller.StepExecutionEventController;
import org.tuxdevelop.spring.batch.lightmin.server.event.listener.OnApplicationReadyEventListener;
import org.tuxdevelop.spring.batch.lightmin.server.event.listener.OnLightminClientApplicationRegisteredEventListener;
import org.tuxdevelop.spring.batch.lightmin.server.fe.annotation.EnableLightminServerFrontend;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.service.*;
import org.tuxdevelop.spring.batch.lightmin.server.support.ClientApplicationStatusUpdater;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Configuration
@EnableLightminServerCore
@EnableLightminServerFrontend
@Import(value = {LightminServerStandaloneDiscoveryConfiguration.class})
public class LightminServerStandaloneConfiguration {

    @Bean
    public RegistrationController registrationController(final RegistrationBean registrationBean) {
        return new RegistrationController(registrationBean);
    }

    @Bean
    public JobExecutionEventController jobExecutionEventController(final EventService eventService) {
        return new JobExecutionEventController(eventService);
    }

    @Bean
    public StepExecutionEventController stepExecutionEventController(final EventService eventService) {
        return new StepExecutionEventController(eventService);
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
    public ScheduledTaskRegistrar serverScheduledTaskRegistrar() {
        return new ScheduledTaskRegistrar();
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
