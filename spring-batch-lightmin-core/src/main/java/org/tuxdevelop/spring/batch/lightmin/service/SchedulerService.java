package org.tuxdevelop.spring.batch.lightmin.service;

import org.springframework.beans.factory.InitializingBean;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerStatus;

public interface SchedulerService extends InitializingBean {
	/**
	 *
	 * @param jobConfiguration
	 * @return
	 */
	String registerSchedulerForJob(JobConfiguration jobConfiguration);

	/**
	 *
	 * @param beanName
	 */
	void unregisterSchedulerForJob(String beanName);

	/**
	 *
	 * @param jobConfiguration
	 */
	void refreshSchedulerForJob(JobConfiguration jobConfiguration);

	/**
	 *
	 * @param beanName
	 */
	void schedule(String beanName);

	/**
	 *
	 * @param beanName
	 */
	void terminate(String beanName);

	SchedulerStatus getSchedulerStatus(String beanName);
}
