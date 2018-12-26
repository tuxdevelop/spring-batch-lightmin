package org.tuxdevelop.spring.batch.lightmin.client.api.controller;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.service.ServiceEntry;

import java.util.List;
import java.util.Map;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@ResponseBody
@RequestMapping("/")
public class JobRestController extends AbstractRestController implements InitializingBean {

    private final ServiceEntry serviceEntry;

    public JobRestController(final ServiceEntry serviceEntry) {
        this.serviceEntry = serviceEntry;
    }

    /**
     * Retrieves a {@link JobExecution} for a given id
     *
     * @param jobExecutionId the id of the jobExecution
     * @return the JobExecution
     */
    @GetMapping(
            value = JobRestControllerAPI.JOB_EXECUTIONS_JOB_EXECUTION_ID,
            produces = PRODUCES)
    public ResponseEntity<JobExecution> getJobExecutionById(
            @PathVariable(value = "jobexecutionid") final Long jobExecutionId) {
        final JobExecution jobExecution = this.serviceEntry.getByJobExecutionId(jobExecutionId);
        return ResponseEntity.ok(jobExecution);
    }

    /**
     * Retrieves {@link JobExecutionPage} containing {@link JobExecution}s for a given
     * {@link org.springframework.batch.core.JobInstance} id
     *
     * @param jobInstanceId the id of the JobInstance
     * @param startIndex    the index position of the page
     * @param pageSize      the size of the page
     * @return the JobExecutionPage
     */
    @GetMapping(
            value = JobRestControllerAPI.JOB_EXECUTION_PAGES_INSTANCE_ID,
            produces = PRODUCES)
    public ResponseEntity<JobExecutionPage> getJobExecutionsByJobInstanceId(
            @RequestParam("jobinstanceid") final Long jobInstanceId,
            @RequestParam(value = "startindex", defaultValue = "0") final int startIndex,
            @RequestParam(value = "pagesize", defaultValue = "10") final int pageSize) {

        final JobExecutionPage jobInstanceExecutions =
                this.serviceEntry.getJobExecutionPage(jobInstanceId, startIndex, pageSize);
        return ResponseEntity.ok(jobInstanceExecutions);
    }

    /**
     * Retrieves {@link JobExecutionPage} containing {@link JobExecution}s for a given
     * {@link org.springframework.batch.core.JobInstance} id
     *
     * @param jobInstanceId the id of the JobInstance
     * @return the JobExecutionPage
     */
    @GetMapping(
            value = JobRestControllerAPI.JOB_EXECUTION_PAGES_INSTANCE_ID_ALL,
            produces = PRODUCES)
    public ResponseEntity<JobExecutionPage> getAllJobExecutionsByJobInstanceId(
            @RequestParam("jobinstanceid") final Long
                    jobInstanceId) {
        final JobExecutionPage jobInstanceExecutions = this.serviceEntry.getJobExecutionPage(jobInstanceId);
        return ResponseEntity.ok(jobInstanceExecutions);
    }

    /**
     * Retrieves {@link JobInstancePage} containing {@link JobInstance}s for a given name of a
     * {@link org.springframework.batch.core.Job}
     *
     * @param jobName    the name of the Spring Batch Job
     * @param startIndex the index position of the page
     * @param pageSize   the size of the page
     * @return the JobInstancePage
     */
    @GetMapping(
            value = JobRestControllerAPI.JOB_INSTANCES_JOB_NAME,
            produces = PRODUCES)
    public ResponseEntity<JobInstancePage> getJobInstancesByJobName(
            @RequestParam("jobname") final String jobName,
            @RequestParam(value = "startindex", defaultValue = "0") final int startIndex,
            @RequestParam(value = "pagesize", defaultValue = "10") final int pageSize) {
        final JobInstancePage jobInstancePage =
                this.serviceEntry.getJobInstancesByJobName(jobName, startIndex, pageSize);
        return ResponseEntity.ok(jobInstancePage);
    }

    /**
     * Retrieves high level {@link ApplicationJobInfo} of the Application
     *
     * @return the ApplicationJobInfo
     */
    @GetMapping(
            value = JobRestControllerAPI.APPLICATION_JOB_INFO,
            produces = PRODUCES)
    public ResponseEntity<ApplicationJobInfo> getApplicationJobInfo() {
        final ApplicationJobInfo applicationJobInfo = this.serviceEntry.getApplicationJobInfo();
        return ResponseEntity.ok(applicationJobInfo);
    }

