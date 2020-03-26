package org.tuxdevelop.spring.batch.lightmin.server.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;
import org.tuxdevelop.test.configuration.ITServerConfiguration;
import org.tuxdevelop.test.configuration.ITServerConfigurationApplication;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ITServerConfigurationApplication.class, ITServerConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobExecutionEventControllerIT {

    private static final String LOCALHOST = "http://localhost";

    @Autowired
    private RestTemplate restTemplate;
    @LocalServerPort
    private Integer serverPort;
    @Autowired
    private EventService eventService;

    @Test
    public void testConsumeJobExecutionFailedEvent() {
        final JobExecutionEventInfo jobExecutionEventInfo = new JobExecutionEventInfo();
        jobExecutionEventInfo.setJobName("test");
        jobExecutionEventInfo.setJobExecutionId(1L);
        jobExecutionEventInfo.setEndDate(new Date());
        jobExecutionEventInfo.setStartDate(new Date());
        jobExecutionEventInfo.setExitStatus(new ExitStatus(org.springframework.batch.core.ExitStatus.COMPLETED.getExitCode()));
        final ResponseEntity<Void> response = this.restTemplate.postForEntity(
                LOCALHOST + ":" + this.getServerPort() + "/api/events/jobexecutions", jobExecutionEventInfo, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        final List<JobExecutionEventInfo> events = this.eventService.getAllJobExecutionEvents(0, 10);
        assertThat(events).isNotEmpty();
    }

    private int getServerPort() {
        return this.serverPort;
    }
}
