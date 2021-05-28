package org.tuxdevelop.spring.batch.lightmin.client.classic.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.client.annotation.EnableLightminClientCore;
import org.tuxdevelop.spring.batch.lightmin.client.classic.event.OnClientApplicationReadyEventListener;
import org.tuxdevelop.spring.batch.lightmin.client.classic.event.OnContextClosedEventListener;
import org.tuxdevelop.spring.batch.lightmin.client.classic.service.LightminClientApplicationRegistrationService;
import org.tuxdevelop.spring.batch.lightmin.client.classic.service.LightminClientRegistratorService;
import org.tuxdevelop.spring.batch.lightmin.client.classic.service.UrlLightminServerLocatorService;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientPublishEventsConfiguration;
import org.tuxdevelop.spring.batch.lightmin.client.service.LightminServerLocatorService;

@Configuration
@EnableLightminClientCore
@EnableConfigurationProperties(
        value = {LightminClientClassicConfigurationProperties.class})
@Import(LightminClientPublishEventsConfiguration.class)
public class ClassicLightminClientConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = {LightminClientRegistratorService.class})
    public LightminClientRegistratorService lightminClientRegistratorService(
            final LightminClientProperties lightminClientProperties,
            final LightminClientClassicConfigurationProperties lightminClientClassicConfigurationProperties,
            final JobRegistry jobRegistry,
            final LightminServerLocatorService lightminServerLocatorService,
            @Qualifier("serverRestTemplate") final RestTemplate restTemplate) {

        return new LightminClientRegistratorService(
                lightminClientProperties,
                lightminClientClassicConfigurationProperties,
                restTemplate,
                jobRegistry,
                lightminServerLocatorService);
    }

    /**
     * ApplicationListener triggering registration after being ready/shutdown
     */
    @Bean
    @ConditionalOnMissingBean(value = {LightminClientApplicationRegistrationService.class})
    public LightminClientApplicationRegistrationService lightminClientApplicationRegistrationService(
            final LightminClientRegistratorService lightminClientRegistrator,
            final LightminClientClassicConfigurationProperties lightminClientClassicConfigurationProperties) {
        final LightminClientApplicationRegistrationService registrationLightminClientApplicationBean =
                new LightminClientApplicationRegistrationService(lightminClientRegistrator);
        registrationLightminClientApplicationBean.setAutoRegister(
                lightminClientClassicConfigurationProperties.isAutoRegistration());
        registrationLightminClientApplicationBean.setAutoDeregister(
                lightminClientClassicConfigurationProperties.isAutoDeregistration());
        registrationLightminClientApplicationBean.setRegisterPeriod(
                lightminClientClassicConfigurationProperties.getPeriod());
        return registrationLightminClientApplicationBean;
    }


    @Bean
    @ConditionalOnMissingBean(value = {LightminServerLocatorService.class})
    public LightminServerLocatorService urlLightminServerLocator(
            final LightminClientClassicConfigurationProperties lightminClientClassicConfigurationProperties) {
        return new UrlLightminServerLocatorService(lightminClientClassicConfigurationProperties);
    }

    @Bean
    public OnClientApplicationReadyEventListener onClientApplicationReadyEventListener(
            final LightminClientApplicationRegistrationService lightminClientApplicationRegistrationService,
            final LightminClientProperties lightminClientProperties) {
        return new OnClientApplicationReadyEventListener(
                lightminClientApplicationRegistrationService, lightminClientProperties);
    }

    @Bean
    public OnContextClosedEventListener onContextClosedEventListener(
            final LightminClientApplicationRegistrationService lightminClientApplicationRegistrationService) {
        return new OnContextClosedEventListener(lightminClientApplicationRegistrationService);
    }
}
