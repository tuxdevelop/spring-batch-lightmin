package org.tuxdevelop.spring.batch.lightmin.server.admin;


import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

import java.util.Map;

public interface AdminServerService {
    void saveJobConfiguration(JobConfiguration jobConfiguration, LightminClientApplication lightminClientApplication);

    void updateJobConfiguration(JobConfiguration jobConfiguration, LightminClientApplication lightminClientApplication);

    JobConfigurations getJobConfigurations(LightminClientApplication lightminClientApplication);

    Map<String, JobConfigurations> getJobConfigurationsMap(LightminClientApplication lightminClientApplication);

    void deleteJobConfiguration(Long jobConfigurationId, LightminClientApplication lightminClientApplication);

    JobConfiguration getJobConfiguration(Long jobConfigurationId, LightminClientApplication lightminClientApplication);

    void startJobConfigurationScheduler(Long jobConfigurationId, LightminClientApplication lightminClientApplication);

    void stopJobConfigurationScheduler(Long jobConfigurationId, LightminClientApplication
            lightminClientApplication);
}
