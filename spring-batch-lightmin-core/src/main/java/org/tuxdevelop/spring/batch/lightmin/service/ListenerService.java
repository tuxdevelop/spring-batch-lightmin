package org.tuxdevelop.spring.batch.lightmin.service;

import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public interface ListenerService {
    String registerListenerForJob(JobConfiguration jobConfiguration);

    void unregisterListenerForJob(String beanName);

    void refreshListenerForJob(JobConfiguration jobConfiguration);

    void activateListener(String beanName, Boolean forceActivation);

    void terminateListener(String beanName);
}
