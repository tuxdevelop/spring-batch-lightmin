package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.server.repository.*;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventServiceBean;
import org.tuxdevelop.spring.batch.lightmin.server.service.JournalServiceBean;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.util.BasicAuthHttpRequestInterceptor;

import java.util.Collections;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Configuration
@EnableConfigurationProperties(value = {LightminServerCoreProperties.class})
public class LightminServerCoreConfiguration {

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
    public JobExecutionEventRepository jobExecutionEventRepository(final LightminServerCoreProperties lightminServerCoreProperties) {
        return new MapJobExecutionEventRepository(lightminServerCoreProperties.getEventRepositorySize());
    }

    @Bean
    @ConditionalOnMissingBean(value = {JournalRepository.class})
    public JournalRepository journalRepository() {
        return new MapJournalRepository();
    }

    @Bean
    @ConditionalOnMissingBean(value = {JournalServiceBean.class})
    public JournalServiceBean journalServiceBean(final JournalRepository journalRepository) {
        return new JournalServiceBean(journalRepository);
    }

    static class RestTemplateFactory {

        private static RestTemplate restTemplate;

        static RestTemplate getRestTemplate(final LightminServerCoreProperties lightminServerCoreProperties) {

            if (restTemplate == null) {
                restTemplate = new RestTemplate();
            }
            if (lightminServerCoreProperties.getClientUserName() != null) {
                restTemplate.setInterceptors(
                        Collections.singletonList(
                                new BasicAuthHttpRequestInterceptor(lightminServerCoreProperties.getClientUserName(),
                                        lightminServerCoreProperties.getClientPassword()))
                );
            }
            return restTemplate;
        }
    }
}
