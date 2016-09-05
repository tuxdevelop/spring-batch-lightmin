package org.tuxdevelop.spring.batch.lightmin.server.admin;

import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.support.ServiceEntry;

import java.util.Map;

/**
 * Embedded implementation of theb {@link AdminServerService}
 *
 * @author Marcel Becker
 * @since 0.3
 */
public class EmbeddedAdminServerServiceBean implements AdminServerService {

    private final ServiceEntry serviceEntry;

    public EmbeddedAdminServerServiceBean(final ServiceEntry serviceEntry) {
        this.serviceEntry = serviceEntry;
    }

    @Override
    public void saveJobConfiguration(final JobConfiguration jobConfiguration, final LightminClientApplication lightminClientApplication) {
        serviceEntry.saveJobConfiguration(jobConfiguration);
    }

    @Override
    public void updateJobConfiguration(final JobConfiguration jobConfiguration, final LightminClientApplication lightminClientApplication) {
        serviceEntry.updateJobConfiguration(jobConfiguration);
    }

    @Override
    public JobConfigurations getJobConfigurations(final LightminClientApplication lightminClientApplication) {
        return serviceEntry.getJobConfigurations(lightminClientApplication.getLightminClientInformation().getRegisteredJobs());
    }

    @Override
    public Map<String, JobConfigurations> getJobConfigurationsMap(final LightminClientApplication
                                                                          lightminClientApplication) {
        return serviceEntry.getJobConfigurationMap(lightminClientApplication.getLightminClientInformation()
                .getRegisteredJobs());
    }

    @Override
    public void deleteJobConfiguration(final Long jobConfigurationId, final LightminClientApplication lightminClientApplication) {
        serviceEntry.deleteJobConfiguration(jobConfigurationId);
    }

    @Override
    public JobConfiguration getJobConfiguration(final Long jobConfigurationId, final LightminClientApplication lightminClientApplication) {
        return serviceEntry.getJobConfigurationById(jobConfigurationId);
    }

    @Override
    public void startJobConfigurationScheduler(final Long jobConfigurationId, final LightminClientApplication lightminClientApplication) {
        serviceEntry.startJobConfiguration(jobConfigurationId);
    }

    @Override
    public void stopJobConfigurationScheduler(final Long jobConfigurationId, final LightminClientApplication lightminClientApplication) {
        serviceEntry.stopJobConfiguration(jobConfigurationId);
    }

}
