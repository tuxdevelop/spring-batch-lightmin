package org.tuxdevelop.spring.batch.lightmin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.tuxdevelop.spring.batch.lightmin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.listener.FolderListener;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;
import org.tuxdevelop.spring.batch.lightmin.util.BeanRegistrar;

import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultListenerServiceTest {

    @Mock
    private BeanRegistrar beanRegistrar;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobRegistry jobRegistry;
    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private DefaultListenerService listenerService;

    private Job job;
    private JobLauncher jobLauncher;

    @Test
    public void testRegisterListenerForJob() throws NoSuchJobException {
        final JobListenerConfiguration jobListenerConfiguration = DomainTestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName("testBean");
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobListenerConfiguration);
        this.listenerService.registerListenerForJob(jobConfiguration);
        verify(this.jobRegistry, times(1)).getJob(anyString());
        verify(this.beanRegistrar, times(1)).registerBean(
                eq(FolderListener.class),
                eq("testBean"),
                anySet(),
                eq(null),
                eq(null),
                eq(null),
                eq(null));
    }

    @Test
    public void testUnregisterListenerForJob() {
        final String beanName = "testBean";
        this.listenerService.unregisterListenerForJob(beanName);
        verify(this.beanRegistrar, times(1)).unregisterBean(beanName);
    }

    @Test
    public void testActivateListener() {
        final JobListenerConfiguration jobListenerConfiguration = DomainTestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName("testBean");
        jobListenerConfiguration.setListenerStatus(ListenerStatus.ACTIVE);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobListenerConfiguration);
        final ListenerConstructorWrapper listenerConstructorWrapper = new ListenerConstructorWrapper();
        listenerConstructorWrapper.setJobIncrementer(JobIncrementer.DATE);
        listenerConstructorWrapper.setJob(this.job);
        listenerConstructorWrapper.setJobConfiguration(jobConfiguration);
        listenerConstructorWrapper.setJobLauncher(this.jobLauncher);
        listenerConstructorWrapper.setJobParameters(new JobParametersBuilder().toJobParameters());
        final FolderListener folderListener = new FolderListener(listenerConstructorWrapper);
        when(this.applicationContext.getBean(anyString(), any(Class.class))).thenReturn(folderListener);
        when(this.applicationContext.containsBean(anyString())).thenReturn(Boolean.TRUE);
        try {
            this.listenerService.activateListener("testBean", Boolean.FALSE);
        } catch (final Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testTerminateListener() {
        final JobListenerConfiguration jobListenerConfiguration = DomainTestHelper.createJobListenerConfiguration
                ("src/test/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setBeanName("testBean");
        jobListenerConfiguration.setListenerStatus(ListenerStatus.ACTIVE);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobListenerConfiguration);
        final ListenerConstructorWrapper listenerConstructorWrapper = new ListenerConstructorWrapper();
        listenerConstructorWrapper.setJobIncrementer(JobIncrementer.DATE);
        listenerConstructorWrapper.setJob(this.job);
        listenerConstructorWrapper.setJobConfiguration(jobConfiguration);
        listenerConstructorWrapper.setJobLauncher(this.jobLauncher);
        listenerConstructorWrapper.setJobParameters(new JobParametersBuilder().toJobParameters());
        final FolderListener folderListener = new FolderListener(listenerConstructorWrapper);
        when(this.applicationContext.getBean(anyString(), Matchers.any(Class.class))).thenReturn(folderListener);
        when(this.applicationContext.containsBean(anyString())).thenReturn(Boolean.TRUE);
        try {
            this.listenerService.terminateListener("testBean");
        } catch (final Exception e) {
            fail(e.getMessage());
        }

    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.listenerService = new DefaultListenerService(this.beanRegistrar, this.jobRegistry, this.jobRepository);
        ReflectionTestUtils.setField(this.listenerService, "applicationContext", this.applicationContext);
        this.job = DomainTestHelper.createJob("testJob");
        this.jobLauncher = new SimpleJobLauncher();
    }

}
