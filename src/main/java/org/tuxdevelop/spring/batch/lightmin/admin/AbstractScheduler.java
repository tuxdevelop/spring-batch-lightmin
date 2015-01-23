package org.tuxdevelop.spring.batch.lightmin.admin;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

public abstract class AbstractScheduler {

	protected abstract void schedule();

	protected static class JobRunner implements Runnable {

		private Job job;
		private JobLauncher jobLauncher;
		private JobParameters jobParameters;

		public JobRunner(final Job job, final JobLauncher jobLauncher, final JobParameters jobParameters) {
			this.job = job;
			this.jobLauncher = jobLauncher;
			this.jobParameters = jobParameters;
		}

		// TODO exception handling
		@Override
		public void run() {
			try {
				jobLauncher.run(job, jobParameters);
			} catch (JobExecutionAlreadyRunningException e) {
				e.printStackTrace();
			} catch (JobRestartException e) {
				e.printStackTrace();
			} catch (JobInstanceAlreadyCompleteException e) {
				e.printStackTrace();
			} catch (JobParametersInvalidException e) {
				e.printStackTrace();
			}
		}

	}
}
