package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.server.api.controller.RegistrationController;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

@Configuration
@Import(value = {CommonServerConfiguration.class, LightminServerProperties.class})
public class ServerConfiguration {

    @Bean
    public RegistrationController registrationController(final RegistrationBean registrationBean) {
        return new RegistrationController(registrationBean);
    }

}
