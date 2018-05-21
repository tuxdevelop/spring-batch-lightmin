package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JobExecutionEventRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.MapJobExecutionEventRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.MapLightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventServiceBean;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.util.BasicAuthHttpRequestInterceptor;

import java.util.Collections;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Configuration
@EnableConfigurationProperties(value = {LightminServerProperties.class})
@Import(value = {SpringBatchLightminWebConfiguration.class, LightminServerDiscoveryConfiguration.class})
public class CommonServerConfiguration {

    @Bean
    @ConditionalOnMissingBean(LightminApplicationRepository.class)
    public LightminApplicationRepository lightminApplicationRepository() {
        return new MapLightminApplicationRepository();
    }

    @Bean
    public RegistrationBean registrationBean(final LightminApplicationRepository lightminApplicationRepository) {
        return new RegistrationBean(lightminApplicationRepository);
    }

    @Bean
    @ConditionalOnMissingBean(EventService.class)
    public EventService eventService(@Qualifier("jobExecutionEventRepository") final JobExecutionEventRepository jobExecutionEventRepository) {
        return new EventServiceBean(jobExecutionEventRepository);
    }

    @Bean
    @ConditionalOnMissingBean(value = JobExecutionEventRepository.class)
    public JobExecutionEventRepository jobExecutionEventRepository(final LightminServerProperties lightminServerProperties) {
        return new MapJobExecutionEventRepository(lightminServerProperties.getEventRepositorySize());
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