    /**
     * Retrieves high level {@link JobInfo} of a {@link org.springframework.batch.core.Job} for a given job name
     *
     * @param jobName the name of the Spring Batch Job
     * @return the JobInfo
     */
    @GetMapping(
            value = JobRestControllerAPI.JOB_INFO_JOB_NAME,
            produces = PRODUCES)
    public ResponseEntity<JobInfo> getJobInfo(@PathVariable("jobname") final String jobName) {
        final JobInfo jobInfo = this.serviceEntry.getJobInfo(jobName);
        return ResponseEntity.ok(jobInfo);
    }

    /**
     * Restarts a {@link org.springframework.batch.core.JobExecution} of a given id
     *
     * @param jobExecutionId the id of the JobExecution
     * @return HTTP Status Code 200
     */
    @GetMapping(
            value = JobRestControllerAPI.JOB_EXECUTIONS_RESTART)
    public ResponseEntity<Void> restartJobExecution(@PathVariable("jobexecutionid") final Long jobExecutionId) {
        this.serviceEntry.restartJobExecution(jobExecutionId);
        return ResponseEntity.ok().build();
    }

    /**
     * Stops a running {@link org.springframework.batch.core.JobExecution} of a given id
     *
     * @param jobExecutionId the id of the JobExecution
     * @return HTTP Status Code 200
     */
    @GetMapping(
            value = JobRestControllerAPI.JOB_EXECUTIONS_STOP)
    public ResponseEntity<Void> stopJobExecution(@PathVariable("jobexecutionid") final Long jobExecutionId) {
        this.serviceEntry.stopJobExecution(jobExecutionId);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves a {@link StepExecution} of a {@link org.springframework.batch.core.JobExecution}
     *
     * @param jobExecutionId  the id of the {@link org.springframework.batch.core.JobExecution}
     * @param stepExecutionId the id of the {@link org.springframework.batch.core.StepExecution}
     * @return the StepExecution
     */
    @GetMapping(
            value = JobRestControllerAPI.STEP_EXECUTIONS,
            produces = PRODUCES)
    public ResponseEntity<StepExecution> getStepExecution(
            @PathVariable("jobexecutionid") final Long jobExecutionId,
            @PathVariable("stepexecutionid") final Long stepExecutionId) {
        final StepExecution stepExecution = this.serviceEntry.getStepExecution(jobExecutionId, stepExecutionId);
        return ResponseEntity.ok(stepExecution);
    }

    /**
     * Retrieves the last used {@link JobParameters} of a {@link org.springframework.batch.core.Job} run
     *
     * @param jobName the name of Spring Batch Job
     * @return the JobParamaters
     */
    @GetMapping(
            value = JobRestControllerAPI.JOB_PARAMETERS,
            produces = PRODUCES)
    public ResponseEntity<JobParameters> getLastJobParameters(@RequestParam("jobname") final String jobName) {
        final JobParameters jobParameters = this.serviceEntry.getLastJobParameters(jobName);
        return ResponseEntity.ok(jobParameters);
    }


    /**
     * Retrieves {@link JobExecution}s for a given Job Name and query parameters
     *
     * @param jobName        name of the job, if null or empty, all known job names will be queried
     * @param queryParameter query parameters
     *                       EXIT_STATUS - String value of the Spring Batch {@link ExitStatus}
     *                       START_TIME - Start time of the JobExecution {@link java.util.Date}
     *                       END_TIME - End time of the JobExecution {@link java.util.Date}
     * @param resultSize     maximum size of the result set, Default value = 200
     * @return all found {@link JobExecution}s depending on the query Parameters
     */
    @PostMapping(
            value = JobRestControllerAPI.QUERY_JOB_EXECUTIONS,
            produces = PRODUCES,
            consumes = CONSUMES)
    public ResponseEntity<List<JobExecution>> queryJobExecutions(
            @RequestParam(name = "jobname", required = false) final String jobName,
            @RequestParam(name = "resultsize", defaultValue = "200") final Integer resultSize,
            @RequestBody final Map<String, Object> queryParameter) {
        final String jobNameQueryParameter = StringUtils.hasText(jobName) ? jobName : null;
        final List<JobExecution> jobExecutions =
                this.serviceEntry.findJobExecutions(jobNameQueryParameter, queryParameter, resultSize);
        return ResponseEntity.ok(jobExecutions);
    }

    @Override
    public void afterPropertiesSet() {
        assert this.serviceEntry != null;
    }
}
