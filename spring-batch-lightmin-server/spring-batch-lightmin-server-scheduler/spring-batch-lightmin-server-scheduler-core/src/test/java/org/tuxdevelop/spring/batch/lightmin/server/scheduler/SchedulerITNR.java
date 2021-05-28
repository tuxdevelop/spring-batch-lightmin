package org.tuxdevelop.spring.batch.lightmin.server.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JobExecutionEventRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JournalRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration.ServerSchedulerCoreConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration.ServerSchedulerCoreConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.MapSchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.MapSchedulerExecutionRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ExecutionStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ServerSchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.service.*;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServerSchedulerCoreConfiguration.class, MockedServerCoreRepoConfig.class,
        SchedulerITNR.MockConfig.class}, properties = "spring.batch.lightmin.server.metrics-enabled=false")
public class SchedulerITNR {

    private static final String APP_NAME = "IT_NR_APP";


    @Configuration
    static class MockConfig {
        @Bean
        SchedulerExecutionRepository executionRepository() {
            return new MapSchedulerExecutionRepository();
        }

        @Bean
        SchedulerConfigurationRepository schedulerConfigurationRepository() {
            return new MapSchedulerConfigurationRepository();
        }

        @Bean
        public ExecutionPollerService executionPollerService(final ServerSchedulerService serverSchedulerService,
                                                             final SchedulerExecutionService schedulerExecutionService,
                                                             final ServerSchedulerCoreConfigurationProperties properties) {
            return new StandaloneExecutionPollerService(serverSchedulerService, schedulerExecutionService, properties);
        }

        @Bean
        public ExecutionCleanUpService executionCleanUpService(final SchedulerExecutionRepository schedulerExecutionRepository,
                                                               final ServerSchedulerCoreConfigurationProperties properties) {
            return new StandaloneExecutionCleanupService(schedulerExecutionRepository, properties);
        }
    }

    @MockBean
    private JobServerService jobServerService;
    @Autowired
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
