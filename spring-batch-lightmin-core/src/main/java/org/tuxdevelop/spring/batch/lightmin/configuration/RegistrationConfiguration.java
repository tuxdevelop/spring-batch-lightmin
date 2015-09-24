package org.tuxdevelop.spring.batch.lightmin.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.admin.scheduler.Scheduler;
import org.tuxdevelop.spring.batch.lightmin.service.AdminService;
import org.tuxdevelop.spring.batch.lightmin.service.SchedulerService;
import org.tuxdevelop.spring.batch.lightmin.util.CommonJobFactory;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;

@Slf4j
@Configuration
public class RegistrationConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private AdminService adminService;

    @PostConstruct
    public void registerJobs() throws DuplicateJobException {
        // register jobs of the current application context
        final Map<String, Job> jobs = applicationContext.getBeansOfType(Job.class);
        if (jobs != null) {
            for (final Map.Entry<String, Job> jobEntry : jobs.entrySet()) {
                final Job job = jobEntry.getValue();
                final String jobName = job.getName();
                final CommonJobFactory commonJobFactory = new CommonJobFactory(job, jobName);
                jobRegistry.register(commonJobFactory);
            }
        }

        // register all stored jobConfigurations
        final Collection<JobConfiguration> jobConfigurations = adminService.getJobConfigurations(jobRegistry.getJobNames());
        if (jobConfigurations != null) {
            for (final JobConfiguration jobConfiguration : jobConfigurations) {
                final String jobName = jobConfiguration.getJobName();
                if (jobRegistry.getJobNames().contains(jobName)) {
                    schedulerService.registerSchedulerForJob(jobConfiguration);
                } else {
                    log.debug("No Job with jobName " + jobName + " is present. Registration canceled");
                }
            }
        }
        // Schedule all registered schedulers
        final Map<String, Scheduler> schedulerMap = applicationContext.getBeansOfType(Scheduler.class);
        if (schedulerMap != null) {
            for (final Map.Entry<String, Scheduler> schedulerEntry : schedulerMap.entrySet()) {
                final Scheduler scheduler = schedulerEntry.getValue();
                if (SchedulerStatus.RUNNING.equals(scheduler.getSchedulerStatus())) {
                    log.debug("scheduling bean: " + schedulerEntry.getKey());
                    scheduler.schedule();
                    log.debug("scheduled bean: " + schedulerEntry.getKey());
                }
            }
        }

    }


}
