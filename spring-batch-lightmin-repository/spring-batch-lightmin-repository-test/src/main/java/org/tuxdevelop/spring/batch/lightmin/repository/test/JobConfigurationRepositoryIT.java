package org.tuxdevelop.spring.batch.lightmin.repository.test;


import org.junit.Before;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class JobConfigurationRepositoryIT {

    private static final String APPLICATION_NAME = "test_application_name";

    public abstract JobConfigurationRepository getJobConfigurationRepository();

    public abstract ITJobConfigurationRepository getITItJdbcJobConfigurationRepository();

    @Before
    public void init() {
        this.getITItJdbcJobConfigurationRepository().clean(APPLICATION_NAME);
    }

    @Test
    public void addIT() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final Collection<JobConfiguration> allResult = this.getJobConfigurationRepository().getAllJobConfigurations(APPLICATION_NAME);
        assertThat(allResult).hasSize(2);
    }

    @Test
    public void addWithListenerIT() {
        final JobListenerConfiguration jobListenerConfiguration = DomainTestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobListenerConfiguration);
        final JobConfiguration addedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobListenerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobListenerConfiguration()).isNotNull();
        final Collection<JobConfiguration> allResult = this.getJobConfigurationRepository().getAllJobConfigurations(APPLICATION_NAME);
        assertThat(allResult).hasSize(2);
    }

    @Test
    public void getJobConfigurationIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration fetchedJobConfiguration = this.getJobConfigurationRepository().getJobConfiguration
                (addedJobConfiguration.getJobConfigurationId(), APPLICATION_NAME);
        assertThat(fetchedJobConfiguration).isEqualTo(addedJobConfiguration);
    }

    @Test
    public void getJobConfigurationWithListenerIT() throws NoSuchJobConfigurationException {
        final JobListenerConfiguration jobListenerConfiguration = DomainTestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobListenerConfiguration);
        final JobConfiguration addedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobListenerConfiguration()).isNotNull();
        final JobConfiguration fetchedJobConfiguration = this.getJobConfigurationRepository().getJobConfiguration
                (addedJobConfiguration.getJobConfigurationId(), APPLICATION_NAME);
        assertThat(fetchedJobConfiguration).isEqualTo(addedJobConfiguration);
    }

    @Test
    public void updateIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        addedJobConfiguration.setJobName("updated");
        final Map<String, Object> jobParameters = new HashMap<>();
        jobParameters.put("Double", 20.2);
        addedJobConfiguration.setJobParameters(jobParameters);
        final JobConfiguration updatedJobConfiguration = this.getJobConfigurationRepository().update(addedJobConfiguration, APPLICATION_NAME);
        assertThat(updatedJobConfiguration.getJobName()).isEqualTo("updated");
        final JobConfiguration fetchedJobConfiguration = this.getJobConfigurationRepository().getJobConfiguration
                (updatedJobConfiguration.getJobConfigurationId(), APPLICATION_NAME);
        assertThat(fetchedJobConfiguration).isEqualTo(updatedJobConfiguration);
    }

    @Test
    public void updateWithListenerIT() throws NoSuchJobConfigurationException {
        final JobListenerConfiguration jobListenerConfiguration = DomainTestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobListenerConfiguration);
        final JobConfiguration addedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobListenerConfiguration()).isNotNull();
        addedJobConfiguration.setJobName("updated");
        final JobConfiguration updatedJobConfiguration = this.getJobConfigurationRepository().update(addedJobConfiguration, APPLICATION_NAME);
        assertThat(updatedJobConfiguration.getJobName()).isEqualTo("updated");
        final JobConfiguration fetchedJobConfiguration = this.getJobConfigurationRepository().getJobConfiguration
                (updatedJobConfiguration.getJobConfigurationId(), APPLICATION_NAME);
        assertThat(fetchedJobConfiguration).isEqualTo(updatedJobConfiguration);
    }

    @Test(expected = NoSuchJobConfigurationException.class)
    public void updateJobConfigurationIdNotExistingIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        addedJobConfiguration.setJobName("updated");
        addedJobConfiguration.setJobConfigurationId(-1000L);
        this.getJobConfigurationRepository().update(addedJobConfiguration, APPLICATION_NAME);
    }

    @Test
    public void updateWithParametersIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        addedJobConfiguration.setJobName("updated");
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("test", "input");
        addedJobConfiguration.setJobParameters(parameters);
        final JobConfiguration updatedJobConfiguration = this.getJobConfigurationRepository().update(addedJobConfiguration, APPLICATION_NAME);
        assertThat(updatedJobConfiguration.getJobName()).isEqualTo("updated");
        final JobConfiguration fetchedJobConfiguration = this.getJobConfigurationRepository().getJobConfiguration
                (addedJobConfiguration.getJobConfigurationId(), APPLICATION_NAME);
        assertThat(fetchedJobConfiguration).isEqualTo(updatedJobConfiguration);
    }

    @Test
    public void updateWithListenerWithParametersIT() throws NoSuchJobConfigurationException {
        final JobListenerConfiguration jobListenerConfiguration = DomainTestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobListenerConfiguration);
        final JobConfiguration addedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobListenerConfiguration()).isNotNull();
        addedJobConfiguration.setJobName("updated");
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("test", "input");
        addedJobConfiguration.setJobParameters(parameters);
        final JobConfiguration updatedJobConfiguration = this.getJobConfigurationRepository().update(addedJobConfiguration, APPLICATION_NAME);
        assertThat(updatedJobConfiguration.getJobName()).isEqualTo("updated");
        final JobConfiguration fetchedJobConfiguration = this.getJobConfigurationRepository().getJobConfiguration(1L, APPLICATION_NAME);
        assertThat(fetchedJobConfiguration).isEqualTo(updatedJobConfiguration);
    }

    @Test
    public void getJobConfigurationsIT() throws NoSuchJobException, NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final Collection<JobConfiguration> jobConfigurations = this.getJobConfigurationRepository().getJobConfigurations("sampleJob", APPLICATION_NAME);
        assertThat(jobConfigurations.size()).isEqualTo(2);
    }

    @Test(expected = NoSuchJobException.class)
    public void getJobConfigurationsJobNameUnknownIT() throws NoSuchJobException, NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        this.getJobConfigurationRepository().getJobConfigurations("sampleJobUnknown", APPLICATION_NAME);
    }

    @Test(expected = NoSuchJobConfigurationException.class)
    public void deleteIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        this.getJobConfigurationRepository().delete(addedJobConfiguration, APPLICATION_NAME);
        this.getJobConfigurationRepository().getJobConfiguration(addedJobConfiguration.getJobConfigurationId(), APPLICATION_NAME);
    }

    @Test(expected = NoSuchJobConfigurationException.class)
    public void deleteJobConfigurationIdNotExistingIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        addedJobConfiguration.setJobConfigurationId(-100L);
        this.getJobConfigurationRepository().delete(addedJobConfiguration, APPLICATION_NAME);
    }

    @Test
    public void getJobConfigurationsByNameIT() throws NoSuchJobException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final Map<String, Object> jobParameters = new HashMap<>();
        jobParameters.put("LONG", 10L);
        jobParameters.put("DOUBLE", 20.2);
        jobParameters.put("STRING", "test");
        jobParameters.put("DATE", "2015/03/27 23:19:24:120");
        jobParameters.put("DATE", "2015/03/27");
        jobConfiguration.setJobParameters(jobParameters);
        final JobConfiguration addedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add("sampleJob");
        jobNames.add("sampleJobSecond");
        final Collection<JobConfiguration> jobConfigurations = this.getJobConfigurationRepository()
                .getAllJobConfigurationsByJobNames(jobNames, APPLICATION_NAME);
        assertThat(jobConfigurations.size()).isEqualTo(2);
    }

    @Test
    public void getAllJobConfigurationsTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfigurationFirst = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration jobConfigurationSecond = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedFirstJobConfiguration = this.getJobConfigurationRepository().add(jobConfigurationFirst, APPLICATION_NAME);
        final JobConfiguration addedSecondJobConfiguration = this.getJobConfigurationRepository().add(jobConfigurationSecond, APPLICATION_NAME);

        final Collection<JobConfiguration> jobConfigurations = this.getJobConfigurationRepository().getAllJobConfigurations(APPLICATION_NAME);
        assertThat(jobConfigurations).isNotNull();
        assertThat(jobConfigurations).hasSize(2);
        final Set<JobConfiguration> jobConfigurationSet = new HashSet<>(jobConfigurations);
        assertThat(jobConfigurationSet).contains(addedFirstJobConfiguration);
        assertThat(jobConfigurationSet).contains(addedSecondJobConfiguration);

    }

}
