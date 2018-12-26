package org.tuxdevelop.spring.batch.lightmin.documentation.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.ITConfigurationApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.classic.configuration.LightminClientClassicConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.client.classic.service.LightminClientRegistratorService;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.LightminServerCoreProperties;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.service.AdminService;
import org.tuxdevelop.spring.batch.lightmin.service.ServiceEntry;
import org.tuxdevelop.test.configuration.ITJobConfiguration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.fail;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {ITConfigurationApplication.class, ITJobConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractServiceDocumentation {

    @Autowired
    private AdminService adminService;
    @Autowired
    private Job simpleJob;
    @Autowired
    private Job simpleBlockingJob;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    protected LightminClientProperties lightminClientProperties;
    @Autowired
    protected LightminClientClassicConfigurationProperties lightminProperties;
    @Autowired
    protected LightminServerCoreProperties lightminServerCoreProperties;
    @Autowired
    protected LightminClientRegistratorService lightminClientRegistrator;
    @Autowired
    protected ServiceEntry serviceEntry;
    @Autowired
    protected JobExplorer jobExplorer;
    @Autowired
    protected RegistrationBean registrationBean;
    protected MyThread myThread;
    @LocalServerPort
    private Integer serverPort;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    protected RequestSpecification documentationSpec;

    protected Long addedJobConfigurationId;
    protected Long addedListenerJobConfigurationId;
    protected Long launchedJobExecutionId;
    protected Long launchedJobInstanceId;
    protected Long launchedStepExecutionId;

    protected int getServerPort() {
        return this.serverPort;
    }


    protected void addJobConfigurations() {
        final JobConfiguration jobConfiguration = this.createJobConfiguration();
        final JobConfiguration listenerJobConfiguration = this.createListenerJobConfiguration();
        this.adminService.saveJobConfiguration(jobConfiguration);
        this.adminService.saveJobConfiguration(listenerJobConfiguration);
        final Collection<JobConfiguration> jobConfigurations = this.adminService.getJobConfigurationsByJobName("simpleJob");
        for (final JobConfiguration configuration : jobConfigurations) {
            if (configuration.getJobSchedulerConfiguration() != null) {
                this.addedJobConfigurationId = configuration.getJobConfigurationId();
            }
            if (configuration.getJobListenerConfiguration() != null) {
                this.addedListenerJobConfigurationId = configuration.getJobConfigurationId();
            }
        }
    }

    protected void launchSimpleJob() {
        try {
            final JobExecution execution = this.jobLauncher.run(this.simpleJob, new JobParametersBuilder().addDate("date", new
                    Date()).toJobParameters());
            this.launchedJobExecutionId = execution.getId();
            this.launchedJobInstanceId = execution.getJobInstance().getId();
            this.launchedStepExecutionId = execution.getStepExecutions().iterator().next().getId();
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    protected void launchSimpleJobWithOutParameters() {
        try {
            final JobExecution execution = this.jobLauncher.run(this.simpleJob, new JobParametersBuilder().toJobParameters());
            this.launchedJobExecutionId = execution.getId();
            this.launchedJobInstanceId = execution.getJobInstance().getId();
            this.launchedStepExecutionId = execution.getStepExecutions().iterator().next().getId();
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    protected void launchSimpleBlockingJob() {
        try {
            this.myThread = new MyThread(this.jobLauncher, this.simpleBlockingJob);
            this.myThread.start();
            Thread.sleep(500);
            final Set<JobExecution> jobExecutions = this.jobExplorer.findRunningJobExecutions(this.simpleBlockingJob.getName());
            final JobExecution execution = jobExecutions.iterator().next();
            this.launchedJobExecutionId = execution.getId();
            this.launchedJobInstanceId = execution.getJobInstance().getId();
            this.launchedStepExecutionId = execution.getStepExecutions().iterator().next().getId();
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    protected org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration createApiJobConfiguration() {
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerConfiguration jobListenerConfiguration
                = new org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerConfiguration();
        jobListenerConfiguration.setListenerStatus(org.tuxdevelop.spring.batch.lightmin.api.resource.admin.ListenerStatus.STOPPED);
        jobListenerConfiguration.setPollerPeriod(1000L);
        jobListenerConfiguration.setSourceFolder("src/test/resources/input");
        jobListenerConfiguration.setFilePattern("*_input.txt");
        jobListenerConfiguration.setJobListenerType(org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setTaskExecutorType(org.tuxdevelop.spring.batch.lightmin.api.resource.admin.TaskExecutorType.SYNCHRONOUS);
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration jobConfiguration
                = new org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration();
        jobConfiguration.setJobName("simpleJob");
        jobConfiguration.setJobIncrementer(org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer.DATE);
        jobConfiguration.setJobListenerConfiguration(jobListenerConfiguration);
        return jobConfiguration;
    }

    protected JobConfiguration createJobConfiguration() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setFixedDelay(100000L);
        jobSchedulerConfiguration.setInitialDelay(100000L);
        jobSchedulerConfiguration.setJobSchedulerType(JobSchedulerType.PERIOD);
        jobSchedulerConfiguration.setTaskExecutorType(TaskExecutorType.ASYNCHRONOUS);
        jobSchedulerConfiguration.setSchedulerStatus(SchedulerStatus.INITIALIZED);
        final JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setJobName("simpleJob");
        jobConfiguration.setJobIncrementer(JobIncrementer.DATE);
        jobConfiguration.setJobSchedulerConfiguration(jobSchedulerConfiguration);
        return jobConfiguration;
    }

    protected JobConfiguration createListenerJobConfiguration() {
        final JobListenerConfiguration jobListenerConfiguration = new JobListenerConfiguration();
        jobListenerConfiguration.setListenerStatus(ListenerStatus.STOPPED);
        jobListenerConfiguration.setPollerPeriod(1000L);
        jobListenerConfiguration.setSourceFolder("src/test/resources");
        jobListenerConfiguration.setFilePattern("*_input.txt");
        jobListenerConfiguration.setJobListenerType(JobListenerType.LOCAL_FOLDER_LISTENER);
        jobListenerConfiguration.setTaskExecutorType(TaskExecutorType.ASYNCHRONOUS);
        final JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setJobName("simpleJob");
        jobConfiguration.setJobIncrementer(JobIncrementer.DATE);
        jobConfiguration.setJobListenerConfiguration(jobListenerConfiguration);
        return jobConfiguration;
    }

    protected LightminClientApplication createLightminClientApplication(final String applicationName) {
        return LightminClientApplication.createApplication
                (Arrays.asList(this.simpleJob.getName()), this.lightminClientProperties);
    }


    protected void cleanUp() {
        try {
            final Collection<JobConfiguration> allJC = this.adminService.getJobConfigurationsByJobName("simpleJob");
            for (final JobConfiguration jobConfiguration : allJC) {
                this.adminService.deleteJobConfiguration(jobConfiguration.getJobConfigurationId());
            }
        } catch (final SpringBatchLightminApplicationException e) {
            log.warn("Repository clean up failed");
        }
    }

    @Before
    public void init() {
        this.cleanUp();
        final int port = this.serverPort;
        this.lightminClientProperties.setServiceUrl("http://localhost:" + port);
        this.lightminClientProperties.setServerPort(port);
        this.lightminClientProperties.setManagementPort(port);
        this.lightminProperties.getServer().setUrl(new String[]{"http://localhost:" + port});
        //lightminClientRegistrator.register();
        this.addJobConfigurations();
        this.documentationSpec = new RequestSpecBuilder().addFilter(documentationConfiguration(this.restDocumentation)).build();
        this.launchSimpleJob();
    }

    public static class MyThread extends Thread {

        private final JobLauncher jobLauncher;
        private final Job job;

        private MyThread(final JobLauncher jobLauncher, final Job job) {
            this.jobLauncher = jobLauncher;
            this.job = job;
        }

        @Override
        public void run() {
            try {
                this.jobLauncher.run(this.job, new JobParametersBuilder().toJobParameters());
            } catch (final Exception e) {
                fail(e.getMessage());
            }
        }

    }

}
