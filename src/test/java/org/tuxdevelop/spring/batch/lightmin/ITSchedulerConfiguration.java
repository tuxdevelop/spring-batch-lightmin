package org.tuxdevelop.spring.batch.lightmin;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.admin.scheduler.CronScheduler;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.admin.scheduler.PeriodScheduler;

@Configuration
public class ITSchedulerConfiguration {

	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private Job simpleJob;

	@Bean
	public PeriodScheduler periodScheduler() {
		final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
				10L, 10L, JobSchedulerType.PERIOD);
		final PeriodScheduler periodScheduler = new PeriodScheduler(
				TestHelper.createJobConfiguration(jobSchedulerConfiguration), simpleJob,
				new JobParametersBuilder().toJobParameters(), JobIncrementer.DATE, jobLauncher);
		return periodScheduler;
	}

	@Bean
	public CronScheduler cronScheduler() {
		final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(
				"0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
		final CronScheduler cronScheduler = new CronScheduler(
				TestHelper.createJobConfiguration(jobSchedulerConfiguration), simpleJob,
				new JobParametersBuilder().toJobParameters(), JobIncrementer.DATE, jobLauncher);
		return cronScheduler;
	}
}
