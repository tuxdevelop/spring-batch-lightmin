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
     * @param jobConfigurationId
     * @return a {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     * @throws {@link org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException}
     */
    JobConfiguration getJobConfiguration(Long jobConfigurationId) throws NoSuchJobConfigurationException;

    /**
     *
     * @param jobName
     * @return
     * @throws {@link org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException}
     */
    Collection<JobConfiguration> getJobConfigurations(String jobName) throws NoSuchJobException;

    /**
     *
     * @param jobConfiguration
     * @return
     */
    JobConfiguration add(JobConfiguration jobConfiguration);

    /**
     *
     * @param jobConfiguration
     * @return
     * @throws {@link org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException}
     */
    JobConfiguration update(JobConfiguration jobConfiguration) throws NoSuchJobConfigurationException;


    /**
     *
     * @param jobConfiguration
     * @throws {@link org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException}
     */
    void delete(JobConfiguration jobConfiguration) throws NoSuchJobConfigurationException;

    /**
     *
     * @return
     */
    Collection<JobConfiguration> getAllJobConfigurations();

    Collection<JobConfiguration> getAllJobConfigurationsByJobNames(Collection<String> jobNames);
}
