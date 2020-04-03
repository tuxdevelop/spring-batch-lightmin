package org.tuxdevelop.spring.batch.lightmin.server.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration.ServerSchedulerCoreConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ExecutionStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ServerSchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.service.SchedulerConfigurationService;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServerSchedulerCoreConfiguration.class})
public class SchedulerITNR {

    private static final String APP_NAME = "IT_NR_APP";

    @MockBean
    private JobServerService jobServerService;
    @MockBean
    private LightminApplicationRepository lightminApplicationRepository;
    @Autowired
    private SchedulerConfigurationService schedulerConfigurationService;
    @Autowired
    private SchedulerExecutionRepository schedulerExecutionRepository;

    @Test
    public void testNR() {
        this.setup();

        while (true) {
            final StringBuilder builder = new StringBuilder();
            this.schedulerExecutionRepository.findAll().forEach(
                    schedulerExecution -> builder.append(schedulerExecution).append("\n")
            );
            log.info("----> {}", builder.toString());
            try {
                Thread.sleep(5000L);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Before
    public void init() {
        //Mocks

        final LightminClientApplication lightminClientApplication = new LightminClientApplication();
        lightminClientApplication.setName(APP_NAME);
        when(this.lightminApplicationRepository.findByApplicationName(APP_NAME)).thenReturn(Collections.singleton(lightminClientApplication));
    }

    private void setup() {
        final SchedulerConfiguration config = this.createSchedulerConfiguration();
        this.createSchedulerExecution(config.getId(), new Date(), ExecutionStatus.NEW);
    }


    private SchedulerConfiguration createSchedulerConfiguration() {
        final SchedulerConfiguration schedulerConfiguration = new SchedulerConfiguration();
        schedulerConfiguration.setApplication(APP_NAME);
        schedulerConfiguration.setJobName("testJob");
        schedulerConfiguration.setMaxRetries(5);
        schedulerConfiguration.setInstanceExecutionCount(1);
        schedulerConfiguration.setRetryable(Boolean.TRUE);
        schedulerConfiguration.setCronExpression("0/5 * * * * ? *");
        schedulerConfiguration.setJobIncrementer(JobIncrementer.DATE);
        schedulerConfiguration.setStatus(ServerSchedulerStatus.ACTIVE);
        final Map<String, Object> jobParameters = new HashMap<>();
        jobParameters.put("LONG", 200L);
        jobParameters.put("STRING", "hello");
        schedulerConfiguration.setJobParameters(jobParameters);
        return this.schedulerConfigurationService.save(schedulerConfiguration);
    }

    protected SchedulerExecution createSchedulerExecution(final Long schedulerConfigurationId,
                                                          final Date nextExecution,
                                                          final Integer state) {
        final SchedulerExecution schedulerExecution = new SchedulerExecution();
        schedulerExecution.setExecutionCount(0);
        schedulerExecution.setState(state);
        schedulerExecution.setNextFireTime(nextExecution);
        schedulerExecution.setSchedulerConfigurationId(schedulerConfigurationId);
        return this.schedulerExecutionRepository.save(schedulerExecution);
    }
}
