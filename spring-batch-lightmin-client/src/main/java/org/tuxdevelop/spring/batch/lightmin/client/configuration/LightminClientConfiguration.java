package org.tuxdevelop.spring.batch.lightmin.client.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.client.api.controller.LightminClientApplicationRestController;
import org.tuxdevelop.spring.batch.lightmin.client.event.JobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.event.listener.OnJobExecutionFinishedEventListener;
import org.tuxdevelop.spring.batch.lightmin.client.registration.LightminClientRegistrator;
import org.tuxdevelop.spring.batch.lightmin.client.registration.RegistrationLightminClientApplicationBean;
import org.tuxdevelop.spring.batch.lightmin.client.registration.listener.OnClientApplicationReadyEventListener;
import org.tuxdevelop.spring.batch.lightmin.client.registration.listener.OnContextClosedEventListener;
import org.tuxdevelop.spring.batch.lightmin.client.registration.service.LightminClientApplicationService;
import org.tuxdevelop.spring.batch.lightmin.configuration.CommonSpringBatchLightminConfiguration;
import org.tuxdevelop.spring.batch.lightmin.util.BasicAuthHttpRequestInterceptor;

import java.util.Collections;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Configuration
@EnableConfigurationProperties(value = {LightminClientProperties.class, LightminProperties.class})
@Import(value = {CommonSpringBatchLightminConfiguration.class, LightminClientDiscoveryConfiguration.class})
public class LightminClientConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = {LightminClientRegistrator.class})
    @ConditionalOnProperty(prefix = "spring.batch.lightmin.client", value = "discovery-enabled", havingValue = "false", matchIfMissing = true)
    public LightminClientRegistrator lightminClientRegistrator(final LightminClientProperties lightminClientProperties,
                                                               final LightminProperties lightminProperties,
                                                               final JobRegistry jobRegistry) {
        final RestTemplate restTemplate = LightminServerRestTemplateFactory.getRestTemplate(lightminProperties);
        return new LightminClientRegistrator(lightminClientProperties, lightminProperties, restTemplate, jobRegistry);
    }

    /**
     * ApplicationListener triggering registration after being ready/shutdown
     */
    @Bean
    @ConditionalOnMissingBean(value = {RegistrationLightminClientApplicationBean.class})
    @ConditionalOnProperty(prefix = "spring.batch.lightmin.client", value = "discovery-enabled", havingValue = "false", matchIfMissing = true)
    public RegistrationLightminClientApplicationBean registrationLightminClientApplicationBean(final LightminClientRegistrator lightminClientRegistrator,
                                                                                               final LightminProperties lightminProperties) {
        final RegistrationLightminClientApplicationBean registrationLightminClientApplicationBean =
                new RegistrationLightminClientApplicationBean(lightminClientRegistrator);
        registrationLightminClientApplicationBean.setAutoRegister(lightminProperties.isAutoRegistration());
        registrationLightminClientApplicationBean.setAutoDeregister(lightminProperties.isAutoDeregistration());
        registrationLightminClientApplicationBean.setRegisterPeriod(lightminProperties.getPeriod());
        return registrationLightminClientApplicationBean;
    }

    @Bean
    public JobExecutionEventPublisher jobExecutionEventPublisher(final LightminProperties lightminProperties) {
        final RestTemplate restTemplate = LightminServerRestTemplateFactory.getRestTemplate(lightminProperties);
        return new JobExecutionEventPublisher(restTemplate, lightminProperties);
    }

    @Bean
    @ConditionalOnMissingBean(value = {LightminClientDiscoveryConfiguration.class})
    public OnClientApplicationReadyEventListener onClientApplicationReadyEventListener(final RegistrationLightminClientApplicationBean registrationLightminClientApplicationBean,
                                                                                       final LightminClientProperties lightminClientProperties) {
        return new OnClientApplicationReadyEventListener(registrationLightminClientApplicationBean, lightminClientProperties);
    }

    @Bean
    @ConditionalOnMissingBean(value = {LightminClientDiscoveryConfiguration.class})
    public OnContextClosedEventListener onContextClosedEventListener(final RegistrationLightminClientApplicationBean registrationLightminClientApplicationBean) {
        return new OnContextClosedEventListener(registrationLightminClientApplicationBean);
    }

    @Bean
    public OnJobExecutionFinishedEventListener onJobExecutionFinishedEventListener(final JobExecutionEventPublisher jobExecutionEventPublisher) {
        return new OnJobExecutionFinishedEventListener(jobExecutionEventPublisher);
    }

    @Bean
    public LightminClientApplicationService lightminClientApplicationService(final JobRegistry jobRegistry,
                                                                             final LightminClientProperties lightminClientProperties) {
        return new LightminClientApplicationService(jobRegistry, lightminClientProperties);
    }

    @Bean
    public LightminClientApplicationRestController lightminClientApplicationRestController(final LightminClientApplicationService lightminClientApplicationService) {
        return new LightminClientApplicationRestController(lightminClientApplicationService);
    }

    static class LightminServerRestTemplateFactory {

        private static RestTemplate restTemplate;

        static RestTemplate getRestTemplate(final LightminProperties lightminProperties) {

            if (restTemplate == null) {
                restTemplate = new RestTemplate();
            }
            if (lightminProperties.getUsername() != null) {
                restTemplate.setInterceptors(
                        Collections.singletonList(
                                new BasicAuthHttpRequestInterceptor(lightminProperties.getUsername(),
                                        lightminProperties.getPassword()))
                );
            }
            return restTemplate;
        }
    }
}
