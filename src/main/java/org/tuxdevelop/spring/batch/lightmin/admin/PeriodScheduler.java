package org.tuxdevelop.spring.batch.lightmin.admin;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.Getter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

@Getter
public class PeriodScheduler extends AbstractScheduler{

	private JobConfiguration jobConfiguration;
	private ScheduledExecutorService executorService;
	private JobSchedulerConfiguration jobSchedulerConfiguration;
	private JobLauncher jobLauncher;
	private Job job;
	private JobRunner jobRunner;
	private JobParameters jobParameters;

	// TODO null checks
	public PeriodScheduler(final JobConfiguration jobConfiguration, final Job job, final JobParameters jobParameters,
			final JobLauncher jobLauncher) {
		this.jobConfiguration = jobConfiguration;
		this.executorService = new ScheduledThreadPoolExecutor(1);
		this.jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
		this.job = job;
		this.jobRunner = new JobRunner(job, jobLauncher, jobParameters);
	}

	@Override
	public void schedule() {
		executorService.scheduleWithFixedDelay(jobRunner, jobSchedulerConfiguration.getInitialDelay(),
				jobSchedulerConfiguration.getFixedDelay(), TimeUnit.MILLISECONDS);
	}



}
