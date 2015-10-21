package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.dao.LightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;
import org.tuxdevelop.spring.batch.lightmin.service.StepService;

/**
 * @author Marcel Becker
 * @version 0.1
 */
public interface SpringBatchLightminConfigurator extends BatchConfigurer {

    /**
     * @return an instance of {@link org.tuxdevelop.spring.batch.lightmin.service.JobService}
     */
    JobService getJobService();

    /**
     * @return an instance of an instance of {@link org.tuxdevelop.spring.batch.lightmin.service.StepService}
     */
    StepService getStepService();

    /**
     * @return an instance of {@link org.springframework.batch.core.launch.JobOperator}
     */
    JobOperator getJobOperator();

    /**
     * @return an instance of {@link org.springframework.batch.core.configuration.JobRegistry}
     */
    JobRegistry getJobRegistry();

    /**
     * @return an instance of {@link org.springframework.batch.core.repository.dao.JobExecutionDao}
     */
    JobExecutionDao getJobExecutionDao();

    /**
     * @return an instance of {@link org.tuxdevelop.spring.batch.lightmin.dao.LightminJobExecutionDao}
     */
    LightminJobExecutionDao getLightminJobExecutionDao();

    /**
     * @return an instance of {@link org.springframework.batch.core.repository.dao.JobInstanceDao}
     */
    JobInstanceDao getJobInstanceDao();

    /**
     * @return an instance of {@link org.springframework.batch.core.repository.dao.StepExecutionDao}
     */
    StepExecutionDao getStepExecutionDao();

    /**
     * @return the current value of configured table.prefix
     */
    String getTablePrefix();
}
