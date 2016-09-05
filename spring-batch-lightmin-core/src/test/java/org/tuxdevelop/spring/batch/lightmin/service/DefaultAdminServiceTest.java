package org.tuxdevelop.spring.batch.lightmin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAdminServiceTest {

    @Mock
    private JobConfigurationRepository jobConfigurationRepository;

    @Mock
    private SchedulerService schedulerService;
    @Mock
    private ListenerService listenerService;

    @InjectMocks
    private DefaultAdminService defaultAdminService;

    @Test
    public void saveJobConfigurationTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        when(jobConfigurationRepository.add(any(JobConfiguration.class))).thenReturn(jobConfiguration);
        try {
            defaultAdminService.saveJobConfiguration(jobConfiguration);
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Test(expected = SpringBatchLightminApplicationException.class)
    public void saveJobConfigurationErrorTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        when(jobConfigurationRepository.add(any(JobConfiguration.class))).thenReturn(jobConfiguration);
        try {
            when(jobConfigurationRepository.update(any(JobConfiguration.class)))
                    .thenThrow(NoSuchJobConfigurationException.class);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
        defaultAdminService.saveJobConfiguration(jobConfiguration);
    }

    @Test
    public void updateJobConfigurationTest() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        when(jobConfigurationRepository.getJobConfiguration(anyLong())).thenReturn(jobConfiguration);
        try {
            defaultAdminService.updateJobConfiguration(jobConfiguration);
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Test(expected = SpringBatchLightminApplicationException.class)
    public void updateJobConfigurationErrorTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        try {
            when(jobConfigurationRepository.getJobConfiguration(anyLong())).thenReturn(jobConfiguration);
            when(jobConfigurationRepository.update(any(JobConfiguration.class)))
                    .thenThrow(NoSuchJobConfigurationException.class);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
        defaultAdminService.updateJobConfiguration(jobConfiguration);
    }

    @Test
    public void deleteJobConfigurationTest() {
        final Long jobConfigurationId = 1L;
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(jobConfigurationId);
        try {
            when(jobConfigurationRepository.getJobConfiguration(jobConfigurationId)).thenReturn(jobConfiguration);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }

        try {
            defaultAdminService.deleteJobConfiguration(jobConfigurationId);
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }

    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void deleteJobConfigurationErrorTest() {
        final Long jobConfigurationId = 1L;
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(jobConfigurationId);
        try {
            when(jobConfigurationRepository.getJobConfiguration(jobConfigurationId)).thenReturn(jobConfiguration);
            doThrow(NoSuchJobConfigurationException.class).when(jobConfigurationRepository).delete(jobConfiguration);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
        defaultAdminService.deleteJobConfiguration(jobConfigurationId);
    }

    @Test
    public void getJobConfigurationsByJobNameTest() {
        final Long jobConfigurationId = 1L;
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(jobConfigurationId);
        final Collection<JobConfiguration> jobConfigurations = new LinkedList<JobConfiguration>();
        jobConfigurations.add(jobConfiguration);
        try {
            when(jobConfigurationRepository.getJobConfigurations("sampleJob")).thenReturn(jobConfigurations);
        } catch (final NoSuchJobException e) {
            fail(e.getMessage());
        }
        final Collection<JobConfiguration> result = defaultAdminService.getJobConfigurationsByJobName("sampleJob");
        assertThat(result).hasSize(1);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = SpringBatchLightminApplicationException.class)
    public void getJobConfigurationsByJobNameErrorTest() {
        try {
            when(jobConfigurationRepository.getJobConfigurations("sampleJob")).thenThrow(NoSuchJobException.class);
        } catch (final NoSuchJobException e) {
            fail(e.getMessage());
        }
        defaultAdminService.getJobConfigurationsByJobName("sampleJob");
    }

    @Test
    public void getJobConfigurationMapTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        final JobConfiguration jobConfigurationSecond = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfigurationSecond.setJobConfigurationId(2L);
        final Collection<JobConfiguration> jobConfigurations = new LinkedList<JobConfiguration>();
        jobConfigurations.add(jobConfiguration);
        jobConfigurations.add(jobConfigurationSecond);
        final Collection<String> jobNames = new LinkedList<String>();
        jobNames.add("sampleJob");
        when(jobConfigurationRepository.getAllJobConfigurationsByJobNames(jobNames)).thenReturn(jobConfigurations);
        final Map<String, Collection<JobConfiguration>> result = defaultAdminService.getJobConfigurationMap(jobNames);
        assertThat(result).containsKey("sampleJob");
        assertThat(result.get("sampleJob")).hasSize(2);
    }

    @Test
    public void getJobConfigurationsTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        final JobConfiguration jobConfigurationSecond = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfigurationSecond.setJobConfigurationId(2L);
        final Collection<JobConfiguration> jobConfigurations = new LinkedList<JobConfiguration>();
        jobConfigurations.add(jobConfiguration);
        jobConfigurations.add(jobConfigurationSecond);
        final Collection<String> jobNames = new LinkedList<String>();
        jobNames.add("sampleJob");
        when(jobConfigurationRepository.getAllJobConfigurationsByJobNames(jobNames)).thenReturn(jobConfigurations);
        final Collection<JobConfiguration> result = defaultAdminService.getJobConfigurations(jobNames);
        assertThat(result).hasSize(2);
    }

    @Test
    public void getJobConfigurationByIdTest() {
        final Long jobConfigurationId = 1L;
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(jobConfigurationId);
        try {
            when(jobConfigurationRepository.getJobConfiguration(jobConfigurationId)).thenReturn(jobConfiguration);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }

        final JobConfiguration result = defaultAdminService.getJobConfigurationById(jobConfigurationId);
        assertThat(result).isEqualTo(jobConfiguration);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = SpringBatchLightminApplicationException.class)
    public void getJobConfigurationByIdErrorTest() {
        final Long jobConfigurationId = 1L;
        try {
            when(jobConfigurationRepository.getJobConfiguration(jobConfigurationId))
                    .thenThrow(NoSuchJobConfigurationException.class);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
        defaultAdminService.getJobConfigurationById(jobConfigurationId);
    }

    @Test
    public void stopJobConfigurationSchedulerTest() throws NoSuchJobConfigurationException {
        final String beanName = "schedulerBean";
        final Long jobConfigurationId = 10L;
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper
                .createJobSchedulerConfiguration("* * * * * *", null, null, JobSchedulerType.CRON);
        jobSchedulerConfiguration.setBeanName(beanName);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        when(jobConfigurationRepository.getJobConfiguration(jobConfigurationId)).thenReturn(jobConfiguration);
        defaultAdminService.stopJobConfiguration(jobConfigurationId);
        verify(schedulerService, times(1)).terminate(beanName);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = SpringBatchLightminApplicationException.class)
    public void stopJobConfigurationSchedulerExceptionTest() throws NoSuchJobConfigurationException {
        final Long jobConfigurationId = 10L;
        when(jobConfigurationRepository.getJobConfiguration(jobConfigurationId))
                .thenThrow(NoSuchJobConfigurationException.class);
        defaultAdminService.stopJobConfiguration(jobConfigurationId);
    }

    @Test
    public void startJobConfigurationSchedulerTest() throws NoSuchJobConfigurationException {
        final String beanName = "schedulerBean";
        final Long jobConfigurationId = 10L;
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper
                .createJobSchedulerConfiguration("* * * * * *", null, null, JobSchedulerType.CRON);
        jobSchedulerConfiguration.setBeanName(beanName);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        when(jobConfigurationRepository.getJobConfiguration(jobConfigurationId)).thenReturn(jobConfiguration);
        defaultAdminService.startJobConfiguration(jobConfigurationId);
        verify(schedulerService, times(1)).schedule(beanName, Boolean.FALSE);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = SpringBatchLightminApplicationException.class)
    public void startJobConfigurationSchedulerExceptionTest() throws NoSuchJobConfigurationException {
        final Long jobConfigurationId = 10L;
        when(jobConfigurationRepository.getJobConfiguration(jobConfigurationId))
                .thenThrow(NoSuchJobConfigurationException.class);
        defaultAdminService.startJobConfiguration(jobConfigurationId);
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        defaultAdminService = new DefaultAdminService(jobConfigurationRepository, schedulerService, listenerService);
    }

}
