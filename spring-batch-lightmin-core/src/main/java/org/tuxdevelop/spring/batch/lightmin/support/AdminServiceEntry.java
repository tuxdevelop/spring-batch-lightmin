package org.tuxdevelop.spring.batch.lightmin.support;


import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;

import java.util.Collection;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public interface AdminServiceEntry {

    /**
     * Creates and saves a new {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration}
     *
     * @param jobConfiguration {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration} to save.
     */
    void saveJobConfiguration(JobConfiguration jobConfiguration);

    /**
     * Udpates a existing {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration}
     *
     * @param jobConfiguration {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration} to update.
     */
    void updateJobConfiguration(JobConfiguration jobConfiguration);

    /**
     * Deletes an existing {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration} of the given id.
     *
     * @param jobConfigurationId The id of the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain
     *                           .JobConfiguration} to delete.
     */
    void deleteJobConfiguration(final Long jobConfigurationId);

    /**
     * Retrieves all known {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration} for a given job
     * name
     *
     * @param jobName the given name of the job
     * @return all known {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations} for a
     * given job
     * name
     */
    JobConfigurations getJobConfigurationsByJobName(String jobName);

    /**
     * Retrieves a Map of String and all known {@link org.tuxdevelop.spring.batch.lightmin.admin.domain
     * .JobConfiguration} . The key of the maps belongs to a job name.
     *
     * @param jobNames Collection of job names get the configurations for.
     * @return all known {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration}s
     */
    Map<String, JobConfigurations> getJobConfigurationMap(final Collection<String> jobNames);

    /**
     * Retrieves all known {@link org.tuxdevelop.spring.batch.lightmin.admin.domain
     * .JobConfiguration}s.
     *
     * @param jobNames names of the {@link org.springframework.batch.core.Job}s, to get the configurations for
     * @return all known {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration}s
     */
    JobConfigurations getJobConfigurations(final Collection<String> jobNames);

    /**
     * Retrieves a {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration} for a given id.
     *
     * @param jobConfigurationId id of the {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration}
     * @return the {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration} for the id
     */
    JobConfiguration getJobConfigurationById(Long jobConfigurationId);

    /**
     * Stops the Scheduler of the {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration} of the
     * given id
     *
     * @param jobConfigurationId id of the {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration}
     */
    void stopJobConfiguration(Long jobConfigurationId);

    /**
     * Starts the Scheduler of the {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration} of the
     * given id
     *
     * @param jobConfigurationId id of the {@link org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration}
     */
    void startJobConfiguration(Long jobConfigurationId);
}
