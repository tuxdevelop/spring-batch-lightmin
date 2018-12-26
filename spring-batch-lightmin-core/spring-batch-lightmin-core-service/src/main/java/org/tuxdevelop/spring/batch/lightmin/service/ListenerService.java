package org.tuxdevelop.spring.batch.lightmin.service;

import org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public interface ListenerService extends JobLauncherService {

    /**
     * Registers a {@link org.tuxdevelop.spring.batch.lightmin.domain.JobListenerConfiguration}
     * of a given {@link JobConfiguration} within the spring context
     *
     * @param jobConfiguration the jobConfiguration to register
     * @return the name of the created spring bean
     */
    String registerListenerForJob(JobConfiguration jobConfiguration);

    /**
     * Unregisters a {@link org.tuxdevelop.spring.batch.lightmin.admin.listener.Listener} of a given bean name
     *
     * @param beanName the name of the spring bean
     */
    void unregisterListenerForJob(String beanName);

    /**
     * * Refreshes a {@link org.tuxdevelop.spring.batch.lightmin.domain.JobListenerConfiguration}
     * of a given {@link JobConfiguration} within the spring context
     *
     * @param jobConfiguration
     */
    void refreshListenerForJob(JobConfiguration jobConfiguration);

    /**
     * Starts the {@link org.springframework.integration.dsl.IntegrationFlow} of a
     * {@link org.tuxdevelop.spring.batch.lightmin.admin.listener.Listener} bean.
     *
     * @param beanName        the name of the Listener bean
     * @param forceActivation if true, an already running IntegrationFlow will be started.
     */
    void activateListener(String beanName, Boolean forceActivation);

    /**
     * Stops the {@link org.springframework.integration.dsl.IntegrationFlow} of a
     * {@link org.tuxdevelop.spring.batch.lightmin.admin.listener.Listener} bean.
     *
     * @param beanName
     */
    void terminateListener(String beanName);
}
