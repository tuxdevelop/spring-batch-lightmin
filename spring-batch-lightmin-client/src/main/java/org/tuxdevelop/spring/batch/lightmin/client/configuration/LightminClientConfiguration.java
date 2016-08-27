package org.tuxdevelop.spring.batch.lightmin.client.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.client.registration.LightminClientRegistrator;
import org.tuxdevelop.spring.batch.lightmin.client.registration.RegistrationLightminClientApplicationListener;

import java.util.Collections;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Configuration
@EnableConfigurationProperties(value = {LightminClientProperties.class, LightminProperties.class})
public class LightminClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LightminClientRegistrator lightminClientRegistrator(final LightminClientProperties lightminClientProperties,
                                                               final LightminProperties lightminProperties,
                                                               final JobRegistry jobRegistry) {
        final RestTemplate restTemplate = new RestTemplate();
        if (lightminProperties.getUsername() != null) {
            restTemplate.setInterceptors(
                    Collections.<ClientHttpRequestInterceptor>singletonList(
                            new BasicAuthHttpRequestInterceptor(lightminProperties.getUsername(),
                                    lightminProperties.getPassword()))
            );
        }
        return new LightminClientRegistrator(lightminClientProperties, lightminProperties, restTemplate, jobRegistry);
    }

    /**
     * ApplicationListener triggering registration after being ready/shutdown
     */
    @Bean
    @ConditionalOnMissingBean
    public RegistrationLightminClientApplicationListener registrationLightminClientApplicationListener(
            final LightminClientRegistrator lightminClientRegistrator,
            final LightminProperties lightminProperties) {
        final RegistrationLightminClientApplicationListener registrationLightminClientApplicationListener =
                new RegistrationLightminClientApplicationListener(lightminClientRegistrator);
        registrationLightminClientApplicationListener.setAutoRegister(lightminProperties.isAutoRegistration());
        registrationLightminClientApplicationListener.setAutoDeregister(lightminProperties.isAutoDeregistration());
        registrationLightminClientApplicationListener.setRegisterPeriod(lightminProperties.getPeriod());
        return registrationLightminClientApplicationListener;
    }
}
