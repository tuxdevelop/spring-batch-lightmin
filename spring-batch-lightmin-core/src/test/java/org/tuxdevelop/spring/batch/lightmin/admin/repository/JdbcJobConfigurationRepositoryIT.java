package org.tuxdevelop.spring.batch.lightmin.admin.repository;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.tuxdevelop.spring.batch.lightmin.ITPersistenceConfiguration;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITPersistenceConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcJobConfigurationRepositoryIT {

    @Autowired
    private JobConfigurationRepository jdbcJobConfigurationRepository;

    @Test
    @Rollback
    public void addIT() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = jdbcJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = jdbcJobConfigurationRepository.add(jobConfiguration);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isEqualTo(2L);
        assertThat(secondAddedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
    }

    @Test
    @Rollback
    public void getJobConfigurationIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = jdbcJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration fetchedJobConfiguration = jdbcJobConfigurationRepository.getJobConfiguration(1L);
        assertThat(fetchedJobConfiguration).isEqualTo(addedJobConfiguration);
    }

    @Test
    @Rollback
    public void updateIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = jdbcJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        addedJobConfiguration.setJobName("updated");
        final JobConfiguration updatedJobConfiguration = jdbcJobConfigurationRepository.update(addedJobConfiguration);
        assertThat(updatedJobConfiguration.getJobName()).isEqualTo("updated");
        final JobConfiguration fetchedJobConfiguration = jdbcJobConfigurationRepository.getJobConfiguration(1L);
        assertThat(fetchedJobConfiguration).isEqualTo(updatedJobConfiguration);
    }

    @Test(expected = NoSuchJobConfigurationException.class)
    @Rollback
    public void updateJobConfigurationIdNotExistingIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = jdbcJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        addedJobConfiguration.setJobName("updated");
        addedJobConfiguration.setJobConfigurationId(-1000L);
        jdbcJobConfigurationRepository.update(addedJobConfiguration);
    }

    @Test
    @Rollback
    public void updateWithParametersIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = jdbcJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        addedJobConfiguration.setJobName("updated");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("test", "input");
        addedJobConfiguration.setJobParameters(parameters);
        final JobConfiguration updatedJobConfiguration = jdbcJobConfigurationRepository.update(addedJobConfiguration);
        assertThat(updatedJobConfiguration.getJobName()).isEqualTo("updated");
        final JobConfiguration fetchedJobConfiguration = jdbcJobConfigurationRepository.getJobConfiguration(1L);
        assertThat(fetchedJobConfiguration).isEqualTo(updatedJobConfiguration);
    }

    @Test
    @Rollback
    public void getJobConfigurationsIT() throws NoSuchJobException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = jdbcJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = jdbcJobConfigurationRepository.add(jobConfiguration);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isEqualTo(2L);
        assertThat(secondAddedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final Collection<JobConfiguration> jobConfigurations = jdbcJobConfigurationRepository.getJobConfigurations
                ("sampleJob");
        assertThat(jobConfigurations.size()).isEqualTo(2);
    }

    @Test(expected = NoSuchJobException.class)
    @Rollback
    public void getJobConfigurationsJobNameUnknownIT() throws NoSuchJobException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = jdbcJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = jdbcJobConfigurationRepository.add(jobConfiguration);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isEqualTo(2L);
        assertThat(secondAddedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        jdbcJobConfigurationRepository.getJobConfigurations("sampleJobUnknown");
    }

    @Test(expected = NoSuchJobConfigurationException.class)
    @Rollback
    public void deleteIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = jdbcJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        jdbcJobConfigurationRepository.delete(addedJobConfiguration);
        jdbcJobConfigurationRepository.getJobConfiguration(1L);
    }

    @Test(expected = NoSuchJobConfigurationException.class)
    @Rollback
    public void deleteJobConfigurationIdNotExistingIT() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = jdbcJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        addedJobConfiguration.setJobConfigurationId(-100L);
        jdbcJobConfigurationRepository.delete(addedJobConfiguration);
    }

    @Test
    @Rollback
    public void getJobConfigurationsByNameIT() throws NoSuchJobException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final Map<String, Object> jobParameters = new HashMap<String, Object>();
        jobParameters.put("LONG", 10L);
        jobParameters.put("DOUBLE", 20.2);
        jobParameters.put("STRING", "test");
        jobParameters.put("DATE", "2015/03/27 23:19:24:120");
        jobParameters.put("DATE", "2015/03/27");
        jobConfiguration.setJobParameters(jobParameters);
        final JobConfiguration addedJobConfiguration = jdbcJobConfigurationRepository.add(jobConfiguration);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isEqualTo(1L);
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final JobConfiguration secondAddedJobConfiguration = jdbcJobConfigurationRepository.add(jobConfiguration);
        assertThat(secondAddedJobConfiguration).isNotNull();
        assertThat(secondAddedJobConfiguration.getJobConfigurationId()).isEqualTo(2L);
        assertThat(secondAddedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        final Collection<String> jobNames = new LinkedList<String>();
        jobNames.add("sampleJob");
        jobNames.add("sampleJobSecond");
        final Collection<JobConfiguration> jobConfigurations = jdbcJobConfigurationRepository
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
        final JobConfiguration addedFirstJobConfiguration = jdbcJobConfigurationRepository.add(jobConfigurationFirst);
        final JobConfiguration addedSecondJobConfiguration = jdbcJobConfigurationRepository.add(jobConfigurationSecond);

        final Collection<JobConfiguration> jobConfigurations = jdbcJobConfigurationRepository.getAllJobConfigurations();
        assertThat(jobConfigurations).isNotNull();
        assertThat(jobConfigurations).hasSize(2);
        final Set<JobConfiguration> jobConfigurationSet = new HashSet<JobConfiguration>(jobConfigurations);
        assertThat(jobConfigurationSet).contains(addedFirstJobConfiguration);
        assertThat(jobConfigurationSet).contains(addedSecondJobConfiguration);

    }

}
