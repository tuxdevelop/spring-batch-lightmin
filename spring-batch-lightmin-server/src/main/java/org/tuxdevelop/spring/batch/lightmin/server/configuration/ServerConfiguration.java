package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.tuxdevelop.spring.batch.lightmin.server.admin.AdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.admin.RemoteAdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.api.controller.JobExecutionEventController;
import org.tuxdevelop.spring.batch.lightmin.server.api.controller.RegistrationController;
import org.tuxdevelop.spring.batch.lightmin.server.event.listener.OnApplicationReadyEventListener;
import org.tuxdevelop.spring.batch.lightmin.server.event.listener.OnLightminClientApplicationRegisteredEventListener;
import org.tuxdevelop.spring.batch.lightmin.server.job.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.job.RemoteJobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;
import org.tuxdevelop.spring.batch.lightmin.server.support.ClientApplicationStatusUpdater;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Configuration
@Import(value = {CommonServerConfiguration.class})
public class ServerConfiguration {

    @Bean
    public RegistrationController registrationController(final RegistrationBean registrationBean) {
        return new RegistrationController(registrationBean);
    }

    @Bean
    public JobExecutionEventController jobExecutionEventController(final EventService eventService) {
        return new JobExecutionEventController(eventService);
    }

    @Bean
    public AdminServerService adminServerService(final LightminServerProperties lightminServerProperties) {
        return new RemoteAdminServerService(CommonServerConfiguration.RestTemplateFactory.getRestTemplate(lightminServerProperties));
    }

    @Bean
    public JobServerService jobServerService(final LightminServerProperties lightminServerProperties) {
        return new RemoteJobServerService(CommonServerConfiguration.RestTemplateFactory.getRestTemplate(lightminServerProperties));
    }

    @Bean
    public ClientApplicationStatusUpdater clientApplicationStatusUpdater(final LightminServerProperties lightminServerProperties,
                                                                         final LightminApplicationRepository lightminApplicationRepository) {
        return new ClientApplicationStatusUpdater(CommonServerConfiguration.RestTemplateFactory.getRestTemplate(lightminServerProperties), lightminApplicationRepository);
    }

    @Bean
    public ScheduledTaskRegistrar serverScheduledTaskRegistrar() {
        return new ScheduledTaskRegistrar();
    }

    @Bean
    public OnApplicationReadyEventListener onApplicationReadyEventListener(final ScheduledTaskRegistrar serverScheduledTaskRegistrar,
                                                                           final ClientApplicationStatusUpdater clientApplicationStatusUpdater,
                                                                           final LightminServerProperties lightminServerProperties) {
        return new OnApplicationReadyEventListener(serverScheduledTaskRegistrar, clientApplicationStatusUpdater, lightminServerProperties);
    }

    @Bean
    public OnLightminClientApplicationRegisteredEventListener onLightminClientApplicationRegisteredEventListener(final ClientApplicationStatusUpdater clientApplicationStatusUpdater) {
        return new OnLightminClientApplicationRegisteredEventListener(clientApplicationStatusUpdater);
    }

}
