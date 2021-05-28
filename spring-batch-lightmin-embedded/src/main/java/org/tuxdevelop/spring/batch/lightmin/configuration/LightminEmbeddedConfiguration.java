package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.client.annotation.EnableLightminClientCore;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.event.EmbeddedJobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.event.EmbeddedStepJobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.JobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.StepExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.event.listener.OnApplicationReadyEventEmbeddedListener;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.BaseStandaloneConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.LightminServerCoreProperties;
import org.tuxdevelop.spring.batch.lightmin.server.repository.*;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.annotation.EnableServerSchedulerMapRepository;
import org.tuxdevelop.spring.batch.lightmin.server.service.AdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.sheduler.StandaloneSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.service.EmbeddedAdminServerService;
import org.tuxdevelop.spring.batch.lightmin.service.EmbeddedJobServerService;
import org.tuxdevelop.spring.batch.lightmin.service.ServiceEntry;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Configuration
@EnableLightminClientCore
@EnableServerSchedulerMapRepository
@Import(StandaloneSchedulerConfiguration.class)
public class LightminEmbeddedConfiguration extends BaseStandaloneConfiguration {

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
}

