package org.tuxdevelop.spring.batch.lightmin.admin.scheduler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerStatus;

import java.util.TimeZone;

@Slf4j
@Getter
public class CronScheduler extends AbstractScheduler {

	private final JobConfiguration jobConfiguration;
	private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
	private final JobSchedulerConfiguration jobSchedulerConfiguration;
	private final Job job;
	private final JobRunner jobRunner;
	private final Trigger trigger;
	private final TimeZone timeZone;

	public CronScheduler(final JobConfiguration jobConfiguration, final Job job, final JobParameters jobParameters,
			final JobIncrementer jobIncrementer, final JobLauncher jobLauncher) {
		this.jobConfiguration = jobConfiguration;
		threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(1);
		threadPoolTaskScheduler.afterPropertiesSet();
		jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
		this.job = job;
		timeZone = TimeZone.getDefault();
		trigger = new CronTrigger(jobSchedulerConfiguration.getCronExpression(), timeZone);
		jobRunner = new JobRunner(job, jobLauncher, jobParameters, jobIncrementer);
		setStatus(SchedulerStatus.INITIALIZED);
	}

	@Override
	public void schedule() {
		log.info("Scheduling: " + jobRunner.getJob().getName() + " with Parameters: "
				+ jobRunner.getJobParameters().toProperties());
		threadPoolTaskScheduler.schedule(jobRunner, trigger);
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
