package org.tuxdevelop.spring.batch.lightmin.admin;

import java.util.Date;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractScheduler implements InitializingBean {

	@Getter
	@Setter
	private SchedulerStatus status;

	protected abstract void schedule();

	protected abstract void terminate();

	protected static class JobRunner implements Runnable {

		@Getter
		private final Job job;
		private final JobLauncher jobLauncher;
		@Getter
		private final JobParameters jobParameters;
		private final JobIncremeter jobIncremeter;

		public JobRunner(final Job job, final JobLauncher jobLauncher, final JobParameters jobParameters,
				final JobIncremeter jobIncremeter) {
			this.job = job;
			this.jobLauncher = jobLauncher;
			this.jobParameters = jobParameters;
			this.jobIncremeter = jobIncremeter;
			attacheJobIncremeter(jobParameters);
		}

		// TODO exception handling
		@Override
		public void run() {
			try {
				jobLauncher.run(job, jobParameters);
			} catch (final JobExecutionAlreadyRunningException e) {
				e.printStackTrace();
			} catch (final JobRestartException e) {
				e.printStackTrace();
			} catch (final JobInstanceAlreadyCompleteException e) {
				e.printStackTrace();
			} catch (final JobParametersInvalidException e) {
				e.printStackTrace();
			}
		}

		private void attacheJobIncremeter(JobParameters jobParameters) {
			if (jobParameters == null) {
				jobParameters = new JobParametersBuilder().toJobParameters();
			}
			if (JobIncremeter.DATE.equals(jobIncremeter)) {
				final Map<String, JobParameter> stringJobParameterMap = jobParameters.getParameters();
				stringJobParameterMap.put(JobIncremeter.DATE.getIncremeterIdentifier(), new JobParameter(new Date()));
				jobParameters = new JobParameters(stringJobParameterMap);
			}
		}

	}
}
