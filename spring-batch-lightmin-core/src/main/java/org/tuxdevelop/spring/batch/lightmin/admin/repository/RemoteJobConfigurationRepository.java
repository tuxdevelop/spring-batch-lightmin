package org.tuxdevelop.spring.batch.lightmin.admin.repository;

import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;

import java.util.Collection;

/**
 * @author Marcel Becker
 * @since 0.4
 */
public class RemoteJobConfigurationRepository implements JobConfigurationRepository{


    private final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;

    public RemoteJobConfigurationRepository(SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties) {
        this.springBatchLightminConfigurationProperties = springBatchLightminConfigurationProperties;
    }

    @Override
    public JobConfiguration getJobConfiguration(Long jobConfigurationId, String applicationName) throws NoSuchJobConfigurationException {
        return null;
    }

    @Override
    public Collection<JobConfiguration> getJobConfigurations(String jobName, String applicationName) throws NoSuchJobException, NoSuchJobConfigurationException {
        return null;
    }

    @Override
    public JobConfiguration add(JobConfiguration jobConfiguration, String applicationName) {
        return null;
    }

    @Override
    public JobConfiguration update(JobConfiguration jobConfiguration, String applicationName) throws NoSuchJobConfigurationException {
        return null;
    }

    @Override
    public void delete(JobConfiguration jobConfiguration, String applicationName) throws NoSuchJobConfigurationException {

    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurations(String applicationName) {
        return null;
    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurationsByJobNames(Collection<String> jobNames, String applicationName) {
        return null;
    }
}
