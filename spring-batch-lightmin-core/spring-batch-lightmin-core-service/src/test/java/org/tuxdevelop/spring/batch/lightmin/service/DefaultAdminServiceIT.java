package org.tuxdevelop.spring.batch.lightmin.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.listener.FolderListener;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;
import org.tuxdevelop.test.configuration.ITConfiguration;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ITConfiguration.class)
public class DefaultAdminServiceIT {

    private static final String JOB_NAME = "simpleJob";

    @Autowired
    private AdminService adminService;

    @Autowired
    private ApplicationContext applicationContext;


    @Test
    public void testDeleteJobConfiguration() {
        final JobSchedulerConfiguration jobSchedulerConfiguration =
                DomainTestHelper.createJobSchedulerConfiguration(null, 10L, 10L,
                        JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(JOB_NAME);
        this.adminService.saveJobConfiguration(jobConfiguration);
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add(JOB_NAME);
        final Collection<JobConfiguration> fetchedJobConfigurations = this.adminService.getJobConfigurations(jobNames);
        assertThat(fetchedJobConfigurations).hasSize(1);

        final Long jobConfigurationId = fetchedJobConfigurations.stream().findFirst().get().getJobConfigurationId();

        assertThat(jobConfigurationId).isNotNull();
        final JobConfiguration fetchedJobConfiguration = this.adminService.getJobConfigurationById(jobConfigurationId);
        assertThat(fetchedJobConfiguration).isNotNull();
        this.adminService.deleteJobConfiguration(jobConfigurationId);
        BDDAssertions.assertThatExceptionOfType(SpringBatchLightminApplicationException.class)
                .as("Job shouldn't be managed by Entity Manager. We are awaiting an Exception.")
                .isThrownBy(() -> this.adminService.getJobConfigurationById(jobConfigurationId));
    }

    @Test
    public void testUpdateJobConfiguration() {
        final JobSchedulerConfiguration jobSchedulerConfiguration =
                DomainTestHelper.createJobSchedulerConfiguration(null, 10L, 10L,
                        JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(JOB_NAME);
        this.adminService.saveJobConfiguration(jobConfiguration);
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add(JOB_NAME);
        final Collection<JobConfiguration> fetchedJobConfigurations = this.adminService.getJobConfigurations(jobNames);
        assertThat(fetchedJobConfigurations).hasSize(1);
        Long jobConfigurationId = null;
        for (final JobConfiguration fetchedJobConfiguration : fetchedJobConfigurations) {
            jobConfigurationId = fetchedJobConfiguration.getJobConfigurationId();
        }
        assertThat(jobConfiguration).isNotNull();
        final JobConfiguration fetchedJobConfiguration = this.adminService.getJobConfigurationById(jobConfigurationId);
        final Map<String, Object> jobParameters = new HashMap<>();
        jobParameters.put("Double", 20.2);
        fetchedJobConfiguration.setJobParameters(jobParameters);
        this.adminService.updateJobConfiguration(fetchedJobConfiguration);
        final JobConfiguration updatedJobConfiguration = this.adminService.getJobConfigurationById(jobConfigurationId);
        assertThat(updatedJobConfiguration).isEqualTo(fetchedJobConfiguration);
    }

    @Test
    public void testHappyPathSaveConfigurationWithScheduler() {
        // Set Up happy Path
        final JobSchedulerConfiguration jobSchedulerConfiguration =
                DomainTestHelper.createJobSchedulerConfiguration(null, 10L, 10L,
                        JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(JOB_NAME);
        this.adminService.saveJobConfiguration(jobConfiguration);

        Collection<JobConfiguration> jobConfigurationsByJobName = this.adminService.getJobConfigurationsByJobName(JOB_NAME);
        BDDAssertions.assertThat(jobConfigurationsByJobName).hasSize(1);
        Optional<JobConfiguration> optional = jobConfigurationsByJobName.stream().findFirst();
        BDDAssertions.assertThat(optional).isPresent();
        JobConfiguration jobConfigurationActual = optional.get();
        BDDAssertions.assertThat(jobConfigurationActual.getJobConfigurationId()).isNotNull().isPositive();
        JobSchedulerConfiguration jobSchedulerActual = jobConfigurationActual.getJobSchedulerConfiguration();
        BDDAssertions.assertThat(jobSchedulerActual).isNotNull();

        BDDAssertions.assertThat(jobSchedulerActual.getBeanName())
                .as("Tests if Bean Name got generated Successfully. Ignore the UUID at the end.")
                .isNotNull().startsWith(jobConfiguration.getJobName() + "-" + jobSchedulerConfiguration.getJobSchedulerType() + "-");
    }

    @Test
    public void testHappyPathSaveConfigurationWithListener() {
        // Set Up happy Path
        final JobListenerConfiguration jobListenerConfiguration = DomainTestHelper.createJobListenerConfiguration("src/test/resources", "*", JobListenerType.LOCAL_FOLDER_LISTENER);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobListenerConfiguration);
        jobConfiguration.setJobName(JOB_NAME);
        jobConfiguration.getJobListenerConfiguration().setListenerStatus(ListenerStatus.ACTIVE);

        this.adminService.saveJobConfiguration(jobConfiguration);

        Collection<JobConfiguration> jobConfigurationsByJobName = this.adminService.getJobConfigurationsByJobName(JOB_NAME);
        BDDAssertions.assertThat(jobConfigurationsByJobName).hasSize(1);
        Optional<JobConfiguration> optional = jobConfigurationsByJobName.stream().findFirst();
        BDDAssertions.assertThat(optional).isPresent();
        JobConfiguration jobConfigurationActual = optional.get();
        BDDAssertions.assertThat(jobConfigurationActual.getJobConfigurationId()).isNotNull().isPositive();
        JobListenerConfiguration jobListenerActual = jobConfigurationActual.getJobListenerConfiguration();
        BDDAssertions.assertThat(jobListenerActual).isNotNull();

        BDDAssertions.assertThat(jobListenerActual.getBeanName())
                .as("Tests if Bean Name got generated Successfully. Ignore the UUID at the end.")
                .isNotNull().startsWith(jobConfiguration.getJobName() + "-" + jobListenerActual.getJobListenerType() + "-");
    }

    @Before
    public void init() {
        try {
            final Collection<JobConfiguration> allJC = this.adminService.getJobConfigurationsByJobName(JOB_NAME);
            for (final JobConfiguration jobConfiguration : allJC) {
                this.adminService.deleteJobConfiguration(jobConfiguration.getJobConfigurationId());
            }
        } catch (final SpringBatchLightminApplicationException e) {
            log.warn("Repository clean up failed");
        }
    }
}
