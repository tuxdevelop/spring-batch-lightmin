package org.tuxdevelop.spring.batch.lightmin.api.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobExecution;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobInstanceExecutions;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobInstancePage;

import static org.assertj.core.api.Assertions.assertThat;

public class JobRestControllerIT extends CommonControllerIT {

    @Test
    //TODO: Fixme
    public void getJobExecutionByIdIT() {
        final Long jobExecutionId = launchedJobExecutionId;
        final String uri = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobRestControllerAPI.JOB_EXECUTIONS_JOB_EXECUTION_ID;

        final ResponseEntity<JobExecution> response = restTemplate.getForEntity(uri, JobExecution.class,
                jobExecutionId);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(jobExecutionId);
    }

    @Test
    public void getJobExecutionsByJobInstanceIdIT() {
        final Long jobInstanceId = launchedJobInstanceId;
        final String uri = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobRestControllerAPI.JOB_EXECUTION_PAGES_INSTANCE_ID;
        final ResponseEntity<JobInstanceExecutions> response = restTemplate.getForEntity(uri,
                JobInstanceExecutions.class,
                jobInstanceId);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getJobInstanceId()).isEqualTo(jobInstanceId);
    }

    @Test
    public void getJobInstancesByJobNameIT() {
        final String jobName = "simpleJob";
        final String uri = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobRestControllerAPI.JOB_INSTANCES_JOB_NAME;
        final ResponseEntity<JobInstancePage> response = restTemplate.getForEntity(uri, JobInstancePage.class,
                jobName);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getJobName()).isEqualTo(jobName);
    }

    @Before
    public void init() {
        launchSimpleJob();
    }
}
