package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.client.api.controller.JobConfigurationRestController;
import org.tuxdevelop.spring.batch.lightmin.client.api.controller.JobLauncherRestController;
import org.tuxdevelop.spring.batch.lightmin.client.api.controller.JobRestController;
import org.tuxdevelop.spring.batch.lightmin.service.*;

@Configuration
public class SpringBatchLightminRestConfiguration {

    @Bean
    public ServiceEntry serviceEntry(final AdminService adminService,
                                     final JobService jobService,
                                     final StepService stepService,
                                     final JobExecutionQueryService jobExecutionQueryService,
                                     final JobLauncherBean jobLauncherBean) {
        return new ControllerServiceEntryBean(adminService, jobService, stepService, jobExecutionQueryService, jobLauncherBean);
    }

    @Bean
    public JobConfigurationRestController jobConfigurationRestController(final ServiceEntry serviceEntry,
                                                                         final JobRegistry jobRegistry) {
        return new JobConfigurationRestController(serviceEntry, jobRegistry);
    }

    @Bean
    public JobRestController jobRestController(final ServiceEntry serviceEntry) {
        return new JobRestController(serviceEntry);
    }

    @Bean
    public JobLauncherRestController jobLauncherRestController(final ServiceEntry serviceEntry) {
        return new JobLauncherRestController(serviceEntry);
    }
}
