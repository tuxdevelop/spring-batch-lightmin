package org.tuxdevelop.spring.batch.lightmin.client.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.client.event.JobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.event.listener.OnJobExecutionFailedEventListener;
import org.tuxdevelop.spring.batch.lightmin.client.registration.LightminClientRegistrator;
import org.tuxdevelop.spring.batch.lightmin.client.registration.RegistrationLightminClientApplicationBean;
import org.tuxdevelop.spring.batch.lightmin.client.registration.listener.OnClientApplicationReadyEventListener;
import org.tuxdevelop.spring.batch.lightmin.client.registration.listener.OnContextClosedEventListener;
import org.tuxdevelop.spring.batch.lightmin.configuration.CommonSpringBatchLightminConfiguration;
import org.tuxdevelop.spring.batch.lightmin.util.BasicAuthHttpRequestInterceptor;

import java.util.Collections;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Configuration
@EnableConfigurationProperties(value = {LightminClientProperties.class, LightminProperties.class})
@Import(value = {CommonSpringBatchLightminConfiguration.class})
public class LightminClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
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
    @ConditionalOnMissingBean
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
    public OnClientApplicationReadyEventListener onClientApplicationReadyEventListener(final RegistrationLightminClientApplicationBean registrationLightminClientApplicationBean,
                                                                                       final LightminClientProperties lightminClientProperties) {
        return new OnClientApplicationReadyEventListener(registrationLightminClientApplicationBean, lightminClientProperties);
    }

    @Bean
    public OnContextClosedEventListener onContextClosedEventListener(final RegistrationLightminClientApplicationBean registrationLightminClientApplicationBean) {
        return new OnContextClosedEventListener(registrationLightminClientApplicationBean);
    }

    @Bean
    public OnJobExecutionFailedEventListener onJobExecutionFailedEventListener(final JobExecutionEventPublisher jobExecutionEventPublisher) {
        return new OnJobExecutionFailedEventListener(jobExecutionEventPublisher);
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
