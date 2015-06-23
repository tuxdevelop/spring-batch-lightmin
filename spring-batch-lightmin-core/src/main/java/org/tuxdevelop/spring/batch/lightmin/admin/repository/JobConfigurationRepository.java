package org.tuxdevelop.spring.batch.lightmin.admin.repository;

import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.execption.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.execption.NoSuchJobException;

import java.util.Collection;

public interface JobConfigurationRepository {

    /**
     * returns the {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration} for a given jobConfigurationId
     *
     * @param jobConfigurationId
     * @return a {@link org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration}
     * @throws {@link org.tuxdevelop.spring.batch.lightmin.execption.NoSuchJobConfigurationException}
     */
    JobConfiguration getJobConfiguration(Long jobConfigurationId) throws NoSuchJobConfigurationException;

    /**
     *
     * @param jobName
     * @return
     * @throws {@link org.tuxdevelop.spring.batch.lightmin.execption.NoSuchJobException}
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
     * @throws {@link org.tuxdevelop.spring.batch.lightmin.execption.NoSuchJobConfigurationException}
     */
    JobConfiguration update(JobConfiguration jobConfiguration) throws NoSuchJobConfigurationException;


    /**
     *
     * @param jobConfiguration
     * @throws {@link org.tuxdevelop.spring.batch.lightmin.execption.NoSuchJobConfigurationException}
     */
    void delete(JobConfiguration jobConfiguration) throws NoSuchJobConfigurationException;

    /**
     *
     * @return
     */
    Collection<JobConfiguration> getAllJobConfigurations();

}
