package org.tuxdevelop.spring.batch.lightmin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobRepository;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.util.BeanRegistrar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SchedulerServiceBeanTest {

    @InjectMocks
    private SchedulerServiceBean schedulerService;

    @Mock
    private BeanRegistrar beanRegistrar;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobRegistry jobRegistry;

    private Job sampleJob;

    @Test
    public void registerSchedulerForJobPeriodTest() throws NoSuchJobException {
        when(jobRegistry.getJob("sampleJob")).thenReturn(sampleJob);
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        final String beanName = schedulerService.registerSchedulerForJob(jobConfiguration);
        assertThat(beanName).isEqualTo("sampleJobPERIOD1");
    }

    @Test
    public void registerSchedulerForJobCronTest() throws NoSuchJobException {
        when(jobRegistry.getJob("sampleJob")).thenReturn(sampleJob);
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration("* * *" +
                        " * * *",
                null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        final String beanName = schedulerService.registerSchedulerForJob(jobConfiguration);
        assertThat(beanName).isEqualTo("sampleJobCRON1");
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        schedulerService = new SchedulerServiceBean(beanRegistrar, jobRepository, jobRegistry);
        sampleJob = TestHelper.createJob("sampleJob");
    }
}
