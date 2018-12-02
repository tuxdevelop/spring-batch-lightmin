package org.tuxdevelop.spring.batch.lightmin.repository;

import org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;

import java.util.Collection;

/**
 * @author Marcel Becker
 * @since 0.1
 */
public interface JobConfigurationRepository {

    /**
     * returns the {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration} for a given
     * jobConfigurationId for a Spring Batch Lightmin application identified by the applicationName
     *
     * @param jobConfigurationId the technical id of the {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration}
     * @param applicationName    the nane of the lightmin application
     * @return a {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration}
     * @throws NoSuchJobConfigurationException - {@link org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException}
     */
    JobConfiguration getJobConfiguration(Long jobConfigurationId, String applicationName) throws NoSuchJobConfigurationException;

    /**
     * returns the {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration}s for a given job name
     * for a Spring Batch Lightmin application identified by the applicationName.
     *
     * @param jobName         name of the {@link org.springframework.batch.core.Job}
     * @param applicationName the nane of the lightmin application
     * @return a Collection of {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration}s
     * @throws NoSuchJobException - {@link org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException}
     */
    Collection<JobConfiguration> getJobConfigurations(String jobName, String applicationName) throws NoSuchJobException, NoSuchJobConfigurationException;

    /**
     * adds a new {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration}
     *
     * @param jobConfiguration the {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration} to add
     * @param applicationName  the nane of the lightmin application
     * @return the added {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration}
     */
    JobConfiguration add(JobConfiguration jobConfiguration, String applicationName);

    /**
     * updates an existing {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration}
     *
     * @param jobConfiguration {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration} to update
     * @param applicationName  the nane of the lightmin application
     * @return the updated {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration}
     * @throws NoSuchJobConfigurationException -
     *                                         {@link org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException}
     */
    JobConfiguration update(JobConfiguration jobConfiguration, String applicationName) throws NoSuchJobConfigurationException;


    /**
     * deletes an existing {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration}
     *
     * @param jobConfiguration {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration} to delete.
     * @param applicationName  the nane of the lightmin application
     * @throws NoSuchJobConfigurationException -
     *                                         {@link org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException}
     */
    void delete(JobConfiguration jobConfiguration, String applicationName) throws NoSuchJobConfigurationException;

    /**
     * returns all existings {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration}s within the
     * repository.
     *
     * @param applicationName the nane of the lightmin application
     * @return a Collection of {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration}s
     */
    Collection<JobConfiguration> getAllJobConfigurations(String applicationName);

    /**
     * returns all {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration}s which belongs to the
     * given job names.
     *
     * @param jobNames        names of the jobs which
     *                        {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration}s should be returned.
     * @param applicationName the nane of the lightmin application
     * @return a Collection of {@link org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration}s
     */
    Collection<JobConfiguration> getAllJobConfigurationsByJobNames(Collection<String> jobNames, String applicationName);
}
