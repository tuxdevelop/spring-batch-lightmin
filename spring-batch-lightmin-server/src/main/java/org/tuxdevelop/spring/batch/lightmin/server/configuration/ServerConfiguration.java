package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.server.admin.AdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.admin.RemoteAdminServerServiceBean;
import org.tuxdevelop.spring.batch.lightmin.server.api.controller.RegistrationController;
import org.tuxdevelop.spring.batch.lightmin.server.job.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.job.RemoteJobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

@Configuration
@Import(value = {CommonServerConfiguration.class, LightminServerProperties.class})
public class ServerConfiguration {

    @Bean
    public RegistrationController registrationController(final RegistrationBean registrationBean) {
        return new RegistrationController(registrationBean);
    }

    @Bean
    public AdminServerService adminServerService(final RestTemplate restTemplate) {
        return new RemoteAdminServerServiceBean(restTemplate);
    }

    @Bean
    public JobServerService jobServerService(final RestTemplate restTemplate) {
        return new RemoteJobServerService(restTemplate);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
