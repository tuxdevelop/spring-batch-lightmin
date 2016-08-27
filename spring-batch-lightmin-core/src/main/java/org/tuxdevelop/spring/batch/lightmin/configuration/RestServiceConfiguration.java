package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.api.controller.JobConfigurationRestController;
import org.tuxdevelop.spring.batch.lightmin.api.controller.JobLauncherRestController;
import org.tuxdevelop.spring.batch.lightmin.api.controller.JobRestController;
import org.tuxdevelop.spring.batch.lightmin.service.AdminService;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;
import org.tuxdevelop.spring.batch.lightmin.service.StepService;
import org.tuxdevelop.spring.batch.lightmin.support.ControllerServiceEntryBean;
import org.tuxdevelop.spring.batch.lightmin.support.JobLauncherBean;
import org.tuxdevelop.spring.batch.lightmin.support.ServiceEntry;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Configuration
public class RestServiceConfiguration {

    @Bean
    public ServiceEntry serviceEntry(final AdminService adminService,
                                     final JobService jobService,
                                     final StepService stepService,
                                     final JobLauncherBean jobLauncherBean) {
        return new ControllerServiceEntryBean(adminService, jobService, stepService, jobLauncherBean);
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

    @Bean
    public JobLauncherBean jobLauncherBean(final JobLauncher defaultAsyncJobLauncher,
                                           final JobRegistry jobRegistry) {
        return new JobLauncherBean(defaultAsyncJobLauncher, jobRegistry);
    }
}
