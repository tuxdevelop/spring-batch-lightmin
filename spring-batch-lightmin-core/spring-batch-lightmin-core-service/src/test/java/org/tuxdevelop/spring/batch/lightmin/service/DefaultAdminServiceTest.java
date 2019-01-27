package org.tuxdevelop.spring.batch.lightmin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminCoreConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAdminServiceTest {

    private static final String APPLICATION_NAME = "test_application";

    @Mock
    private JobConfigurationRepository jobConfigurationRepository;

    @Mock
    private SchedulerService schedulerService;
    @Mock
    private ListenerService listenerService;
    @Mock
    private SpringBatchLightminCoreConfigurationProperties properties;

    private DefaultAdminService defaultAdminService;

    @Test
    public void saveJobConfigurationTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        when(this.jobConfigurationRepository.add(jobConfiguration, APPLICATION_NAME)).thenReturn(jobConfiguration);
        try {
            this.defaultAdminService.saveJobConfiguration(jobConfiguration);
            verify(this.schedulerService, times(1)).registerSchedulerForJob(any(JobConfiguration.class));
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void saveJobConfigurationWithListenerTest() {
        final JobListenerConfiguration jobListenerConfiguration = DomainTestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobListenerConfiguration);
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        when(this.jobConfigurationRepository.add(jobConfiguration, APPLICATION_NAME)).thenReturn(jobConfiguration);
        try {
            this.defaultAdminService.saveJobConfiguration(jobConfiguration);
            verify(this.listenerService, times(1)).registerListenerForJob(any(JobConfiguration.class));
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Test(expected = SpringBatchLightminApplicationException.class)
    public void saveJobConfigurationErrorTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        when(this.jobConfigurationRepository.add(jobConfiguration, APPLICATION_NAME)).thenReturn(jobConfiguration);
        try {
            when(this.jobConfigurationRepository.update(any(JobConfiguration.class), anyString()))
                    .thenThrow(NoSuchJobConfigurationException.class);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
        this.defaultAdminService.saveJobConfiguration(jobConfiguration);
    }

    @Test
    public void updateJobConfigurationTest() throws NoSuchJobConfigurationException {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        when(this.jobConfigurationRepository.getJobConfiguration(jobConfiguration.getJobConfigurationId(), APPLICATION_NAME)).thenReturn(jobConfiguration);
        try {
            this.defaultAdminService.updateJobConfiguration(jobConfiguration);
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateJobConfigurationWithListenerTest() throws NoSuchJobConfigurationException {
        final JobListenerConfiguration jobListenerConfiguration = DomainTestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobListenerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        when(this.jobConfigurationRepository.getJobConfiguration(jobConfiguration.getJobConfigurationId(), APPLICATION_NAME)).thenReturn(jobConfiguration);
        try {
            this.defaultAdminService.updateJobConfiguration(jobConfiguration);
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Test(expected = SpringBatchLightminApplicationException.class)
    public void updateJobConfigurationErrorTest() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        try {
            when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
            when(this.jobConfigurationRepository.getJobConfiguration(jobConfiguration.getJobConfigurationId(), APPLICATION_NAME)).thenReturn(jobConfiguration);
            when(this.jobConfigurationRepository.update(jobConfiguration, APPLICATION_NAME))
                    .thenThrow(NoSuchJobConfigurationException.class);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
        this.defaultAdminService.updateJobConfiguration(jobConfiguration);
    }

    @Test
    public void deleteJobConfigurationTest() {
        final Long jobConfigurationId = 1L;
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(jobConfigurationId);
        try {
            when(this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId, APPLICATION_NAME)).thenReturn(jobConfiguration);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }

        try {
            this.defaultAdminService.deleteJobConfiguration(jobConfigurationId);
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void deleteJobConfigurationWithListenerTest() {
        final Long jobConfigurationId = 1L;
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        final JobListenerConfiguration jobListenerConfiguration = DomainTestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobListenerConfiguration);
        jobConfiguration.setJobConfigurationId(jobConfigurationId);
        try {
            when(this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId, APPLICATION_NAME)).thenReturn(jobConfiguration);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }

        try {
            this.defaultAdminService.deleteJobConfiguration(jobConfigurationId);
        } catch (final SpringBatchLightminApplicationException e) {
            fail(e.getMessage());
        }

    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void deleteJobConfigurationErrorTest() {
        final Long jobConfigurationId = 1L;
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(jobConfigurationId);
        try {
            when(this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId, APPLICATION_NAME)).thenReturn(jobConfiguration);
            doThrow(NoSuchJobConfigurationException.class).when(this.jobConfigurationRepository).delete(jobConfiguration, APPLICATION_NAME);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
        this.defaultAdminService.deleteJobConfiguration(jobConfigurationId);
    }

    @Test
    public void getJobConfigurationsByJobNameTest() {
        final Long jobConfigurationId = 1L;
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(jobConfigurationId);
        final Collection<JobConfiguration> jobConfigurations = new LinkedList<>();
        jobConfigurations.add(jobConfiguration);
        try {
            when(this.jobConfigurationRepository.getJobConfigurations("sampleJob", APPLICATION_NAME)).thenReturn(jobConfigurations);
        } catch (final NoSuchJobException | NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
        final Collection<JobConfiguration> result = this.defaultAdminService.getJobConfigurationsByJobName("sampleJob");
        assertThat(result).hasSize(1);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = SpringBatchLightminApplicationException.class)
    public void getJobConfigurationsByJobNameErrorTest() {
        try {
            when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
            when(this.jobConfigurationRepository.getJobConfigurations("sampleJob", APPLICATION_NAME)).thenThrow(NoSuchJobException.class);
        } catch (final NoSuchJobException | NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
        this.defaultAdminService.getJobConfigurationsByJobName("sampleJob");
    }

    @Test
    public void getJobConfigurationMapTest() {
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        final JobConfiguration jobConfigurationSecond = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfigurationSecond.setJobConfigurationId(2L);
        final Collection<JobConfiguration> jobConfigurations = new LinkedList<>();
        jobConfigurations.add(jobConfiguration);
        jobConfigurations.add(jobConfigurationSecond);
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add("sampleJob");
        when(this.jobConfigurationRepository.getAllJobConfigurationsByJobNames(jobNames, APPLICATION_NAME)).thenReturn(jobConfigurations);
        final Map<String, Collection<JobConfiguration>> result = this.defaultAdminService.getJobConfigurationMap(jobNames);
        assertThat(result).containsKey("sampleJob");
        assertThat(result.get("sampleJob")).hasSize(2);
    }

    @Test
    public void getJobConfigurationsTest() {
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(1L);
        final JobConfiguration jobConfigurationSecond = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfigurationSecond.setJobConfigurationId(2L);
        final Collection<JobConfiguration> jobConfigurations = new LinkedList<>();
        jobConfigurations.add(jobConfiguration);
        jobConfigurations.add(jobConfigurationSecond);
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add("sampleJob");
        when(this.jobConfigurationRepository.getAllJobConfigurationsByJobNames(jobNames, APPLICATION_NAME)).thenReturn(jobConfigurations);
        final Collection<JobConfiguration> result = this.defaultAdminService.getJobConfigurations(jobNames);
        assertThat(result).hasSize(2);
    }

    @Test
    public void getJobConfigurationByIdTest() {
        final Long jobConfigurationId = 1L;
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobConfigurationId(jobConfigurationId);
        try {
            when(this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId, APPLICATION_NAME)).thenReturn(jobConfiguration);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }

        final JobConfiguration result = this.defaultAdminService.getJobConfigurationById(jobConfigurationId);
        assertThat(result).isEqualTo(jobConfiguration);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = SpringBatchLightminApplicationException.class)
    public void getJobConfigurationByIdErrorTest() {
        final Long jobConfigurationId = 1L;
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        try {
            when(this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId, APPLICATION_NAME))
                    .thenThrow(NoSuchJobConfigurationException.class);
        } catch (final NoSuchJobConfigurationException e) {
            fail(e.getMessage());
        }
        this.defaultAdminService.getJobConfigurationById(jobConfigurationId);
    }

    @Test
    public void stopJobConfigurationSchedulerTest() throws NoSuchJobConfigurationException {
        final String beanName = "schedulerBean";
        final Long jobConfigurationId = 10L;
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper
                .createJobSchedulerConfiguration("* * * * * *", null, null, JobSchedulerType.CRON);
        jobSchedulerConfiguration.setBeanName(beanName);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        when(this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId, APPLICATION_NAME)).thenReturn(jobConfiguration);
        this.defaultAdminService.stopJobConfiguration(jobConfigurationId);
        verify(this.schedulerService, times(1)).terminate(beanName);
    }

    @Test
    public void stopJobConfigurationListenerTest() throws NoSuchJobConfigurationException {
        final Long jobConfigurationId = 1L;
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        final String beanName = "schedulerBean";
        final JobListenerConfiguration jobListenerConfiguration = DomainTestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName(beanName);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobListenerConfiguration);
        jobListenerConfiguration.setBeanName(beanName);
        when(this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId, APPLICATION_NAME)).thenReturn(jobConfiguration);
        this.defaultAdminService.stopJobConfiguration(jobConfigurationId);
        verify(this.listenerService, times(1)).terminateListener(beanName);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = SpringBatchLightminApplicationException.class)
    public void stopJobConfigurationSchedulerExceptionTest() throws NoSuchJobConfigurationException {
        final Long jobConfigurationId = 10L;
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        when(this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId, APPLICATION_NAME))
                .thenThrow(NoSuchJobConfigurationException.class);
        this.defaultAdminService.stopJobConfiguration(jobConfigurationId);
    }

    @Test
    public void startJobConfigurationSchedulerTest() throws NoSuchJobConfigurationException {
        final String beanName = "schedulerBean";
        final Long jobConfigurationId = 10L;
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper
                .createJobSchedulerConfiguration("* * * * * *", null, null, JobSchedulerType.CRON);
        jobSchedulerConfiguration.setBeanName(beanName);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        when(this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId, APPLICATION_NAME)).thenReturn(jobConfiguration);
        this.defaultAdminService.startJobConfiguration(jobConfigurationId);
        verify(this.schedulerService, times(1)).schedule(beanName, Boolean.FALSE);
    }

    @Test
    public void startJobConfigurationListenerTest() throws NoSuchJobConfigurationException {
        final Long jobConfigurationId = 1L;
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        final String beanName = "schedulerBean";
        final JobListenerConfiguration jobListenerConfiguration = DomainTestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName(beanName);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobListenerConfiguration);
        when(this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId, APPLICATION_NAME)).thenReturn(jobConfiguration);
        this.defaultAdminService.startJobConfiguration(jobConfigurationId);
        verify(this.listenerService, times(1)).activateListener(beanName, Boolean.FALSE);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = SpringBatchLightminApplicationException.class)
    public void startJobConfigurationSchedulerExceptionTest() throws NoSuchJobConfigurationException {
        final Long jobConfigurationId = 10L;
        when(this.properties.getApplicationName()).thenReturn(APPLICATION_NAME);
        when(this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId, APPLICATION_NAME))
                .thenThrow(NoSuchJobConfigurationException.class);
        this.defaultAdminService.startJobConfiguration(jobConfigurationId);
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.defaultAdminService =
                new DefaultAdminService(
                        this.jobConfigurationRepository,
                        this.schedulerService,
                        this.listenerService,
                        this.properties);
    }

}
