package org.tuxdevelop.spring.batch.lightmin.support;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.tuxdevelop.spring.batch.lightmin.api.resource.ResourceToAdminMapper;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobLaunch;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

public class JobLauncherBean {

    private final JobLauncher jobLauncher;
    private final JobRegistry JobRegistry;

    public JobLauncherBean(final JobLauncher jobLauncher,
                           final JobRegistry jobRegistry) {
        this.jobLauncher = jobLauncher;
        JobRegistry = jobRegistry;
    }

    /**
     * Lauches a {@link org.springframework.batch.core.Job} with the given values of the {@link JobLaunch} parameter
     *
     * @param jobLaunch the launch information for the Job
     */
    public void launchJob(final JobLaunch jobLaunch) {
        final Job job;
        try {
            job = JobRegistry.getJob(jobLaunch.getJobName());
            final JobParameters jobParameters = ResourceToAdminMapper.map(jobLaunch.getJobParameters());
            jobLauncher.run(job, jobParameters);
        } catch (final Exception e) {
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }

    }
}
