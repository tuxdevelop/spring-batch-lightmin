package org.tuxdevelop.spring.batch.lightmin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.ListenerStatus;
import org.tuxdevelop.spring.batch.lightmin.domain.SchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.listener.Listener;
import org.tuxdevelop.spring.batch.lightmin.scheduler.Scheduler;
import org.tuxdevelop.spring.batch.lightmin.util.CommonJobFactory;

import java.util.Collection;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Slf4j
public class JobCreationListener implements ApplicationListener<ApplicationReadyEvent> {

    private final ApplicationContext applicationContext;
    private final JobRegistry jobRegistry;
    private final AdminService adminService;
    private final SchedulerService schedulerService;
    private final ListenerService listenerService;
    private final JobExecutionListenerRegisterBean jobExecutionListenerRegisterBean;

    public JobCreationListener(final ApplicationContext applicationContext,
                               final JobRegistry jobRegistry,
                               final AdminService adminService,
                               final SchedulerService schedulerService,
                               final ListenerService listenerService,
                               final JobExecutionListenerRegisterBean jobExecutionListenerRegisterBean) {
        this.applicationContext = applicationContext;
        this.jobRegistry = jobRegistry;
        this.adminService = adminService;
        this.schedulerService = schedulerService;
        this.listenerService = listenerService;
        this.jobExecutionListenerRegisterBean = jobExecutionListenerRegisterBean;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent applicationEvent) {
        // register jobs of the current application context
        final Map<String, Job> jobs = this.applicationContext.getBeansOfType(Job.class);
        if (jobs != null) {
            for (final Map.Entry<String, Job> jobEntry : jobs.entrySet()) {
                final Job job = jobEntry.getValue();
                final String jobName = job.getName();
                final CommonJobFactory commonJobFactory = new CommonJobFactory(job, jobName);
                try {
                    this.jobRegistry.register(commonJobFactory);
                } catch (final DuplicateJobException e) {
                    log.error("Job with name: " + jobName + " is already registered!");
                }
                this.jobExecutionListenerRegisterBean.registerListener(job);
            }
        }

        // register all stored jobConfigurations
        final Collection<JobConfiguration> jobConfigurations = this.adminService.getJobConfigurations(this.jobRegistry.getJobNames());
        if (jobConfigurations != null) {
            for (final JobConfiguration jobConfiguration : jobConfigurations) {
                final String jobName = jobConfiguration.getJobName();
                if (this.jobRegistry.getJobNames().contains(jobName)) {
                    if (jobConfiguration.getJobSchedulerConfiguration() != null) {
                        this.schedulerService.registerSchedulerForJob(jobConfiguration);
                    } else if (jobConfiguration.getJobListenerConfiguration() != null) {
                        this.listenerService.registerListenerForJob(jobConfiguration);
                    }
                } else {
                    log.debug("No Job with jobName " + jobName + " is present. Registration canceled");
                }
            }
        }
        // Schedule all registered schedulers
        final Map<String, Scheduler> schedulerMap = this.applicationContext.getBeansOfType(Scheduler.class);
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
        // Schedule all registered listeners
        final Map<String, Listener> listenerMap = this.applicationContext.getBeansOfType(Listener.class);
        if (listenerMap != null) {
            for (final Map.Entry<String, Listener> listenerEntry : listenerMap.entrySet()) {
                final Listener listener = listenerEntry.getValue();
                if (ListenerStatus.ACTIVE.equals(listener.getListenerStatus())) {
                    log.debug("activating Listener bean: {}", listenerEntry.getKey());
                    listener.start();
                    log.debug("activated Listener bean: {}", listenerEntry.getKey());
                }
            }
        }
    }
}
