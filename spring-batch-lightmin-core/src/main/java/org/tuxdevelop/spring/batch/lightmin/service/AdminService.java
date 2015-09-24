package org.tuxdevelop.spring.batch.lightmin.service;

import org.springframework.beans.factory.InitializingBean;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;

import java.util.Collection;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.1
 */
public interface AdminService extends InitializingBean {

    /**
     * Creates and saves a new {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     *
     * @param jobConfiguration {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration} to save.
     */
    void saveJobConfiguration(JobConfiguration jobConfiguration);

    /**
     * Udpates a existing {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     *
     * @param jobConfiguration {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration} to update.
     */
    void updateJobConfiguration(JobConfiguration jobConfiguration);

    /**
     * Deletes an existing {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration} of the given id.
     *
     * @param jobConfigurationId The id of the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain
     *                           .JobConfiguration} to delete.
     */
    void deleteJobConfiguration(final Long jobConfigurationId);

    /**
     * Retrieves all known {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration} for a given job
     * name
     *
     * @param jobName the given name of the job
     * @return all known {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration} for a given job
     * name
     */
    Collection<JobConfiguration> getJobConfigurationsByJobName(String jobName);

    /**
     * Retrieves a Map of String and all known {@link org.tuxdevelop.spring.batch.lightmin.admin.domain
     * .JobConfiguration} . The key of the maps belongs to a job name.
     *
     * @return all known {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}s
     */
    Map<String, Collection<JobConfiguration>> getJobConfigurationMap(final Collection<String> jobNames);

    /**
     * Retrieves all known {@link org.tuxdevelop.spring.batch.lightmin.admin.domain
     * .JobConfiguration}s.
     *
     * @param jobNames names of the {@link org.springframework.batch.core.Job}s, to get the configurations for
     *
     * @return all known {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}s
     */
    Collection<JobConfiguration> getJobConfigurations(final Collection<String> jobNames);

    /**
     * Retrieves a {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration} for a given id.
     *
     * @param jobConfigurationId id of the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     * @return the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration} for the id
     */
    JobConfiguration getJobConfigurationById(Long jobConfigurationId);

    /**
     * Stops the Scheduler of the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration} of the
     * given id
     *
     * @param jobConfigurationId id of the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     */
    void stopJobConfigurationScheduler(Long jobConfigurationId);

    /**
     * Starts the Scheduler of the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration} of the
     * given id
     *
     * @param jobConfigurationId id of the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     */
    void startJobConfigurationScheduler(Long jobConfigurationId);
}
