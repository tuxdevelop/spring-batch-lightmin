package org.tuxdevelop.spring.batch.lightmin.admin.repository;

import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class MapJobConfigurationRepositoryTest {

    private MapJobConfigurationRepository mapJobConfigurationRepository;

    @Test
    public void addTest() {
        mapJobConfigurationRepository = new MapJobConfigurationRepository();
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = mapJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = mapJobConfigurationRepository.add(jobConfiguration);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isEqualTo(2L);
        assertThat(secondAddedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
    }

    @Test(expected = SpringBatchLightminConfigurationException.class)
    public void addJobNameNullTest() {
        mapJobConfigurationRepository = new MapJobConfigurationRepository();
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(null);
        mapJobConfigurationRepository.add(jobConfiguration);

    }


    @Test
    public void getJobConfigurationTest() throws NoSuchJobConfigurationException {
        mapJobConfigurationRepository = new MapJobConfigurationRepository();
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = mapJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration fetchedJobConfiguration = mapJobConfigurationRepository.getJobConfiguration(1L);
        assertThat(fetchedJobConfiguration).isEqualTo(addedJobConfiguration);
    }


    @Test
    public void updateTest() throws NoSuchJobConfigurationException {
        mapJobConfigurationRepository = new MapJobConfigurationRepository();
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = mapJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        addedJobConfiguration.setJobName("updated");
        final JobConfiguration updatedJobConfiguration = mapJobConfigurationRepository.update(addedJobConfiguration);
        assertThat(updatedJobConfiguration.getJobName()).isEqualTo("updated");
        final JobConfiguration fetchedJobConfiguration = mapJobConfigurationRepository.getJobConfiguration(1L);
        assertThat(fetchedJobConfiguration).isEqualTo(updatedJobConfiguration);
    }

    @Test
    public void getJobConfigurationsTest() throws NoSuchJobException {
        mapJobConfigurationRepository = new MapJobConfigurationRepository();
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = mapJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = mapJobConfigurationRepository.add(jobConfiguration);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isEqualTo(2L);
        assertThat(secondAddedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final Collection<JobConfiguration> jobConfigurations = mapJobConfigurationRepository.getJobConfigurations
                ("sampleJob");
        assertThat(jobConfigurations.size()).isEqualTo(2);
    }

    @Test(expected = NoSuchJobException.class)
    public void getJobConfigurationsUnknownJobNameTest() throws NoSuchJobException {
        mapJobConfigurationRepository = new MapJobConfigurationRepository();
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = mapJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = mapJobConfigurationRepository.add(jobConfiguration);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isEqualTo(2L);
        assertThat(secondAddedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        mapJobConfigurationRepository.getJobConfigurations("sampleJobUnknown");
    }

    @Test(expected = NoSuchJobConfigurationException.class)
    public void deleteTest() throws NoSuchJobConfigurationException, NoSuchJobException {
        mapJobConfigurationRepository = new MapJobConfigurationRepository();
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = mapJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        mapJobConfigurationRepository.delete(addedJobConfiguration);
        mapJobConfigurationRepository.getJobConfiguration(1L);
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void deleteJobNameNullTest() throws NoSuchJobConfigurationException, NoSuchJobException {
        mapJobConfigurationRepository = new MapJobConfigurationRepository();
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(null);
        mapJobConfigurationRepository.delete(jobConfiguration);
    }

    @Test(expected = NoSuchJobConfigurationException.class)
    public void deleteJobNameNoExistingTest() throws NoSuchJobConfigurationException, NoSuchJobException {
        mapJobConfigurationRepository = new MapJobConfigurationRepository();
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName("notExisting");
        mapJobConfigurationRepository.delete(jobConfiguration);
    }

    @Test(expected = NoSuchJobConfigurationException.class)
    public void deleteJobConfigurationNullTest() throws NoSuchJobConfigurationException, NoSuchJobException {
        mapJobConfigurationRepository = new MapJobConfigurationRepository();
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        mapJobConfigurationRepository.add(jobConfiguration);
        final JobConfiguration jobConfigurationToDelete = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfigurationToDelete.setJobConfigurationId(-10L);
        mapJobConfigurationRepository.delete(jobConfigurationToDelete);
    }

    @Test
    public void getAllJobConfigurationsTest() {
        mapJobConfigurationRepository = new MapJobConfigurationRepository();
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfigurationFirst = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration jobConfigurationSecond = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedFirstJobConfiguration = mapJobConfigurationRepository.add(jobConfigurationFirst);
        final JobConfiguration addedSecondJobConfiguration = mapJobConfigurationRepository.add(jobConfigurationSecond);

        final Collection<JobConfiguration> jobConfigurations = mapJobConfigurationRepository.getAllJobConfigurations();
        assertThat(jobConfigurations).isNotNull();
        assertThat(jobConfigurations).hasSize(2);
        final Set<JobConfiguration> jobConfigurationSet = new HashSet<JobConfiguration>(jobConfigurations);
        assertThat(jobConfigurationSet).contains(addedFirstJobConfiguration);
        assertThat(jobConfigurationSet).contains(addedSecondJobConfiguration);

    }

    @Test
    public void getAllJobConfigurationsByJobNamesTest() {
        mapJobConfigurationRepository = new MapJobConfigurationRepository();
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfigurationFirst = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration jobConfigurationSecond = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedFirstJobConfiguration = mapJobConfigurationRepository.add(jobConfigurationFirst);
        final JobConfiguration addedSecondJobConfiguration = mapJobConfigurationRepository.add(jobConfigurationSecond);
        final Collection<String> jobNames = new LinkedList<String>();
        jobNames.add("sampleJob");
        final Collection<JobConfiguration> jobConfigurations = mapJobConfigurationRepository
                .getAllJobConfigurationsByJobNames(jobNames);
        assertThat(jobConfigurations).isNotNull();
        assertThat(jobConfigurations).hasSize(2);
        final Set<JobConfiguration> jobConfigurationSet = new HashSet<JobConfiguration>(jobConfigurations);
        assertThat(jobConfigurationSet).contains(addedFirstJobConfiguration);
        assertThat(jobConfigurationSet).contains(addedSecondJobConfiguration);

    }

    @Test
    public void getAllJobConfigurationsByJobNamesUnknownTest() {
        mapJobConfigurationRepository = new MapJobConfigurationRepository();
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfigurationFirst = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration jobConfigurationSecond = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        mapJobConfigurationRepository.add(jobConfigurationFirst);
        mapJobConfigurationRepository.add(jobConfigurationSecond);
        final Collection<String> jobNames = new LinkedList<String>();
        jobNames.add("sampleJobUnknown");
        final Collection<JobConfiguration> jobConfigurations = mapJobConfigurationRepository
                .getAllJobConfigurationsByJobNames(jobNames);
        assertThat(jobConfigurations).isNotNull();
        assertThat(jobConfigurations).isEmpty();

    }


}
