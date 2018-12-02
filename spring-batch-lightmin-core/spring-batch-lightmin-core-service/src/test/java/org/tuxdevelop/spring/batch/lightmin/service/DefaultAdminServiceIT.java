package org.tuxdevelop.spring.batch.lightmin.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;
import org.tuxdevelop.test.configuration.ITConfiguration;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ITConfiguration.class)
public class DefaultAdminServiceIT {

    private static final String JOB_NAME = "simpleJob";

    @Autowired
    private AdminService adminService;


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
        Long jobConfigurationId = null;
        for (final JobConfiguration fetchedJobConfiguration : fetchedJobConfigurations) {
            jobConfigurationId = fetchedJobConfiguration.getJobConfigurationId();
        }
        assertThat(jobConfigurationId).isNotNull();
        final JobConfiguration fetchedJobConfiguration = this.adminService.getJobConfigurationById(jobConfigurationId);
        assertThat(fetchedJobConfiguration).isNotNull();
        this.adminService.deleteJobConfiguration(jobConfigurationId);
        try {
            this.adminService.getJobConfigurationById(jobConfigurationId);
            fail("Should not be here");
        } catch (final SpringBatchLightminApplicationException e) {
            //OK
        }
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
