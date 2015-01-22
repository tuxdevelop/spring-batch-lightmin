package org.tuxdevelop.spring.batch.lightmin;

import java.util.LinkedList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.StepExecution;

public class TestHelper {

	public static Job createJob(final String jobName) {
		return new Job() {
			@Override
			public boolean isRestartable() {
				return false;
			}

			@Override
			public String getName() {
				return jobName;
			}

			@Override
			public JobParametersValidator getJobParametersValidator() {
				return null;
			}

			@Override
			public JobParametersIncrementer getJobParametersIncrementer() {
				return null;
			}

			@Override
			public void execute(JobExecution execution) {
			}
		};
	}

	public static List<JobInstance> createJobInstances(final long count, final String jobName) {
		final List<JobInstance> jobInstances = new LinkedList<JobInstance>();
		for (long i = 1; i <= count; i++) {
			final JobInstance jobInstance = new JobInstance(i, jobName);
			jobInstances.add(jobInstance);
		}
		return jobInstances;
	}

	public static JobInstance createJobInstance(final Long id, final String jobName) {
		return new JobInstance(id, jobName);
	}

	public static List<JobExecution> createJobExecutions(final long count) {
		final List<JobExecution> jobExecutions = new LinkedList<JobExecution>();
		for (long i = 1; i <= count; i++) {
			final JobExecution jobExecution = createJobExecution(i);
			jobExecutions.add(jobExecution);
		}
		return jobExecutions;
	}

	public static JobExecution createJobExecution(final Long jobExecutionId) {
		return new JobExecution(jobExecutionId);
	}

	public static StepExecution createStepExecution(final String stepName, final JobExecution jobExecution) {
		return new StepExecution(stepName, jobExecution);
	}

}
