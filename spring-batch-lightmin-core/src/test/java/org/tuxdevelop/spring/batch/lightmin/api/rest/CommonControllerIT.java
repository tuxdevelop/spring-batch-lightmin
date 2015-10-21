package org.tuxdevelop.spring.batch.lightmin.api.rest;


import lombok.Getter;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.tuxdevelop.spring.batch.lightmin.ITConfigurationApplication;
import org.tuxdevelop.spring.batch.lightmin.ITJobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.service.AdminService;

import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
@SpringApplicationConfiguration(classes = {ITConfigurationApplication.class, ITJobConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class CommonControllerIT {

    public static final String LOCALHOST = "http://localhost";

    @Autowired
    @Getter
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AdminService adminService;

    @Autowired
    private EmbeddedWebApplicationContext embeddedWebApplicationContext;

    @Autowired
    private Job simpleJob;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    protected RestTemplate restTemplate;

    protected Long addedJobConfigurationId;

    protected Long launchedJobExecutionId;
    protected Long launchedJobInstanceId;

    protected int getServerPort() {
        return embeddedWebApplicationContext.getEmbeddedServletContainer().getPort();
    }


    protected void addJobConfigurations() {
        final JobConfiguration jobConfiguration = createJobConfiguration();
        adminService.saveJobConfiguration(jobConfiguration);
        final Collection<JobConfiguration> jobConfigurations = adminService.getJobConfigurationsByJobName("simpleJob");
        for (final JobConfiguration configuration : jobConfigurations) {
            addedJobConfigurationId = configuration.getJobConfigurationId();
        }
    }

    protected void launchSimpleJob() {
        try {
            final JobExecution execution = jobLauncher.run(simpleJob, new JobParametersBuilder().addDate("date", new
                    Date()).toJobParameters());
            launchedJobExecutionId = execution.getId();
            launchedJobInstanceId = execution.getJobInstance().getId();
        } catch (final Exception e) {
            fail(e.getMessage());
        }
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

}
