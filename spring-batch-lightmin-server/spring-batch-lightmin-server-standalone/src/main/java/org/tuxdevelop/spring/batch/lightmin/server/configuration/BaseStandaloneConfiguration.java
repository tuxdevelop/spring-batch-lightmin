package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.tuxdevelop.spring.batch.lightmin.server.annotation.EnableLightminServerCore;
import org.tuxdevelop.spring.batch.lightmin.server.api.controller.JobExecutionEventController;
import org.tuxdevelop.spring.batch.lightmin.server.api.controller.RegistrationController;
import org.tuxdevelop.spring.batch.lightmin.server.api.controller.StepExecutionEventController;
import org.tuxdevelop.spring.batch.lightmin.server.fe.annotation.EnableLightminServerFrontend;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

@Configuration
@EnableLightminServerCore
@EnableLightminServerFrontend
public abstract class BaseStandaloneConfiguration {

    @Bean
    public RegistrationController registrationController(final RegistrationBean registrationBean) {
        return new RegistrationController(registrationBean);
    }

    @Bean
    public JobExecutionEventController jobExecutionEventController(final EventService eventService) {
        return new JobExecutionEventController(eventService);
    }

    @Bean
    public StepExecutionEventController stepExecutionEventController(final EventService eventService) {
        return new StepExecutionEventController(eventService);
    }

    @Bean
    public ScheduledTaskRegistrar serverScheduledTaskRegistrar() {
        return new ScheduledTaskRegistrar();
    }
}
