package org.tuxdevelop.spring.batch.lightmin.server.job;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

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

        final String uri = getClientUri(lightminClientApplication) + "/jobexecutions/{jobexecutionid}";
        final ResponseEntity<JobExecution> response = restTemplate.getForEntity(uri, JobExecution.class, jobExecutionId);
        checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public JobInstancePage getJobInstances(final String jobName, final Integer startIndex, final Integer pageSize, final LightminClientApplication lightminClientApplication) {
        final String uri = getClientUri(lightminClientApplication) + "/jobinstances";
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        final Integer startIndexParam = startIndex != null ? startIndex : 0;
        final Integer pageSizeParam = pageSize != null ? pageSize : 10;
        uriComponentsBuilder.queryParam("jobname", jobName);
        uriComponentsBuilder.queryParam("startindex", startIndexParam);
        uriComponentsBuilder.queryParam("pagesize", pageSizeParam);
        final ResponseEntity<JobInstancePage> response = restTemplate.getForEntity(uriComponentsBuilder.toUriString(), JobInstancePage.class, jobName);
        checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public JobInfo getJobInfo(final String jobName, final LightminClientApplication lightminClientApplication) {
        final String uri = getClientUri(lightminClientApplication) + "/jobinfos/{jobname}";
        final ResponseEntity<JobInfo> response = restTemplate.getForEntity(uri, JobInfo.class, jobName);
        checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public JobExecutionPage getJobExecutionPage(final Long jobInstanceId,
                                                final Integer startIndex,
                                                final Integer pageSize,
                                                final LightminClientApplication lightminClientApplication) {
        final String uri = getClientUri(lightminClientApplication) + "/jobexecutionpages";
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        final Integer startIndexParam = startIndex != null ? startIndex : 0;
        final Integer pageSizeParam = pageSize != null ? pageSize : 10;
        uriComponentsBuilder.queryParam("jobinstanceid", jobInstanceId);
        uriComponentsBuilder.queryParam("startindex", startIndexParam);
        uriComponentsBuilder.queryParam("pagesize", pageSizeParam);
        final ResponseEntity<JobExecutionPage> response = restTemplate.getForEntity(uriComponentsBuilder.toUriString(), JobExecutionPage.class, jobInstanceId);
        checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public JobExecutionPage getJobExecutionPage(final Long jobInstanceId, final LightminClientApplication lightminClientApplication) {
        final String uri = getClientUri(lightminClientApplication) + "/jobexecutionpages/all";
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        uriComponentsBuilder.queryParam("jobinstanceid", jobInstanceId);
        final ResponseEntity<JobExecutionPage> response = restTemplate.getForEntity(uriComponentsBuilder.toUriString(),
                JobExecutionPage.class,
                jobInstanceId);
        checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public void restartJobExecution(final Long jobExecutionId, final LightminClientApplication lightminClientApplication) {
        final String uri = getClientUri(lightminClientApplication) + "/jobexecutions/{jobexecutionid}/start";
        final ResponseEntity<Void> response = restTemplate.getForEntity(uri, Void.class, jobExecutionId);
        checkHttpOk(response);
    }

    @Override
    public void stopJobExecution(final Long jobExecutionId, final LightminClientApplication lightminClientApplication) {
        final String uri = getClientUri(lightminClientApplication) + "/jobexecutions/{jobexecutionid}/stop";
        final ResponseEntity<Void> response = restTemplate.getForEntity(uri, Void.class, jobExecutionId);
        checkHttpOk(response);
    }

    @Override
    public StepExecution getStepExecution(final Long jobExecutionId, final Long stepExecutionId, final LightminClientApplication lightminClientApplication) {
        final String uri = getClientUri(lightminClientApplication) + "/stepexecutions/{stepexecutionid}/jobexecutions/{jobexecutionid}";
        final ResponseEntity<StepExecution> response = restTemplate.getForEntity(uri, StepExecution.class, stepExecutionId, jobExecutionId);
        checkHttpOk(response);
        return response.getBody();
    }

    @Override
    public void launchJob(final JobLaunch jobLaunch, final LightminClientApplication lightminClientApplication) {
        final String uri = getClientUri(lightminClientApplication) + "/joblaunches";
        final ResponseEntity<Void> response = restTemplate.postForEntity(uri, jobLaunch, Void.class);
        if (!HttpStatus.CREATED.equals(response.getStatusCode())) {
            final String errorMessage = "ERROR - HTTP STATUS: " + response.getStatusCode();
            throw new SpringBatchLightminApplicationException(errorMessage);
        }
    }

    @Override
    public JobParameters getLastJobParameters(final String jobName, final LightminClientApplication
            lightminClientApplication) {
        final String uri = getClientUri(lightminClientApplication) + "/jobparameters";
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        uriComponentsBuilder.queryParam("jobname", jobName);
        final ResponseEntity<JobParameters> response = restTemplate.getForEntity(uriComponentsBuilder.toUriString(), JobParameters.class);
        checkHttpOk(response);
        return response.getBody();
    }


    private String getClientUri(final LightminClientApplication lightminClientApplication) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(lightminClientApplication.getServiceUrl());
        stringBuilder.append("/api");
        return stringBuilder.toString();
    }

    private void checkHttpOk(final ResponseEntity<?> responseEntity) {
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            final String errorMessage = "ERROR - HTTP STATUS: " + responseEntity.getStatusCode();
            throw new SpringBatchLightminApplicationException(errorMessage);
        }
    }

}
