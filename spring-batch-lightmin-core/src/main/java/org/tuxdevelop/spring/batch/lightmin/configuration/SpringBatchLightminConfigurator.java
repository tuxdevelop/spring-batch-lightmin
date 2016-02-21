package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobOperator;
import org.tuxdevelop.spring.batch.lightmin.dao.LightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;
import org.tuxdevelop.spring.batch.lightmin.service.StepService;

/**
 * @author Marcel Becker
 * @version 0.1
 */
public interface SpringBatchLightminConfigurator {

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
     * @return an instance of {@link org.tuxdevelop.spring.batch.lightmin.dao.LightminJobExecutionDao}
     */
    LightminJobExecutionDao getLightminJobExecutionDao();

    /**
     * @return the current value of configured table.prefix
     */
    String getTablePrefix();
}
