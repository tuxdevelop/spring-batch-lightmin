package org.tuxdevelop.spring.batch.lightmin.documentation.api;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.batch.lightmin.ITConfigurationApplication;
import org.tuxdevelop.spring.batch.lightmin.ITJobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientInformation;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminProperties;
import org.tuxdevelop.spring.batch.lightmin.client.registration.LightminClientRegistrator;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.LightminServerProperties;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.service.AdminService;
import org.tuxdevelop.spring.batch.lightmin.support.ServiceEntry;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.fail;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
@SpringApplicationConfiguration(classes = {ITConfigurationApplication.class, ITJobConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractServiceDocumentation {

    @Autowired
    private AdminService adminService;
    @Autowired
    private EmbeddedWebApplicationContext embeddedWebApplicationContext;
    @Autowired
    private Job simpleJob;
    @Autowired
    private Job simpleBlockingJob;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    protected LightminClientProperties lightminClientProperties;
    @Autowired
    protected LightminProperties lightminProperties;
    @Autowired
    protected LightminServerProperties lightminServerProperties;
    @Autowired
    protected LightminClientRegistrator lightminClientRegistrator;
    @Autowired
    protected ServiceEntry serviceEntry;
    @Autowired
    protected JobExplorer jobExplorer;
    @Autowired
    protected RegistrationBean registrationBean;

    protected MyThread myThread;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    protected RequestSpecification documentationSpec;

    protected Long addedJobConfigurationId;
    protected Long addedListenerJobConfigurationId;
    protected Long launchedJobExecutionId;
    protected Long launchedJobInstanceId;
    protected Long launchedStepExecutionId;

    protected int getServerPort() {
        return embeddedWebApplicationContext.getEmbeddedServletContainer().getPort();
    }


    protected void addJobConfigurations() {
        final JobConfiguration jobConfiguration = createJobConfiguration();
        final JobConfiguration listenerJobConfiguration = createListenerJobConfiguration();
        adminService.saveJobConfiguration(jobConfiguration);
        adminService.saveJobConfiguration(listenerJobConfiguration);
        final Collection<JobConfiguration> jobConfigurations = adminService.getJobConfigurationsByJobName("simpleJob");
        for (final JobConfiguration configuration : jobConfigurations) {
            if (configuration.getJobSchedulerConfiguration() != null) {
                addedJobConfigurationId = configuration.getJobConfigurationId();
            }
            if (configuration.getJobListenerConfiguration() != null) {
                addedListenerJobConfigurationId = configuration.getJobConfigurationId();
            }
        }
    }

    protected void launchSimpleJob() {
        try {
            final JobExecution execution = jobLauncher.run(simpleJob, new JobParametersBuilder().addDate("date", new
                    Date()).toJobParameters());
            launchedJobExecutionId = execution.getId();
            launchedJobInstanceId = execution.getJobInstance().getId();
            launchedStepExecutionId = execution.getStepExecutions().iterator().next().getId();
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    protected void launchSimpleJobWithOutParameters() {
        try {
            final JobExecution execution = jobLauncher.run(simpleJob, new JobParametersBuilder().toJobParameters());
            launchedJobExecutionId = execution.getId();
            launchedJobInstanceId = execution.getJobInstance().getId();
            launchedStepExecutionId = execution.getStepExecutions().iterator().next().getId();
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    protected void launchSimpleBlockingJob() {
        try {
            myThread = new MyThread(jobLauncher, simpleBlockingJob);
            myThread.start();
            Thread.sleep(500);
            final Set<JobExecution> jobExecutions = jobExplorer.findRunningJobExecutions(simpleBlockingJob.getName());
            final JobExecution execution = jobExecutions.iterator().next();
            launchedJobExecutionId = execution.getId();
            launchedJobInstanceId = execution.getJobInstance().getId();
            launchedStepExecutionId = execution.getStepExecutions().iterator().next().getId();
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
        final LightminClientApplication lightminClientApplication = new LightminClientApplication();
        lightminClientApplication.setName(applicationName);
        lightminClientApplication.setServiceUrl("http://localhost:" + getServerPort());
        lightminClientApplication.setManagementUrl("http://localhost:" + getServerPort() + "/management");
        lightminClientApplication.setHealthUrl("http://localhost:" + getServerPort() + "/health");
        lightminClientApplication.setLightminClientInformation(new LightminClientInformation());
        return lightminClientApplication;
    }

    @Before
    public void init() {
        final int port = embeddedWebApplicationContext.getEmbeddedServletContainer().getPort();
        lightminClientProperties.setServiceUrl("http://localhost:" + port);
        lightminClientProperties.setServerPort(port);
        lightminClientProperties.setManagementPort(port);
        lightminProperties.setUrl(new String[]{"http://localhost:" + port});
        //lightminClientRegistrator.register();
        addJobConfigurations();
        this.documentationSpec = new RequestSpecBuilder().addFilter(documentationConfiguration(restDocumentation)).build();
        launchSimpleJob();
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
                jobLauncher.run(job, new JobParametersBuilder().toJobParameters());
            } catch (final Exception e) {
                fail(e.getMessage());
            }
        }

    }

}
