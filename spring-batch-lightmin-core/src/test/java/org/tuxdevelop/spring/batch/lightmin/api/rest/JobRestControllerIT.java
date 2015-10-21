package org.tuxdevelop.spring.batch.lightmin.api.rest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.tuxdevelop.spring.batch.lightmin.api.rest.response.JobExecutionResponse;
import org.tuxdevelop.spring.batch.lightmin.api.rest.response.JobInstanceExecutionsResponse;
import org.tuxdevelop.spring.batch.lightmin.api.rest.response.JobInstancesResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class JobRestControllerIT extends CommonControllerIT {

    @Test
    public void getJobExecutionByIdIT() {
        final Long jobExecutionId = launchedJobExecutionId;
        final String uri = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobRestControllerAPI.JOB_EXECUTIONS_JOB_EXECUTION_ID;

        final ResponseEntity<JobExecutionResponse> response = restTemplate.getForEntity(uri, JobExecutionResponse.class,
                jobExecutionId);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(jobExecutionId);
    }

    @Test
    public void getJobExecutionsByJobInstanceIdIT() {
        final Long jobInstanceId = launchedJobInstanceId;
        final String uri = LOCALHOST + ":" + getServerPort() + AbstractRestController
                .JobRestControllerAPI.JOB_EXECUTIONS_JOB_INSTANCES_JOB_INSTANCE_ID;
        final ResponseEntity<JobInstanceExecutionsResponse> response = restTemplate.getForEntity(uri, JobInstanceExecutionsResponse.class,
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
        final ResponseEntity<JobInstancesResponse> response = restTemplate.getForEntity(uri, JobInstancesResponse.class,
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
