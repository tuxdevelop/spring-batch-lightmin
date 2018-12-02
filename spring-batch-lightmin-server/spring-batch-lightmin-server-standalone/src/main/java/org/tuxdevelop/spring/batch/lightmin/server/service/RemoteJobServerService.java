package org.tuxdevelop.spring.batch.lightmin.server.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.util.RequestUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Marcel Becker
 * @version 0.3
 */
public class RemoteJobServerService implements JobServerService {

    private final RestTemplate restTemplate;

    public RemoteJobServerService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public JobExecution getJobExecution(final Long jobExecutionId,
                                        final LightminClientApplication lightminClientApplication) {

        final String uri = this.getClientUri(lightminClientApplication) + "/jobexecutions/{jobexecutionid}";
        final ResponseEntity<JobExecution> response =
                this.restTemplate.getForEntity(uri, JobExecution.class, jobExecutionId);
        this.checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public JobInstancePage getJobInstances(final String jobName,
                                           final Integer startIndex,
                                           final Integer pageSize,
                                           final LightminClientApplication lightminClientApplication) {
        final String uri = this.getClientUri(lightminClientApplication) + "/jobinstances";
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        final Integer startIndexParam = startIndex != null ? startIndex : 0;
        final Integer pageSizeParam = pageSize != null ? pageSize : 10;
        uriComponentsBuilder.queryParam("jobname", jobName);
        uriComponentsBuilder.queryParam("startindex", startIndexParam);
        uriComponentsBuilder.queryParam("pagesize", pageSizeParam);
        final ResponseEntity<JobInstancePage> response =
                this.restTemplate.getForEntity(uriComponentsBuilder.toUriString(), JobInstancePage.class, jobName);
        this.checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public JobInfo getJobInfo(final String jobName, final LightminClientApplication lightminClientApplication) {
        final String uri = this.getClientUri(lightminClientApplication) + "/jobinfos/{jobname}";
        final ResponseEntity<JobInfo> response = this.restTemplate.getForEntity(uri, JobInfo.class, jobName);
        this.checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public JobExecutionPage getJobExecutionPage(final Long jobInstanceId,
                                                final Integer startIndex,
                                                final Integer pageSize,
                                                final LightminClientApplication lightminClientApplication) {
        final String uri = this.getClientUri(lightminClientApplication) + "/jobexecutionpages";
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        final Integer startIndexParam = startIndex != null ? startIndex : 0;
        final Integer pageSizeParam = pageSize != null ? pageSize : 10;
        uriComponentsBuilder.queryParam("jobinstanceid", jobInstanceId);
        uriComponentsBuilder.queryParam("startindex", startIndexParam);
        uriComponentsBuilder.queryParam("pagesize", pageSizeParam);
        final ResponseEntity<JobExecutionPage> response =
                this.restTemplate.getForEntity(uriComponentsBuilder.toUriString(), JobExecutionPage.class, jobInstanceId);
        this.checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public JobExecutionPage getJobExecutionPage(final Long jobInstanceId,
                                                final LightminClientApplication lightminClientApplication) {
        final String uri = this.getClientUri(lightminClientApplication) + "/jobexecutionpages/all";
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        uriComponentsBuilder.queryParam("jobinstanceid", jobInstanceId);
        final ResponseEntity<JobExecutionPage> response = this.restTemplate.getForEntity(uriComponentsBuilder.toUriString(),
                JobExecutionPage.class,
                jobInstanceId);
        this.checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public void restartJobExecution(final Long jobExecutionId,
                                    final LightminClientApplication lightminClientApplication) {
        final String uri = this.getClientUri(lightminClientApplication) + "/jobexecutions/{jobexecutionid}/restart";
        final ResponseEntity<Void> response = this.restTemplate.getForEntity(uri, Void.class, jobExecutionId);
        this.checkHttpOk(response);
    }

    @Override
    public void stopJobExecution(final Long jobExecutionId, final LightminClientApplication lightminClientApplication) {
        final String uri = this.getClientUri(lightminClientApplication) + "/jobexecutions/{jobexecutionid}/stop";
        final ResponseEntity<Void> response = this.restTemplate.getForEntity(uri, Void.class, jobExecutionId);
        this.checkHttpOk(response);
    }

    @Override
    public StepExecution getStepExecution(final Long jobExecutionId,
                                          final Long stepExecutionId,
                                          final LightminClientApplication lightminClientApplication) {
        final String uri = this.getClientUri(lightminClientApplication)
                + "/stepexecutions/{stepexecutionid}/jobexecutions/{jobexecutionid}";
        final ResponseEntity<StepExecution> response =
                this.restTemplate.getForEntity(uri, StepExecution.class, stepExecutionId, jobExecutionId);
        this.checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public void launchJob(final JobLaunch jobLaunch, final LightminClientApplication lightminClientApplication) {
        final String uri = this.getClientUri(lightminClientApplication) + "/joblaunches";
        final HttpEntity<JobLaunch> entity = RequestUtil.createApplicationJsonEntity(jobLaunch);

        final ResponseEntity<Void> response = this.restTemplate.postForEntity(uri, entity, Void.class);
        if (!HttpStatus.CREATED.equals(response.getStatusCode())) {
            final String errorMessage = "ERROR - HTTP STATUS: " + response.getStatusCode();
            throw new SpringBatchLightminApplicationException(errorMessage);
        }
    }

    @Override
    public JobParameters getLastJobParameters(final String jobName, final LightminClientApplication
            lightminClientApplication) {
        final String uri = this.getClientUri(lightminClientApplication) + "/jobparameters";
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        uriComponentsBuilder.queryParam("jobname", jobName);
        final ResponseEntity<JobParameters> response =
                this.restTemplate.getForEntity(uriComponentsBuilder.toUriString(), JobParameters.class);
        this.checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public List<JobExecution> findJobExecutions(final String jobName,
                                                final LightminClientApplication lightminClientApplication,
                                                final Map<String, Object> queryParameter,
                                                final Integer resultSize) {
        final HttpEntity<Map<String, Object>> entity = RequestUtil.createApplicationJsonEntity(queryParameter);
        final String uri = this.getClientUri(lightminClientApplication) + "/jobexecutions/query";
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        uriComponentsBuilder.queryParam("jobname", jobName);
        uriComponentsBuilder.queryParam("resultsize", resultSize);
        final ResponseEntity<JobExecution[]> response =
                this.restTemplate.postForEntity(uriComponentsBuilder.toUriString(), entity, JobExecution[].class);
        this.checkHttpOk(response);
        return Arrays.asList(response.getBody());
    }


    private String getClientUri(final LightminClientApplication lightminClientApplication) {
        return lightminClientApplication.getServiceUrl() + "/api";
    }

    private void checkHttpOk(final ResponseEntity<?> responseEntity) {
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            final String errorMessage = "ERROR - HTTP STATUS: " + responseEntity.getStatusCode();
            throw new SpringBatchLightminApplicationException(errorMessage);
        }
    }

}
