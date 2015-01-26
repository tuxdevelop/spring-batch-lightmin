package org.tuxdevelop.spring.batch.lightmin.admin;

import java.util.TimeZone;

import lombok.Getter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

@Getter
public class CronScheduler extends AbstractScheduler {

	private JobConfiguration jobConfiguration;
	private TaskScheduler executorService;
	private JobSchedulerConfiguration jobSchedulerConfiguration;
	private JobLauncher jobLauncher;
	private Job job;
	private JobRunner jobRunner;
	private JobParameters jobParameters;
	private Trigger trigger;
	private TimeZone timeZone;

	// TODO null checks
	public CronScheduler(final JobConfiguration jobConfiguration, final Job job, final JobParameters jobParameters,
			final JobIncremeter jobIncremeter, final JobLauncher jobLauncher) {
		this.jobConfiguration = jobConfiguration;
		this.executorService = new ThreadPoolTaskScheduler();
		this.jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
		this.job = job;
		this.timeZone = TimeZone.getDefault();
		this.trigger = new CronTrigger(jobSchedulerConfiguration.getCronExpression(), timeZone);
		this.jobRunner = new JobRunner(job, jobLauncher, jobParameters, jobIncremeter);
	}

	@Override
	public void schedule() {
		executorService.schedule(jobRunner, trigger);
	}
}
