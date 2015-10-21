package org.tuxdevelop.spring.batch.lightmin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.admin.scheduler.Scheduler;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.util.BeanRegistrar;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSchedulerServiceTest {

    @InjectMocks
    private DefaultSchedulerService schedulerService;

    @Mock
    private BeanRegistrar beanRegistrar;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobRegistry jobRegistry;
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private Scheduler scheduler;

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

    @Test
    public void unregisterSchedulerForJobTest() {
        doNothing().when(beanRegistrar).unregisterBean(anyString());
        schedulerService.unregisterSchedulerForJob("sampleJob");
        verify(beanRegistrar, times(1)).unregisterBean("sampleJob");
    }

    @Test
    public void refreshSchedulerForJobTest() {
        final String beanName = "schedulerBean";
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setBeanName(beanName);
        jobSchedulerConfiguration.setTaskExecutorType(TaskExecutorType.ASYNCHRONOUS);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final Map<String, Object> jobParameters = new HashMap<String, Object>();
        jobParameters.put("LONG", 10L);
        jobParameters.put("DOUBLE", 20.2);
        jobParameters.put("STRING", "test");
        jobParameters.put("DATE", "2015/03/27 23:19:24:120");
        jobParameters.put("DATE", "2015/03/27");
        jobConfiguration.setJobParameters(jobParameters);
        final SchedulerConstructorWrapper schedulerConstructorWrapper = new SchedulerConstructorWrapper();
        schedulerConstructorWrapper.setJob(TestHelper.createJob("sampleJob"));
        schedulerConstructorWrapper.setJobConfiguration(jobConfiguration);
        schedulerConstructorWrapper.setJobIncrementer(JobIncrementer.NONE);
        schedulerConstructorWrapper.setJobLauncher(new SimpleJobLauncher());
        schedulerConstructorWrapper.setJobParameters(new JobParametersBuilder().toJobParameters());
        //scheduler = new PeriodScheduler(schedulerConstructorWrapper);
        when(applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(applicationContext.getBean(beanName)).thenReturn(scheduler);
        when(scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.INITIALIZED);
        schedulerService.refreshSchedulerForJob(jobConfiguration);
        verify(scheduler, times(1)).terminate();
    }

    @Test
    public void refreshSchedulerForJobCronTest() {
        final String beanName = "schedulerBean";
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration("some " +
                        "Cron",
                null, null, JobSchedulerType.CRON);
        jobSchedulerConfiguration.setBeanName(beanName);
        jobSchedulerConfiguration.setTaskExecutorType(TaskExecutorType.ASYNCHRONOUS);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final Map<String, Object> jobParameters = new HashMap<String, Object>();
        jobParameters.put("LONG", 10L);
        jobParameters.put("DOUBLE", 20.2);
        jobParameters.put("STRING", "test");
        jobParameters.put("DATE", "2015/03/27 23:19:24:120");
        jobParameters.put("DATE", "2015/03/27");
        jobConfiguration.setJobParameters(jobParameters);
        final SchedulerConstructorWrapper schedulerConstructorWrapper = new SchedulerConstructorWrapper();
        schedulerConstructorWrapper.setJob(TestHelper.createJob("sampleJob"));
        schedulerConstructorWrapper.setJobConfiguration(jobConfiguration);
        schedulerConstructorWrapper.setJobIncrementer(JobIncrementer.NONE);
        schedulerConstructorWrapper.setJobLauncher(new SimpleJobLauncher());
        schedulerConstructorWrapper.setJobParameters(new JobParametersBuilder().toJobParameters());
        //scheduler = new PeriodScheduler(schedulerConstructorWrapper);
        when(applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(applicationContext.getBean(beanName)).thenReturn(scheduler);
        when(scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.INITIALIZED);
        schedulerService.refreshSchedulerForJob(jobConfiguration);
        verify(scheduler, times(1)).terminate();
    }

    @Test
    public void scheduleTest() {
        final String beanName = "schedulerBean";
        when(applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(applicationContext.getBean(beanName)).thenReturn(scheduler);
        when(scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.INITIALIZED);
        schedulerService.schedule(beanName, Boolean.FALSE);
        verify(scheduler, times(1)).schedule();
    }

    @Test
    public void scheduleRUNNINGTest() {
        final String beanName = "schedulerBean";
        when(applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(applicationContext.getBean(beanName)).thenReturn(scheduler);
        when(scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.RUNNING);
        schedulerService.schedule(beanName, Boolean.FALSE);
        verify(scheduler, times(0)).schedule();
    }

    @Test
    public void scheduleRUNNINGForceTest() {
        final String beanName = "schedulerBean";
        when(applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(applicationContext.getBean(beanName)).thenReturn(scheduler);
        when(scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.RUNNING);
        schedulerService.schedule(beanName, Boolean.TRUE);
        verify(scheduler, times(1)).schedule();
    }

    @Test(expected = SpringBatchLightminConfigurationException.class)
    public void scheduleUnknownTest() {
        final String beanName = "schedulerBean";
        when(applicationContext.containsBean(beanName)).thenReturn(Boolean.FALSE);
        schedulerService.schedule(beanName, Boolean.FALSE);
    }

    @Test
    public void terminateTest() {
        final String beanName = "schedulerBean";
        when(applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(applicationContext.getBean(beanName)).thenReturn(scheduler);
        when(scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.RUNNING);
        schedulerService.terminate(beanName);
        verify(scheduler, times(1)).terminate();
    }

    @Test
    public void terminateSTOPPEDTest() {
        final String beanName = "schedulerBean";
        when(applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(applicationContext.getBean(beanName)).thenReturn(scheduler);
        when(scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.STOPPED);
        schedulerService.terminate(beanName);
        verify(scheduler, times(0)).terminate();
    }

    @Test(expected = SpringBatchLightminConfigurationException.class)
    public void terminateUnknownTest() {
        final String beanName = "schedulerBean";
        when(applicationContext.containsBean(beanName)).thenReturn(Boolean.FALSE);
        schedulerService.terminate(beanName);
    }

    @Test
    public void getSchedulerStatusTest() {
        final String beanName = "schedulerBean";
        when(applicationContext.containsBean(beanName)).thenReturn(Boolean.TRUE);
        when(applicationContext.getBean(beanName)).thenReturn(scheduler);
        when(scheduler.getSchedulerStatus()).thenReturn(SchedulerStatus.STOPPED);
        final SchedulerStatus schedulerStatus = schedulerService.getSchedulerStatus(beanName);
        assertThat(schedulerStatus).isEqualTo(SchedulerStatus.STOPPED);
    }

    @Test(expected = SpringBatchLightminConfigurationException.class)
    public void getSchedulerStatusUnknownTest() {
        final String beanName = "schedulerBean";
        when(applicationContext.containsBean(beanName)).thenReturn(Boolean.FALSE);
        schedulerService.getSchedulerStatus(beanName);
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        schedulerService = new DefaultSchedulerService(beanRegistrar, jobRepository, jobRegistry);
        sampleJob = TestHelper.createJob("sampleJob");
        ReflectionTestUtils.setField(schedulerService, "applicationContext", applicationContext);
    }
}
