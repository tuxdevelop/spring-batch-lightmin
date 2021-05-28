package org.tuxdevelop.spring.batch.lightmin.client.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.client.listener.OnJobExecutionFinishedEventListener;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.JobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.RemoteJobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.RemoteStepExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.StepExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.service.LightminServerLocatorService;

@Configuration
@ConditionalOnProperty(
        prefix = "spring.batch.lightmin.client",
        value = "publish-job-events",
        havingValue = "true",
        matchIfMissing = true)
public class LightminClientPublishEventsConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = {StepExecutionEventPublisher.class})
    public StepExecutionEventPublisher stepExecutionEventPublisher(
            final LightminServerLocatorService lightminServerLocator,
            @Qualifier("serverRestTemplate") final RestTemplate restTemplate) {
        return new RemoteStepExecutionEventPublisher(restTemplate, lightminServerLocator);
    }

    @Bean
    @ConditionalOnMissingBean(value = {JobExecutionEventPublisher.class})
    public JobExecutionEventPublisher jobExecutionEventPublisher(
            final LightminServerLocatorService lightminServerLocatorService,
            @Qualifier("serverRestTemplate") final RestTemplate restTemplate) {
        return new RemoteJobExecutionEventPublisher(restTemplate, lightminServerLocatorService);
    }

    @Bean
    public OnJobExecutionFinishedEventListener onJobExecutionFinishedEventListener(
            final JobExecutionEventPublisher jobExecutionEventPublisher,
            final StepExecutionEventPublisher stepExecutionEventPublisher) {
        return new OnJobExecutionFinishedEventListener(jobExecutionEventPublisher, stepExecutionEventPublisher);
    }
}
