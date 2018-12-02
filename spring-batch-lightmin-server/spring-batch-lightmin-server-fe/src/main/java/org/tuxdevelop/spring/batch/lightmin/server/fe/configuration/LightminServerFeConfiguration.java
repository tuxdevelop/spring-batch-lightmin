package org.tuxdevelop.spring.batch.lightmin.server.fe.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.tuxdevelop.spring.batch.lightmin.server.fe.service.*;
import org.tuxdevelop.spring.batch.lightmin.server.service.AdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.service.JournalServiceBean;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

@Configuration
@EnableConfigurationProperties(value = {LightminServerFeConfigurationProperties.class})
@Import(value = {LightminServerFeControllerConfiguration.class})
public class LightminServerFeConfiguration {

    @Bean
    public LightminClientApplicationFeService lightminClientApplicationFeService(
            final RegistrationBean registrationBean) {
        return new LightminClientApplicationFeService(registrationBean);
    }

    @Bean
    public JobFeService jobFeService(
            final JobServerService jobServerService,
            final RegistrationBean registrationBean) {
        return new JobFeService(jobServerService, registrationBean);
    }

    @Bean
    public JobExecutionFeService jobExecutionFeService(
            final RegistrationBean registrationBean,
            final JobServerService jobServerService) {
        return new JobExecutionFeService(registrationBean, jobServerService);
    }

    @Bean
    public JobExecutionEventFeService jobExecutionEventFeService(
            final RegistrationBean registrationBean,
            final EventService eventService) {
        return new JobExecutionEventFeService(registrationBean, eventService);
    }

    @Bean
    public JobLauncherFeService jobLauncherFeService(
            final RegistrationBean registrationBean,
            final JobServerService jobServerService) {
        return new JobLauncherFeService(registrationBean, jobServerService);
    }

    @Bean
    public JobSchedulerFeService jobSchedulerFeService(
            final RegistrationBean registrationBean,
            final AdminServerService adminServerService) {
        return new JobSchedulerFeService(registrationBean, adminServerService);
    }

    @Bean
    public JobListenerFeService jobListenerFeService(
            final RegistrationBean registrationBean,
            final AdminServerService adminServerService) {
        return new JobListenerFeService(registrationBean, adminServerService);
    }

    @Bean
    public JournalsFeService journalsFeService(
            final JournalServiceBean journalServiceBean) {
        return new JournalsFeService(journalServiceBean);
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
