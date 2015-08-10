package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;
import org.tuxdevelop.spring.batch.lightmin.service.StepService;

public interface SpringBatchLightminConfiguration extends BatchConfigurer {


    JobService getJobService();

    StepService getStepService();

    JobOperator getJobOperator();

    JobRegistry getJobRegistry();

    JobExecutionDao getJobExecutionDao();

    JobInstanceDao getJobInstanceDao();

    StepExecutionDao getStepExecutionDao();
}
