package org.tuxdevelop.spring.batch.lightmin.repository.server.api.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.test.configuration.IntegrationTestConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
public class RepositoryControllerIT {

    private static final String APPLICATION_NAME = "sample_application";

    @Autowired
    private RepositoryController repositoryController;


    @Test
    public void testGetJobConfiguration() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null, 1000L, 100L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration savedJobConfiguration = repositoryController.add(jobConfiguration, APPLICATION_NAME);
        try {
            final JobConfiguration fetchedJobConfiguration = repositoryController.getJobConfiguration(savedJobConfiguration.getJobConfigurationId(), APPLICATION_NAME);
            assertThat(fetchedJobConfiguration).isEqualTo(savedJobConfiguration);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
    }

}
