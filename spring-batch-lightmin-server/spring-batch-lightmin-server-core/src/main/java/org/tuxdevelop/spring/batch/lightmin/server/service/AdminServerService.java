package org.tuxdevelop.spring.batch.lightmin.server.service;


import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

import java.util.Map;

/**
 * @author Marcel Becker
 * @see EmbeddedAdminServerService
 * @see RemoteAdminServerService
 * @since 0.3
 */
public interface AdminServerService {

    /**
     * Creates and saves a new {@link JobConfiguration}
     * for a given {@link LightminClientApplication}
     *
     * @param jobConfiguration          the JobConfiguration
     * @param lightminClientApplication the LightminClientApplication
     */
    void saveJobConfiguration(JobConfiguration jobConfiguration, LightminClientApplication lightminClientApplication);

    /**
     * Updates and saves a new {@link JobConfiguration}
     *
     * @param jobConfiguration          the JobConfiguration
     * @param lightminClientApplication the LightminClientApplication
     */
    void updateJobConfiguration(JobConfiguration jobConfiguration, LightminClientApplication lightminClientApplication);

    /**
     * Retrieves all {@link JobConfigurations}
     * for a given {@link LightminClientApplication}
     *
     * @param lightminClientApplication the LightminClientApplication
     * @return the JobConfigurations
     */
    JobConfigurations getJobConfigurations(LightminClientApplication lightminClientApplication);

    /**
     * Retrieves a map of jobName and {@link JobConfigurations}
     * for a given {@link LightminClientApplication}
     *
     * @param lightminClientApplication the LightminClientApplication
     * @return
     */
    Map<String, JobConfigurations> getJobConfigurationsMap(LightminClientApplication lightminClientApplication);

    /**
     * Deletes a {@link JobConfiguration} of a given id
     * for a given {@link LightminClientApplication}
     *
     * @param jobConfigurationId        the id of JobConfiguration
     * @param lightminClientApplication the LightminClientApplication
     */
    void deleteJobConfiguration(Long jobConfigurationId, LightminClientApplication lightminClientApplication);

    /**
     * Retrieves a {@link JobConfiguration} of a given id
     * for a given {@link LightminClientApplication}
     *
     * @param jobConfigurationId        the id of the JobConfiguration
     * @param lightminClientApplication the LightminClientApplication
     * @return the JobConfiguration
     */
    JobConfiguration getJobConfiguration(Long jobConfigurationId, LightminClientApplication lightminClientApplication);

    /**
     * Starts a {@link JobConfiguration} of a given id
     * for a given {@link LightminClientApplication}
     *
     * @param jobConfigurationId
     * @param lightminClientApplication the LightminClientApplication
     */
    void startJobConfigurationScheduler(Long jobConfigurationId, LightminClientApplication lightminClientApplication);

    /**
     * Stops a {@link JobConfiguration} of a given id
     * for a given {@link LightminClientApplication}
     *
     * @param jobConfigurationId
     * @param lightminClientApplication the LightminClientApplication
     */
    void stopJobConfigurationScheduler(Long jobConfigurationId, LightminClientApplication lightminClientApplication);
}
