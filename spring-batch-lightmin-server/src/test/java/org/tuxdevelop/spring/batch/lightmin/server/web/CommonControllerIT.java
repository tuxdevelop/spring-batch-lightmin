package org.tuxdevelop.spring.batch.lightmin.server.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.server.ITConfigurationApplication;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.service.ListenerService;
import org.tuxdevelop.spring.batch.lightmin.service.SchedulerService;
import org.tuxdevelop.test.configuration.ITConfigurationEmbedded;

import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Fail.fail;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ITConfigurationApplication.class, ITConfigurationEmbedded.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class CommonControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private JobConfigurationRepository jobConfigurationRepository;
    @Autowired
    private Job simpleJob;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private LightminApplicationRepository lightminApplicationRepository;
    @Autowired
    private ListenerService listenerService;
    @Autowired
    private RegistrationBean registrationBean;
    @Autowired
    private LightminClientProperties lightminClientProperties;
    @Autowired
    private SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;

    protected MockMvc mockMvc;
    protected Long launchedJobExecutionId;
    protected Long launchedJobInstanceId;
    protected Long launchedStepExecutionId;
    protected JobConfiguration addedJobConfiguration;
    protected JobExecution jobExecution;
    protected String applicationId;
    protected JobConfiguration addedListenerJobConfiguration;

    @Before
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        addJobConfigurations();
        addJobListenerConfiguration();
        launchSimpleJob();
        if (this.lightminApplicationRepository.findAll().size() <= 0) {
            this.registrationBean.register(LightminClientApplication.createApplication(
                    Collections.singletonList(this.simpleJob.getName()),
                    this.lightminClientProperties));
        }
        this.applicationId = this.lightminApplicationRepository.findAll().iterator().next().getId();
    }

    @After
    public void clear() {
        this.lightminApplicationRepository.clear();
    }

    protected void addJobConfigurations() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setBeanName("testBean");
        jobSchedulerConfiguration.setFixedDelay(100000L);
        jobSchedulerConfiguration.setInitialDelay(100000L);
        jobSchedulerConfiguration.setJobSchedulerType(JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setTaskExecutorType(TaskExecutorType.ASYNCHRONOUS);
        jobSchedulerConfiguration.setSchedulerStatus(SchedulerStatus.INITIALIZED);
        final JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setJobName("simpleJob");
        jobConfiguration.setJobConfigurationId(1L);
        jobConfiguration.setJobIncrementer(JobIncrementer.DATE);
        jobConfiguration.setJobSchedulerConfiguration(jobSchedulerConfiguration);
        this.addedJobConfiguration = this.jobConfigurationRepository.add(jobConfiguration,
                this.springBatchLightminConfigurationProperties.getApplicationName());
        this.schedulerService.registerSchedulerForJob(this.addedJobConfiguration);
    }

    protected void addJobListenerConfiguration() {
        final JobListenerConfiguration jobListenerConfiguration = new JobListenerConfiguration();
        jobListenerConfiguration.setListenerStatus(ListenerStatus.STOPPED);
        jobListenerConfiguration.setPollerPeriod(1000L);
        jobListenerConfiguration.setFilePattern("*.txt");
        jobListenerConfiguration.setSourceFolder("src/test");
        jobListenerConfiguration.setTaskExecutorType(TaskExecutorType.SYNCHRONOUS);
        jobListenerConfiguration.setBeanName("myTestBean");
        jobListenerConfiguration.setJobListenerType(JobListenerType.LOCAL_FOLDER_LISTENER);
        final JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setJobName("simpleJob");
        jobConfiguration.setJobConfigurationId(1L);
        jobConfiguration.setJobIncrementer(JobIncrementer.DATE);
        jobConfiguration.setJobListenerConfiguration(jobListenerConfiguration);
        this.addedListenerJobConfiguration = this.jobConfigurationRepository.add(jobConfiguration,
                this.springBatchLightminConfigurationProperties.getApplicationName());
        this.listenerService.registerListenerForJob(this.addedListenerJobConfiguration);
    }

    private void launchSimpleJob() {
        try {
            final JobExecution execution = this.jobLauncher.run(this.simpleJob, new JobParametersBuilder().addLong("time",
                    System.currentTimeMillis()).toJobParameters());
            this.launchedJobExecutionId = execution.getId();
            this.launchedJobInstanceId = execution.getJobInstance().getId();
            final Collection<StepExecution> stepExecutions = execution.getStepExecutions();
            for (final StepExecution stepExecution : stepExecutions) {
                this.launchedStepExecutionId = stepExecution.getId();
            }
            this.jobExecution = execution;

        } catch (final Exception e) {
            if (e instanceof JobExecutionAlreadyRunningException) {
                log.info("Job Already Running");
            } else {
                fail(e.getMessage());
            }
        }
    }

}
