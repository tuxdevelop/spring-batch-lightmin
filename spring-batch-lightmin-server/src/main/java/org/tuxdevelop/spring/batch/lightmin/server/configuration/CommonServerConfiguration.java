package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.MapLightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.util.BasicAuthHttpRequestInterceptor;

import java.util.Collections;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Configuration
@Import(value = {SpringBatchLightminWebConfiguration.class})
public class CommonServerConfiguration {

    @Bean
    public LightminApplicationRepository lightminApplicationRepository() {
        return new MapLightminApplicationRepository();
    }

    @Bean
    public RegistrationBean registrationBean(final LightminApplicationRepository lightminApplicationRepository) {
        return new RegistrationBean(lightminApplicationRepository);
    }

    static class RestTemplateFactory {

        private static RestTemplate restTemplate;

        static RestTemplate getRestTemplate(final LightminServerProperties lightminServerProperties) {

            if (restTemplate == null) {
                restTemplate = new RestTemplate();
            }
            if (lightminServerProperties.getClientUserName() != null) {
                restTemplate.setInterceptors(
                        Collections.singletonList(
                                new BasicAuthHttpRequestInterceptor(lightminServerProperties.getClientUserName(),
                                        lightminServerProperties.getClientPassword()))
                );
            }
            return restTemplate;
        }
    }
}
