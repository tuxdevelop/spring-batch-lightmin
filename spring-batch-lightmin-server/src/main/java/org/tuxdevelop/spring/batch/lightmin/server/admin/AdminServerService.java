package org.tuxdevelop.spring.batch.lightmin.server.admin;


import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

import java.util.Map;

public interface AdminServerService {

    /**
     * @param jobConfiguration
     * @param lightminClientApplication
     */
    void saveJobConfiguration(JobConfiguration jobConfiguration, LightminClientApplication lightminClientApplication);

    /**
     * @param jobConfiguration
     * @param lightminClientApplication
     */
    void updateJobConfiguration(JobConfiguration jobConfiguration, LightminClientApplication lightminClientApplication);

    /**
     * @param lightminClientApplication
     * @return
     */
    JobConfigurations getJobConfigurations(LightminClientApplication lightminClientApplication);

    /**
     * @param lightminClientApplication
     * @return
     */
    Map<String, JobConfigurations> getJobConfigurationsMap(LightminClientApplication lightminClientApplication);

    /**
     * @param jobConfigurationId
     * @param lightminClientApplication
     */
    void deleteJobConfiguration(Long jobConfigurationId, LightminClientApplication lightminClientApplication);

    /**
     * @param jobConfigurationId
     * @param lightminClientApplication
     * @return
     */
    JobConfiguration getJobConfiguration(Long jobConfigurationId, LightminClientApplication lightminClientApplication);

    /**
     * @param jobConfigurationId
     * @param lightminClientApplication
     */
    void startJobConfigurationScheduler(Long jobConfigurationId, LightminClientApplication lightminClientApplication);

    /**
     * @param jobConfigurationId
     * @param lightminClientApplication
     */
    void stopJobConfigurationScheduler(Long jobConfigurationId, LightminClientApplication lightminClientApplication);
}
