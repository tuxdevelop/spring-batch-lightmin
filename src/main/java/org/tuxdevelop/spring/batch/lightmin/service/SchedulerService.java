package org.tuxdevelop.spring.batch.lightmin.service;


import org.springframework.beans.factory.InitializingBean;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;

public interface SchedulerService extends InitializingBean{
    /**
     *
     * @param jobConfiguration
     * @return
     */
    String registerSchedulerForJob(JobConfiguration jobConfiguration);
}
