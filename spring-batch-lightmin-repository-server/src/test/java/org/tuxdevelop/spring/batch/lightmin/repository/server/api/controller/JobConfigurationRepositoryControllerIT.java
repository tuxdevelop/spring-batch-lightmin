package org.tuxdevelop.spring.batch.lightmin.repository.server.api.controller;

import org.junit.Before;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;
import org.tuxdevelop.spring.batch.lightmin.test.util.ITJobConfigurationRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;


public abstract class JobConfigurationRepositoryControllerIT {

    private static final String APPLICATION_NAME = "sample_application";

    @Test
    public void testGetJobConfiguration() {
        final JobConfiguration savedJobConfiguration = this.createNewJobConfiguration();
        try {
            final JobConfiguration response = this.getJobConfigurationRepository().getJobConfiguration(savedJobConfiguration.getJobConfigurationId(), APPLICATION_NAME);
            assertThat(response).isEqualTo(savedJobConfiguration);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetJobConfigurations() {
        final JobConfiguration jobConfiguration = this.createNewJobConfiguration();
        try {
            final Collection<JobConfiguration> response = this.getJobConfigurationRepository().getJobConfigurations("sampleJob", APPLICATION_NAME);
            assertThat(response).isNotNull();
            assertThat(response).isNotEmpty();
            assertThat(response).contains(jobConfiguration);
        } catch (final NoSuchJobException | NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdate() {
        final JobConfiguration jobConfiguration = this.createNewJobConfiguration();
        try {
            final String newJobName = "updated_job_name";
            jobConfiguration.setJobName(newJobName);
            final JobConfiguration updateResponse = this.getJobConfigurationRepository().update(jobConfiguration, APPLICATION_NAME);
            final Collection<JobConfiguration> response = this.getJobConfigurationRepository().getJobConfigurations("updated_job_name", APPLICATION_NAME);
            assertThat(response).isNotNull();
            assertThat(response).isNotEmpty();
            assertThat(response).contains(updateResponse);
        } catch (final NoSuchJobException | NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDelete() {
        final JobConfiguration jobConfiguration = this.createNewJobConfiguration();
        try {
            this.getJobConfigurationRepository().delete(jobConfiguration, APPLICATION_NAME);
            final Collection<JobConfiguration> response = this.getJobConfigurationRepository().getJobConfigurations("sampleJob", APPLICATION_NAME);
            assertThat(response).isNotNull();
            assertThat(response.contains(jobConfiguration)).isFalse();
        } catch (final NoSuchJobException | NoSuchJobConfigurationException e) {
            //ok, jobConfiguration is deleted
        }
    }

    @Test
    public void testGetAllJobConfigurations() {
        final JobConfiguration jobConfiguration = this.createNewJobConfiguration();
        final Collection<JobConfiguration> response = this.getJobConfigurationRepository().getAllJobConfigurations(APPLICATION_NAME);
        assertThat(response).isNotNull();
        assertThat(response).isNotEmpty();
        assertThat(response).contains(jobConfiguration);
    }

    @Test
    public void testGetAllJobConfigurationsByJobNames() {
        final JobConfiguration jobConfiguration = this.createNewJobConfiguration();
        final JobConfiguration secondJobConfiguration = this.createNewJobConfiguration();
        final List<String> jobNames = new ArrayList<>();
        jobNames.add("sampleJob");
        jobNames.add("otherJob");
        try {
            final String newJobName = "otherJob";
            jobConfiguration.setJobName(newJobName);
            final JobConfiguration updateResponse = this.getJobConfigurationRepository().update(jobConfiguration, APPLICATION_NAME);
            final Collection<JobConfiguration> response = this.getJobConfigurationRepository()
                    .getAllJobConfigurationsByJobNames(jobNames, APPLICATION_NAME);
            assertThat(response).isNotNull();
            assertThat(response).isNotEmpty();
            assertThat(response).contains(updateResponse);
            assertThat(response).contains(secondJobConfiguration);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
    }

    private JobConfiguration createNewJobConfiguration() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null, 1000L, 100L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("mySampleBean_" + System.currentTimeMillis());
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        return this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
    }

    public abstract JobConfigurationRepository getJobConfigurationRepository();

    public abstract ITJobConfigurationRepository getITItJdbcJobConfigurationRepository();

    @Before
    public void init() {
        this.getITItJdbcJobConfigurationRepository().clean(APPLICATION_NAME);
    }
}
