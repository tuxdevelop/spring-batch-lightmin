package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.event.EmbeddedJobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.event.EmbeddedStepJobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.JobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.StepExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.event.listener.OnApplicationReadyEventEmbeddedListener;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

@Configuration
public class LightminEmbededClientPublishConfiguration {

    @Bean
    public JobExecutionEventPublisher jobExecutionEventPublisher(final EventService eventService) {
        return new EmbeddedJobExecutionEventPublisher(eventService);
    }

    @Bean
    public StepExecutionEventPublisher stepExecutionEventPublisher(final EventService eventService) {
        return new EmbeddedStepJobExecutionEventPublisher(eventService);

    }

    @Bean
    public OnApplicationReadyEventEmbeddedListener onApplicationReadyEventEmbeddedListener(
            final RegistrationBean registrationBean,
            final JobRegistry jobRegistry,
            final LightminClientProperties lightminClientProperties) {
        return new OnApplicationReadyEventEmbeddedListener(registrationBean, jobRegistry, lightminClientProperties);
    }
}
