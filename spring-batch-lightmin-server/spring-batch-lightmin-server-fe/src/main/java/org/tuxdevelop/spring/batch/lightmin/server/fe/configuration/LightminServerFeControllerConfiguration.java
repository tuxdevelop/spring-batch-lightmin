package org.tuxdevelop.spring.batch.lightmin.server.fe.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.tuxdevelop.spring.batch.lightmin.server.fe.controller.*;
import org.tuxdevelop.spring.batch.lightmin.server.fe.service.*;

@Configuration
public class LightminServerFeControllerConfiguration {

    //IndexController
    @Bean
    public IndexController indexController() {
        return new IndexController();
    }

    //ApplicationsController

    @Bean
    public ApplicationsController applicationsController(
            final LightminClientApplicationFeService lightminClientApplicationFeService) {
        return new ApplicationsController(lightminClientApplicationFeService);
    }

    //Application Instance Controller

    @Bean
    public ApplicationInstanceController applicationInstanceController(
            final LightminClientApplicationFeService lightminClientApplicationFeService) {
        return new ApplicationInstanceController(lightminClientApplicationFeService);
    }

    //Batch Jobs Controller
    @Bean
    public BatchJobsController batchJobsController(final JobFeService jobFeService) {
        return new BatchJobsController(jobFeService);
    }

    //Batch Job Executions Controller

    @Bean
    public BatchJobExecutionsController batchJobExecutionsController(final JobExecutionFeService jobExecutionFeService) {
        return new BatchJobExecutionsController(jobExecutionFeService);
    }

    //Events Controller

    @Bean
    public EventsController eventsController() {
        return new EventsController();
    }

    //Job Execution Event Controller

    @Bean
    public JobExecutionEventsController jobExecutionEventsController(
            final JobExecutionEventFeService jobExecutionEventFeService) {
        return new JobExecutionEventsController(jobExecutionEventFeService);
    }

    //Job Launcher Controller

    @Bean
    public JobLauncherController jobLauncherController(final JobLauncherFeService jobLauncherFeService,
                                                       final Validator validator) {
        return new JobLauncherController(jobLauncherFeService, validator);
    }

    //Job Scheduler Controller

    @Bean
    public JobSchedulersController jobSchedulersController(final JobSchedulerFeService jobSchedulerFeService,
                                                           final Validator validator) {
        return new JobSchedulersController(jobSchedulerFeService, validator);
    }

    //Job Listeners Controller

    @Bean
    public JobListenersController jobListenersController(final JobListenerFeService jobListenerFeService,
                                                         final Validator validator) {
        return new JobListenersController(jobListenerFeService, validator);
    }

    //Global Exception Advice

    @Bean
    public GlobalExceptionControllerAdvice globalExceptionControllerAdvice() {
        return new GlobalExceptionControllerAdvice();
    }

    //Journals Controller

    @Bean
    public JournalsController journalsController(final JournalsFeService journalsFeService) {
        return new JournalsController(journalsFeService);
    }

    @Bean
    public AboutController aboutController() {
        return new AboutController();
    }
}
