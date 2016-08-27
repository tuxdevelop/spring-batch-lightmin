package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.tuxdevelop.spring.batch.lightmin.server.admin.AdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.admin.EmbeddedAdminServerServiceBean;
import org.tuxdevelop.spring.batch.lightmin.server.job.EmbeddedJobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.job.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.support.LocalRegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.support.ServiceEntry;

@Configuration
@Import(value = {CommonServerConfiguration.class})
public class EmbeddedServerConfiguration {

    @Bean
    public AdminServerService adminServerService(final ServiceEntry serviceEntry) {
        return new EmbeddedAdminServerServiceBean(serviceEntry);
    }

    @Bean
    public JobServerService jobServerService(final ServiceEntry serviceEntry) {
        return new EmbeddedJobServerService(serviceEntry);
    }

    @Bean
    public LocalRegistrationBean localRegistrationBean(final LightminApplicationRepository lightminApplicationRepository,
                                                       final Environment environment,
                                                       final JobRegistry jobRegistry) {
        return new LocalRegistrationBean(lightminApplicationRepository, environment, jobRegistry);
    }

}
