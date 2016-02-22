package org.tuxdevelop.spring.batch.lightmin.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.batch.lightmin.ITMapConfiguration;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.Collection;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ActiveProfiles("mapOnly")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITMapConfiguration.class)
public class DefaultAdminServiceIT {

    @Autowired
    private AdminService adminService;

    @Test
    public void testDeleteJobConfiguration() {
        final String jobName = "simpleJob";
        final JobSchedulerConfiguration jobSchedulerConfiguration =
                TestHelper.createJobSchedulerConfiguration(null, 10L, 10L,
                        JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(jobName);
        adminService.saveJobConfiguration(jobConfiguration);
        final Collection<String> jobNames = new LinkedList<String>();
        jobNames.add(jobName);
        final Collection<JobConfiguration> fetchedJobConfigurations = adminService.getJobConfigurations(jobNames);
        assertThat(fetchedJobConfigurations).hasSize(1);
        Long jobConfigurationId = null;
        for (final JobConfiguration fetchedJobConfiguration : fetchedJobConfigurations) {
            jobConfigurationId = fetchedJobConfiguration.getJobConfigurationId();
        }
        assertThat(jobConfiguration).isNotNull();
        final JobConfiguration fetchedJobConfiguration = adminService.getJobConfigurationById(jobConfigurationId);
        assertThat(fetchedJobConfiguration).isNotNull();
        adminService.deleteJobConfiguration(jobConfigurationId);
        try {
            adminService.getJobConfigurationById(jobConfigurationId);
            fail("Should not be here");
        } catch (final SpringBatchLightminApplicationException e) {
            //OK
        }


    }


}
