package org.tuxdevelop.spring.batch.lightmin.admin.repository;


import org.junit.Before;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.test.util.ITJobConfigurationRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class JobConfigurationRepositoryIT {

    abstract JobConfigurationRepository getJobConfigurationRepository();

    abstract ITJobConfigurationRepository getITItJdbcJobConfigurationRepository();

    @Before
    public void init() {
        getITItJdbcJobConfigurationRepository().clean();
    }

    @Test
    public void addIT() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final Collection<JobConfiguration> allResult = getJobConfigurationRepository().getAllJobConfigurations();
        assertThat(allResult).hasSize(2);
    }

    @Test
    public void addWithListenerIT() {
        final JobListenerConfiguration jobListenerConfiguration = TestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobListenerConfiguration);
        final JobConfiguration addedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobListenerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobListenerConfiguration()).isNotNull();
        final Collection<JobConfiguration> allResult = getJobConfigurationRepository().getAllJobConfigurations();
        assertThat(allResult).hasSize(2);
    }

    @Test
    public void getJobConfigurationIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration fetchedJobConfiguration = getJobConfigurationRepository().getJobConfiguration
                (addedJobConfiguration.getJobConfigurationId());
        assertThat(fetchedJobConfiguration).isEqualTo(addedJobConfiguration);
    }

    @Test
    public void getJobConfigurationWithListenerIT() throws NoSuchJobConfigurationException {
        final JobListenerConfiguration jobListenerConfiguration = TestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobListenerConfiguration);
        final JobConfiguration addedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobListenerConfiguration()).isNotNull();
        final JobConfiguration fetchedJobConfiguration = getJobConfigurationRepository().getJobConfiguration
                (addedJobConfiguration.getJobConfigurationId());
        assertThat(fetchedJobConfiguration).isEqualTo(addedJobConfiguration);
    }

    @Test
    public void updateIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        addedJobConfiguration.setJobName("updated");
        final Map<String, Object> jobParameters = new HashMap<>();
        jobParameters.put("Double", 20.2);
        addedJobConfiguration.setJobParameters(jobParameters);
        final JobConfiguration updatedJobConfiguration = getJobConfigurationRepository().update(addedJobConfiguration);
        assertThat(updatedJobConfiguration.getJobName()).isEqualTo("updated");
        final JobConfiguration fetchedJobConfiguration = getJobConfigurationRepository().getJobConfiguration
                (updatedJobConfiguration.getJobConfigurationId());
        assertThat(fetchedJobConfiguration).isEqualTo(updatedJobConfiguration);
    }

    @Test
    public void updateWithListenerIT() throws NoSuchJobConfigurationException {
        final JobListenerConfiguration jobListenerConfiguration = TestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobListenerConfiguration);
        final JobConfiguration addedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobListenerConfiguration()).isNotNull();
        addedJobConfiguration.setJobName("updated");
        final JobConfiguration updatedJobConfiguration = getJobConfigurationRepository().update(addedJobConfiguration);
        assertThat(updatedJobConfiguration.getJobName()).isEqualTo("updated");
        final JobConfiguration fetchedJobConfiguration = getJobConfigurationRepository().getJobConfiguration
                (updatedJobConfiguration.getJobConfigurationId());
        assertThat(fetchedJobConfiguration).isEqualTo(updatedJobConfiguration);
    }

    @Test(expected = NoSuchJobConfigurationException.class)
    public void updateJobConfigurationIdNotExistingIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        addedJobConfiguration.setJobName("updated");
        addedJobConfiguration.setJobConfigurationId(-1000L);
        getJobConfigurationRepository().update(addedJobConfiguration);
    }

    @Test
    public void updateWithParametersIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        addedJobConfiguration.setJobName("updated");
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("test", "input");
        addedJobConfiguration.setJobParameters(parameters);
        final JobConfiguration updatedJobConfiguration = getJobConfigurationRepository().update(addedJobConfiguration);
        assertThat(updatedJobConfiguration.getJobName()).isEqualTo("updated");
        final JobConfiguration fetchedJobConfiguration = getJobConfigurationRepository().getJobConfiguration
                (addedJobConfiguration.getJobConfigurationId());
        assertThat(fetchedJobConfiguration).isEqualTo(updatedJobConfiguration);
    }

    @Test
    public void updateWithListenerWithParametersIT() throws NoSuchJobConfigurationException {
        final JobListenerConfiguration jobListenerConfiguration = TestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobListenerConfiguration);
        final JobConfiguration addedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobListenerConfiguration()).isNotNull();
        addedJobConfiguration.setJobName("updated");
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("test", "input");
        addedJobConfiguration.setJobParameters(parameters);
        final JobConfiguration updatedJobConfiguration = getJobConfigurationRepository().update(addedJobConfiguration);
        assertThat(updatedJobConfiguration.getJobName()).isEqualTo("updated");
        final JobConfiguration fetchedJobConfiguration = getJobConfigurationRepository().getJobConfiguration(1L);
        assertThat(fetchedJobConfiguration).isEqualTo(updatedJobConfiguration);
    }

    @Test
    public void getJobConfigurationsIT() throws NoSuchJobException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final Collection<JobConfiguration> jobConfigurations = getJobConfigurationRepository().getJobConfigurations("sampleJob");
        assertThat(jobConfigurations.size()).isEqualTo(2);
    }

    @Test(expected = NoSuchJobException.class)
    public void getJobConfigurationsJobNameUnknownIT() throws NoSuchJobException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        getJobConfigurationRepository().getJobConfigurations("sampleJobUnknown");
    }

    @Test(expected = NoSuchJobConfigurationException.class)
    public void deleteIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        getJobConfigurationRepository().delete(addedJobConfiguration);
        getJobConfigurationRepository().getJobConfiguration(addedJobConfiguration.getJobConfigurationId());
    }

    @Test(expected = NoSuchJobConfigurationException.class)
    public void deleteJobConfigurationIdNotExistingIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        addedJobConfiguration.setJobConfigurationId(-100L);
        getJobConfigurationRepository().delete(addedJobConfiguration);
    }

    @Test
    public void getJobConfigurationsByNameIT() throws NoSuchJobException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final Map<String, Object> jobParameters = new HashMap<>();
        jobParameters.put("LONG", 10L);
        jobParameters.put("DOUBLE", 20.2);
        jobParameters.put("STRING", "test");
        jobParameters.put("DATE", "2015/03/27 23:19:24:120");
        jobParameters.put("DATE", "2015/03/27");
        jobConfiguration.setJobParameters(jobParameters);
        final JobConfiguration addedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = getJobConfigurationRepository().add(jobConfiguration);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add("sampleJob");
        jobNames.add("sampleJobSecond");
        final Collection<JobConfiguration> jobConfigurations = getJobConfigurationRepository()
                .getAllJobConfigurationsByJobNames(jobNames);
        assertThat(jobConfigurations.size()).isEqualTo(2);
    }

    @Test
    public void getAllJobConfigurationsTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfigurationFirst = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration jobConfigurationSecond = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedFirstJobConfiguration = getJobConfigurationRepository().add(jobConfigurationFirst);
        final JobConfiguration addedSecondJobConfiguration = getJobConfigurationRepository().add(jobConfigurationSecond);

        final Collection<JobConfiguration> jobConfigurations = getJobConfigurationRepository().getAllJobConfigurations();
        assertThat(jobConfigurations).isNotNull();
        assertThat(jobConfigurations).hasSize(2);
        final Set<JobConfiguration> jobConfigurationSet = new HashSet<>(jobConfigurations);
        assertThat(jobConfigurationSet).contains(addedFirstJobConfiguration);
        assertThat(jobConfigurationSet).contains(addedSecondJobConfiguration);

    }

}
