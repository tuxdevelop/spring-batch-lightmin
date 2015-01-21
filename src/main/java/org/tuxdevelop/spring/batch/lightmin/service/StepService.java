package org.tuxdevelop.spring.batch.lightmin.service;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.InitializingBean;

public interface StepService extends InitializingBean {

	/**
	 *
	 * @param jobExecution
	 * @param stepExecutionId
	 * @return
	 */
	StepExecution getStepExecution(JobExecution jobExecution,
			Long stepExecutionId);

	/**
	 *
	 * @param jobExecution
	 */
	void attachStepExecutions(JobExecution jobExecution);

}
