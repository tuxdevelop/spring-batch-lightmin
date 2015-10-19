package org.tuxdevelop.spring.batch.lightmin.service;

import org.springframework.beans.factory.InitializingBean;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerStatus;

/**
 * @author Marcel Becker
 * @since 0.1
 */
public interface SchedulerService extends InitializingBean {

    /**
     * Registers a {@link org.tuxdevelop.spring.batch.lightmin.admin.scheduler.Scheduler} for a given
     * {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     *
     * @param jobConfiguration the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     *                         containing the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration}
     * @return the bean name of the registered {@link org.tuxdevelop.spring.batch.lightmin.admin.scheduler.Scheduler}
     */
    String registerSchedulerForJob(JobConfiguration jobConfiguration);

    /**
     * Remove the {@link org.tuxdevelop.spring.batch.lightmin.admin.scheduler.Scheduler} with a give bean name of the
     * current Spring Context
     *
     * @param beanName name of the {@link org.tuxdevelop.spring.batch.lightmin.admin.scheduler.Scheduler} to unregister
     */
    void unregisterSchedulerForJob(String beanName);

    /**
     * Reloads the {@link org.tuxdevelop.spring.batch.lightmin.admin.scheduler.Scheduler} for a given
     * {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration} within the current Spring context
     *
     * @param jobConfiguration the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     *                         containing the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration}
     */
    void refreshSchedulerForJob(JobConfiguration jobConfiguration);

    /**
     * Triggers the scheduler of Spring Bean with the given name.
     *
     * @param beanName        name of the Spring Bean to schedule
     * @param forceScheduling if true, the scheduler will be triggered independently of the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerStatus}
     */
    void schedule(String beanName, Boolean forceScheduling);

    /**
     * terminates the {@link org.springframework.core.task.TaskExecutor} of the {@link org.tuxdevelop.spring.batch
     * .lightmin.admin.scheduler.Scheduler} for the given name
     *
     * @param beanName name of the {@link org.tuxdevelop.spring.batch.lightmin.admin.scheduler.Scheduler}
     */
    void terminate(String beanName);

    /**
     * Rerieves the current {@link SchedulerStatus} of the {@link org.tuxdevelop.spring.batch.lightmin.admin
     * .scheduler.Scheduler} for the given bean name.
     *
     * @param beanName name of the {@link org.tuxdevelop.spring.batch.lightmin.admin.scheduler.Scheduler}
     * @return The current {@link SchedulerStatus}
     */
    SchedulerStatus getSchedulerStatus(String beanName);
}
