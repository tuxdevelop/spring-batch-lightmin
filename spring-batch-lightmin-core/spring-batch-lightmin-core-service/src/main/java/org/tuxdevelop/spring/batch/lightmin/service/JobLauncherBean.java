package org.tuxdevelop.spring.batch.lightmin.service;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobLaunch;
import org.tuxdevelop.spring.batch.lightmin.client.api.ResourceToDomainMapper;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminCoreConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.event.JobExecutionEvent;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

public class JobLauncherBean implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    private final JobLauncher jobLauncher;
    private final JobRegistry JobRegistry;
    private final SpringBatchLightminCoreConfigurationProperties springBatchLightminCoreConfigurationProperties;

    public JobLauncherBean(final JobLauncher jobLauncher,
                           final JobRegistry jobRegistry,
                           final SpringBatchLightminCoreConfigurationProperties properties) {
        this.jobLauncher = jobLauncher;
        this.JobRegistry = jobRegistry;
        this.springBatchLightminCoreConfigurationProperties = properties;
    }

    /**
     * Lauches a {@link Job} with the given values of the {@link JobLaunch} parameter
     *
     * @param jobLaunch the launch information for the Job
     */
    public void launchJob(final JobLaunch jobLaunch) {
        final Job job;
        try {
            job = this.JobRegistry.getJob(jobLaunch.getJobName());
            final JobParameters jobParameters = ResourceToDomainMapper.map(jobLaunch.getJobParameters());
            final JobExecution jobExecution = this.jobLauncher.run(job, jobParameters);
            final JobExecutionEvent jobExecutionEvent =
                    new JobExecutionEvent(jobExecution, this.springBatchLightminCoreConfigurationProperties.getApplicationName());
            this.applicationEventPublisher.publishEvent(jobExecutionEvent);
        } catch (final Exception e) {
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }

    }

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
