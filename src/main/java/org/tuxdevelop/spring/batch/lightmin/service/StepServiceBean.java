package org.tuxdevelop.spring.batch.lightmin.service;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.beans.factory.InitializingBean;

public class StepServiceBean implements StepService{

	private StepExecutionDao stepExecutionDao;

	public StepServiceBean(final StepExecutionDao stepExecutionDao) {
		this.stepExecutionDao = stepExecutionDao;
	}

	@Override
	public StepExecution getStepExecution(final JobExecution jobExecution, final Long stepExecutionId){
		return stepExecutionDao.getStepExecution(jobExecution, stepExecutionId);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		assert(stepExecutionDao != null);
	}
}
