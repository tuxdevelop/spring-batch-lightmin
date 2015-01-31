package org.tuxdevelop.spring.batch.lightmin.admin;

import java.util.Date;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Slf4j
@Getter
public class PeriodScheduler extends AbstractScheduler {

	private final JobConfiguration jobConfiguration;
	private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
	private final JobSchedulerConfiguration jobSchedulerConfiguration;
	private JobLauncher jobLauncher;
	private final Job job;
	private final JobRunner jobRunner;
	private JobParameters jobParameters;

	public PeriodScheduler(final JobConfiguration jobConfiguration, final Job job, final JobParameters jobParameters,
			final JobIncremeter jobIncremeter, final JobLauncher jobLauncher) {
		this.jobConfiguration = jobConfiguration;
		threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(1);
		threadPoolTaskScheduler.afterPropertiesSet();
		jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
		this.job = job;
		jobRunner = new JobRunner(job, jobLauncher, jobParameters, jobIncremeter);
		setStatus(SchedulerStatus.INITIALIZED);
	}

	@Override
	public void schedule() {
		final Date initialDelay = new Date(System.currentTimeMillis() + jobSchedulerConfiguration.getInitialDelay());
		log.info("Scheduling: " + jobRunner.getJob().getName() + " with Parameters: "
				+ jobRunner.getJobParameters().toProperties());
		threadPoolTaskScheduler.scheduleWithFixedDelay(jobRunner, initialDelay,
				jobSchedulerConfiguration.getFixedDelay());
		setStatus(SchedulerStatus.RUNNING);
	}

	@Override
	protected void terminate() {
		threadPoolTaskScheduler.shutdown();
		while (threadPoolTaskScheduler.getActiveCount() > 0) {
			setStatus(SchedulerStatus.IN_TERMINATION);
		}
		threadPoolTaskScheduler.initialize();
		setStatus(SchedulerStatus.STOPPED);

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

}
