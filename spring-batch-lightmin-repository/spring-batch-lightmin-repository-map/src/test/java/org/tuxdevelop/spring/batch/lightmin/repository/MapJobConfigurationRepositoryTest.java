package org.tuxdevelop.spring.batch.lightmin.repository;

import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.repository.test.ITJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.repository.test.ITMapJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.repository.test.JobConfigurationRepositoryIT;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

public class MapJobConfigurationRepositoryTest extends JobConfigurationRepositoryIT {

    private static final String APPLICATION_NAME = "test_application_name";

    private final MapJobConfigurationRepository jobConfigurationRepository = new MapJobConfigurationRepository();
    private final ITMapJobConfigurationRepository itMapJobConfigurationRepository = new ITMapJobConfigurationRepository();

    @Override
    public JobConfigurationRepository getJobConfigurationRepository() {
        return this.jobConfigurationRepository;
    }

    @Override
    public ITJobConfigurationRepository getITItJdbcJobConfigurationRepository() {
        return this.itMapJobConfigurationRepository;
    }

    @Test(expected = NoSuchJobException.class)
    public void deleteAndGetJobConfigurationsTest() throws NoSuchJobConfigurationException, NoSuchJobException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final JobConfiguration addedJobConfiguration = this.getJobConfigurationRepository().add(jobConfiguration, APPLICATION_NAME);
        assertThat(addedJobConfiguration).isNotNull();
        assertThat(addedJobConfiguration.getJobConfigurationId()).isNotNull();
        assertThat(addedJobConfiguration.getJobSchedulerConfiguration()).isNotNull();
        this.getJobConfigurationRepository().delete(addedJobConfiguration, APPLICATION_NAME);
        this.getJobConfigurationRepository().getJobConfigurations(addedJobConfiguration.getJobName(), APPLICATION_NAME);


    }
}
