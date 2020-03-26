package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.annotation.EnableLightminCore;
import org.tuxdevelop.spring.batch.lightmin.client.annotation.EnableLightminClientCore;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.event.EmbeddedJobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.event.EmbeddedStepJobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.JobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.StepExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.event.listener.OnApplicationReadyEventEmbeddedListener;
import org.tuxdevelop.spring.batch.lightmin.server.annotation.EnableLightminServerCore;
import org.tuxdevelop.spring.batch.lightmin.server.fe.annotation.EnableLightminServerFrontend;
import org.tuxdevelop.spring.batch.lightmin.server.service.AdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.service.EmbeddedAdminServerService;
import org.tuxdevelop.spring.batch.lightmin.service.EmbeddedJobServerService;
import org.tuxdevelop.spring.batch.lightmin.service.ServiceEntry;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Configuration
@EnableLightminCore
@EnableLightminClientCore
@EnableLightminServerCore
@EnableLightminServerFrontend
public class LightminEmbeddedConfiguration {

    @Bean
    public AdminServerService adminServerService(final ServiceEntry serviceEntry) {
        return new EmbeddedAdminServerService(serviceEntry);
    }

    @Bean
    public JobServerService jobServerService(final ServiceEntry serviceEntry) {
        return new EmbeddedJobServerService(serviceEntry);
    }

    @Bean
    public OnApplicationReadyEventEmbeddedListener onApplicationReadyEventEmbeddedListener(
            final RegistrationBean registrationBean,
            final JobRegistry jobRegistry,
            final LightminClientProperties lightminClientProperties) {
        return new OnApplicationReadyEventEmbeddedListener(registrationBean, jobRegistry, lightminClientProperties);
    }


    @Bean
    public JobExecutionEventPublisher jobExecutionEventPublisher(final EventService eventService) {
        return new EmbeddedJobExecutionEventPublisher(eventService);
    }

    @Bean
    public StepExecutionEventPublisher stepExecutionEventPublisher(final EventService eventService) {
        return new EmbeddedStepJobExecutionEventPublisher(eventService);
    }
}

