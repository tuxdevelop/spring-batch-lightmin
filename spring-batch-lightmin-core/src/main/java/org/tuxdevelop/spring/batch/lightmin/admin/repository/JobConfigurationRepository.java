package org.tuxdevelop.spring.batch.lightmin.admin.repository;

import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;

import java.util.Collection;

/**
 * @author Marcel Becker
 * @since 0.1
 */
public interface JobConfigurationRepository {

    /**
     * returns the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration} for a given jobConfigurationId
     *
     * @param jobConfigurationId the technical id of the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     * @return a {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     * @throws NoSuchJobConfigurationException - {@link org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException}
     */
    JobConfiguration getJobConfiguration(Long jobConfigurationId) throws NoSuchJobConfigurationException;

    /**
     * returns the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}s for a given job name.
     *
     * @param jobName name of the {@link org.springframework.batch.core.Job}
     * @return a Collection of {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}s
     * @throws NoSuchJobException - {@link org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException}
     */
    Collection<JobConfiguration> getJobConfigurations(String jobName) throws NoSuchJobException;

    /**
     * adds a new {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     *
     * @param jobConfiguration the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration} to add
     * @return the added {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     */
    JobConfiguration add(JobConfiguration jobConfiguration);

    /**
     * updates an existing {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     *
     * @param jobConfiguration {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration} to update
     * @return the updated {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     * @throws NoSuchJobConfigurationException -
     *                                         {@link org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException}
     */
    JobConfiguration update(JobConfiguration jobConfiguration) throws NoSuchJobConfigurationException;


    /**
     * deletes an existing {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     *
     * @param jobConfiguration {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration} to delete.
     * @throws NoSuchJobConfigurationException -
     *                                         {@link org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException}
     */
    void delete(JobConfiguration jobConfiguration) throws NoSuchJobConfigurationException;

    /**
     * returns all existings {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}s within the
     * repository.
     *
     * @return a Collection of {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}s
     */
    Collection<JobConfiguration> getAllJobConfigurations();

    /**
     * returns all {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}s which belongs to the
     * given job names.
     *
     * @param jobNames names of the jobs which
     *                 {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}s should be returned.
     * @return a Collection of {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}s
     */
    Collection<JobConfiguration> getAllJobConfigurationsByJobNames(Collection<String> jobNames);
}
