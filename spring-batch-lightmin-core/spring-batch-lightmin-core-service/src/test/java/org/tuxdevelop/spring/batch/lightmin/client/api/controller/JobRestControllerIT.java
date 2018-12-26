package org.tuxdevelop.spring.batch.lightmin.client.api.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.batch.dao.QueryParameterKey;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JobRestControllerIT extends CommonControllerIT {

    @Test
    public void testGetJobExecutionById() {
        final Long jobExecutionId = this.launchedJobExecutionId;
        final String uri = LOCALHOST + ":" + this.getServerPort() + AbstractRestController
                .JobRestControllerAPI.JOB_EXECUTIONS_JOB_EXECUTION_ID;

        final ResponseEntity<JobExecution> response = this.restTemplate.getForEntity(uri, JobExecution.class,
                jobExecutionId);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(jobExecutionId);
    }

    @Test
    public void testGetAllJobExecutionsByJobInstanceId() {
        final Long jobInstanceId = this.launchedJobInstanceId;
        final String uri = LOCALHOST + ":" + this.getServerPort() + AbstractRestController
                .JobRestControllerAPI.JOB_EXECUTION_PAGES_INSTANCE_ID_ALL;
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        uriComponentsBuilder.queryParam("jobinstanceid", jobInstanceId);
        final ResponseEntity<JobExecutionPage> response = this.restTemplate.getForEntity(uriComponentsBuilder.toUriString(),
                JobExecutionPage.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTotalJobExecutionCount()).isEqualTo(1);
        assertThat(response.getBody().getJobExecutions()).hasSize(1);
        assertThat(response.getBody().getJobInstanceId()).isEqualTo(jobInstanceId);
    }

    @Test
    public void testGetJobInstancesByJobName() {
        final String jobName = "simpleJob";
        final String uri = LOCALHOST + ":" + this.getServerPort() + AbstractRestController
                .JobRestControllerAPI.JOB_INSTANCES_JOB_NAME;
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        uriComponentsBuilder.queryParam("jobname", jobName);
        final ResponseEntity<JobInstancePage> response = this.restTemplate.getForEntity(uriComponentsBuilder.toUriString(), JobInstancePage.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getJobName()).isEqualTo(jobName);
    }

    @Test
    public void testGetJobExecutionsByJobInstanceId() {
        final Long jobInstanceId = this.launchedJobInstanceId;
        final String uri = LOCALHOST + ":" + this.getServerPort() + AbstractRestController
                .JobRestControllerAPI.JOB_EXECUTION_PAGES_INSTANCE_ID;
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        uriComponentsBuilder.queryParam("jobinstanceid", jobInstanceId);
        uriComponentsBuilder.queryParam("startindex", Integer.valueOf(0).toString());
        uriComponentsBuilder.queryParam("pagesize", Integer.valueOf(10).toString());
        final ResponseEntity<JobExecutionPage> response = this.restTemplate.getForEntity(uriComponentsBuilder.toUriString(),
                JobExecutionPage.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTotalJobExecutionCount()).isEqualTo(1);
        assertThat(response.getBody().getJobExecutions()).hasSize(1);
        assertThat(response.getBody().getJobInstanceId()).isEqualTo(jobInstanceId);
    }

    @Test
    public void testgetJobInstancesByJobName() {
        final String jobName = "simpleJob";
        final String uri = LOCALHOST + ":" + this.getServerPort() + AbstractRestController.JobRestControllerAPI
                .JOB_INSTANCES_JOB_NAME;
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        uriComponentsBuilder.queryParam("jobname", jobName);
        final ResponseEntity<JobInstancePage> response = this.restTemplate.getForEntity(uriComponentsBuilder.toUriString(), JobInstancePage.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTotalJobInstanceCount()).isNotNull();
        assertThat(response.getBody().getJobInstances()).isNotEmpty();
        assertThat(response.getBody().getPageSize()).isNotNull();
        assertThat(response.getBody().getJobName()).isEqualTo(jobName);
    }

    @Test
    public void testGetApplicationJobInfo() {
        final String jobName = "simpleJob";
        final String uri = LOCALHOST + ":" + this.getServerPort() + AbstractRestController.JobRestControllerAPI.JOB_INFO_JOB_NAME;
        final ResponseEntity<JobInfo> response = this.restTemplate.getForEntity(uri, JobInfo.class, jobName);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getJobName()).isEqualTo(jobName);
    }

    @Test
    public void testGetStepExecution() {
        final String uri = LOCALHOST + ":" + this.getServerPort() + AbstractRestController.JobRestControllerAPI.STEP_EXECUTIONS;
        final ResponseEntity<StepExecution> response = this.restTemplate.getForEntity(uri, StepExecution.class, this.launchedStepExecutionId, this.launchedJobExecutionId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(this.launchedStepExecutionId);
        assertThat(response.getBody().getJobExecutionId()).isEqualTo(this.launchedJobExecutionId);
    }

    @Test
    public void testGetLastJobParameters() {
        final String jobName = "simpleJob";
        final String uri = LOCALHOST + ":" + this.getServerPort() + AbstractRestController.JobRestControllerAPI.JOB_PARAMETERS;
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        uriComponentsBuilder.queryParam("jobname", jobName);
        final ResponseEntity<JobParameters> response = this.restTemplate.getForEntity(uriComponentsBuilder.toUriString(), JobParameters.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void testQueryJobExecutionsAllQueryParameters() {
        final String jobName = "simpleJob";
        final Integer size = 4;
        final Date startDate = new Date(System.currentTimeMillis() - 100000);
        final Date endDate = new Date(System.currentTimeMillis() + 100000);
        final String exitStatus = org.springframework.batch.core.ExitStatus.COMPLETED.getExitCode();
        final Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(QueryParameterKey.EXIT_STATUS, exitStatus);
        queryParameters.put(QueryParameterKey.START_DATE, startDate);
        queryParameters.put(QueryParameterKey.END_DATE, endDate);
        final String uri = LOCALHOST + ":" + this.getServerPort() + AbstractRestController.JobRestControllerAPI.QUERY_JOB_EXECUTIONS;
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        uriComponentsBuilder.queryParam("jobname", jobName);
        uriComponentsBuilder.queryParam("resultsize", size);
        final ResponseEntity<JobExecution[]> response = this.restTemplate.postForEntity(uriComponentsBuilder.toUriString(), queryParameters, JobExecution[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final JobExecution[] body = response.getBody();
        assertThat(body).isNotNull().isNotEmpty();
        assertThat(body.length >= 1).isTrue();
        assertThat(body.length <= 4).isTrue();
    }

    @Before
    public void init() {
        this.cleanUp();
        this.launchSimpleJob();
    }
}
