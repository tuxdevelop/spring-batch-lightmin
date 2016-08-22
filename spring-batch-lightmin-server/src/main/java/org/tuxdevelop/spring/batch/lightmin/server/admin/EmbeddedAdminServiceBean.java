package org.tuxdevelop.spring.batch.lightmin.server.admin;

import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.support.AdminServiceEntry;

import java.util.Collection;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public class EmbeddedAdminServiceBean implements AdminServiceEntry {
    @Override
    public void saveJobConfiguration(final JobConfiguration jobConfiguration) {

    }

    @Override
    public void updateJobConfiguration(final JobConfiguration jobConfiguration) {

    }

    @Override
    public void deleteJobConfiguration(final Long jobConfigurationId) {

    }

    @Override
    public JobConfigurations getJobConfigurationsByJobName(final String jobName) {
        return null;
    }

    @Override
    public Map<String, JobConfigurations> getJobConfigurationMap(final Collection<String> jobNames) {
        return null;
    }

    @Override
    public JobConfigurations getJobConfigurations(final Collection<String> jobNames) {
        return null;
    }

    @Override
    public JobConfiguration getJobConfigurationById(final Long jobConfigurationId) {
        return null;
    }

    @Override
    public void stopJobConfigurationScheduler(final Long jobConfigurationId) {

    }

    @Override
    public void startJobConfigurationScheduler(final Long jobConfigurationId) {

    }
}
