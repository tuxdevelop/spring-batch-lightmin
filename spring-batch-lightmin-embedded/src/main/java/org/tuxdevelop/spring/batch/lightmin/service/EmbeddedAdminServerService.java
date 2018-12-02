package org.tuxdevelop.spring.batch.lightmin.service;

import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.service.AdminServerService;

import java.util.Map;

/**
 * Embedded implementation of the {@link AdminServerService}
 *
 * @author Marcel Becker
 * @since 0.3
 */
public class EmbeddedAdminServerService implements AdminServerService {

    private final ServiceEntry serviceEntry;

    public EmbeddedAdminServerService(final ServiceEntry serviceEntry) {
        this.serviceEntry = serviceEntry;
    }

    @Override
    public void saveJobConfiguration(final JobConfiguration jobConfiguration, final LightminClientApplication lightminClientApplication) {
        this.serviceEntry.saveJobConfiguration(jobConfiguration);
    }

    @Override
    public void updateJobConfiguration(final JobConfiguration jobConfiguration, final LightminClientApplication lightminClientApplication) {
        this.serviceEntry.updateJobConfiguration(jobConfiguration);
    }

    @Override
    public JobConfigurations getJobConfigurations(final LightminClientApplication lightminClientApplication) {
        return this.serviceEntry.getJobConfigurations(lightminClientApplication.getLightminClientInformation().getRegisteredJobs());
    }

    @Override
    public Map<String, JobConfigurations> getJobConfigurationsMap(final LightminClientApplication
                                                                          lightminClientApplication) {
        return this.serviceEntry.getJobConfigurationMap(lightminClientApplication.getLightminClientInformation()
                .getRegisteredJobs());
    }

    @Override
    public void deleteJobConfiguration(final Long jobConfigurationId, final LightminClientApplication lightminClientApplication) {
        this.serviceEntry.deleteJobConfiguration(jobConfigurationId);
    }

    @Override
    public JobConfiguration getJobConfiguration(final Long jobConfigurationId, final LightminClientApplication lightminClientApplication) {
        return this.serviceEntry.getJobConfigurationById(jobConfigurationId);
    }

    @Override
    public void startJobConfigurationScheduler(final Long jobConfigurationId, final LightminClientApplication lightminClientApplication) {
        this.serviceEntry.startJobConfiguration(jobConfigurationId);
    }

    @Override
    public void stopJobConfigurationScheduler(final Long jobConfigurationId, final LightminClientApplication lightminClientApplication) {
        this.serviceEntry.stopJobConfiguration(jobConfigurationId);
    }

}
