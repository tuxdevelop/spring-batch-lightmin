package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.MapLightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

@Configuration
@Import(SpringBatchLightminWebConfiguration.class)
public class CommonServerConfiguration {

    @Bean
    public LightminApplicationRepository lightminApplicationRepository() {
        return new MapLightminApplicationRepository();
    }

    @Bean
    public RegistrationBean registrationBean(final LightminApplicationRepository lightminApplicationRepository) {
        return new RegistrationBean(lightminApplicationRepository);
    }

}
