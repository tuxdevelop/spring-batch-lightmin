package org.tuxdevelop.spring.batch.lightmin.api.controller;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.support.ServiceEntry;

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
    @RequestMapping(value = JobRestControllerAPI.JOB_EXECUTIONS_JOB_EXECUTION_ID, produces = PRODUCES, method = RequestMethod.GET)
    public ResponseEntity<JobExecution> getJobExecutionById(@PathVariable(value = "jobexecutionid") final Long jobExecutionId) {
        final JobExecution jobExecution = serviceEntry.getByJobExecutionId(jobExecutionId);
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
    @RequestMapping(value = JobRestControllerAPI.JOB_EXECUTION_PAGES_INSTANCE_ID, produces = PRODUCES, method = RequestMethod.GET)
    public ResponseEntity<JobExecutionPage> getJobExecutionsByJobInstanceId(@RequestParam("jobinstanceid") final Long jobInstanceId,
                                                                            @RequestParam(value = "startindex", defaultValue = "0") final int startIndex,
                                                                            @RequestParam(value = "pagesize", defaultValue = "10") final int pageSize) {
        final JobExecutionPage jobInstanceExecutions = serviceEntry.getJobExecutionPage(jobInstanceId, startIndex, pageSize);
        return ResponseEntity.ok(jobInstanceExecutions);
    }

    /**
     * Retrieves {@link JobExecutionPage} containing {@link JobExecution}s for a given
     * {@link org.springframework.batch.core.JobInstance} id
     *
     * @param jobInstanceId the id of the JobInstance
     * @return the JobExecutionPage
     */
    @RequestMapping(value = JobRestControllerAPI.JOB_EXECUTION_PAGES_INSTANCE_ID_ALL, produces = PRODUCES, method = RequestMethod.GET)
    public ResponseEntity<JobExecutionPage> getAllJobExecutionsByJobInstanceId(@RequestParam("jobinstanceid") final Long
                                                                                       jobInstanceId) {
        final JobExecutionPage jobInstanceExecutions = serviceEntry.getJobExecutionPage(jobInstanceId);
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
    @RequestMapping(value = JobRestControllerAPI.JOB_INSTANCES_JOB_NAME, produces = PRODUCES, method = RequestMethod.GET)
    public ResponseEntity<JobInstancePage> getJobInstancesByJobName(@RequestParam("jobname") final String jobName,
                                                                    @RequestParam(value = "startindex", defaultValue = "0") final int startIndex,
                                                                    @RequestParam(value = "pagesize", defaultValue = "10") final int pageSize) {
        final JobInstancePage jobInstancePage = serviceEntry.getJobInstancesByJobName(jobName, startIndex, pageSize);
        return ResponseEntity.ok(jobInstancePage);
    }

    /**
     * Retrieves high level {@link ApplicationJobInfo} of the Application
     *
     * @return the ApplicationJobInfo
     */
    @RequestMapping(value = JobRestControllerAPI.APPLICATION_JOB_INFO, produces = PRODUCES, method = RequestMethod.GET)
    public ResponseEntity<ApplicationJobInfo> getApplicationJobInfo() {
        final ApplicationJobInfo applicationJobInfo = serviceEntry.getApplicationJobInfo();
        return ResponseEntity.ok(applicationJobInfo);
    }

    /**
     * Retrieves high level {@link JobInfo} of a {@link org.springframework.batch.core.Job} for a given job name
     *
     * @param jobName the name of the Spring Batch Job
     * @return the JobInfo
     */
    @RequestMapping(value = JobRestControllerAPI.JOB_INFO_JOB_NAME, produces = PRODUCES, method = RequestMethod.GET)
    public ResponseEntity<JobInfo> getJobInfo(@PathVariable("jobname") final String jobName) {
        final JobInfo jobInfo = serviceEntry.getJobInfo(jobName);
        return ResponseEntity.ok(jobInfo);
    }

    /**
     * Restarts a {@link org.springframework.batch.core.JobExecution} of a given id
     *
     * @param jobExecutionId the id of the JobExecution
     * @return HTTP Status Code 200
     */
    @RequestMapping(value = JobRestControllerAPI.JOB_EXECUTIONS_RESTART, method = RequestMethod.GET)
    public ResponseEntity<Void> restartJobExecution(@PathVariable("jobexecutionid") final Long jobExecutionId) {
        serviceEntry.restartJobExecution(jobExecutionId);
        return ResponseEntity.ok().build();
    }

    /**
     * Stops a running {@link org.springframework.batch.core.JobExecution} of a given id
     *
     * @param jobExecutionId the id of the JobExecution
     * @return HTTP Status Code 200
     */
    @RequestMapping(value = JobRestControllerAPI.JOB_EXECUTIONS_STOP, method = RequestMethod.GET)
    public ResponseEntity<Void> stopJobExecution(@PathVariable("jobexecutionid") final Long jobExecutionId) {
        serviceEntry.stopJobExecution(jobExecutionId);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves a {@link StepExecution} of a {@link org.springframework.batch.core.JobExecution}
     *
     * @param jobExecutionId  the id of the {@link org.springframework.batch.core.JobExecution}
     * @param stepExecutionId the id of the {@link org.springframework.batch.core.StepExecution}
     * @return the StepExecution
     */
    @RequestMapping(value = JobRestControllerAPI.STEP_EXECUTIONS, produces = PRODUCES, method = RequestMethod.GET)
    public ResponseEntity<StepExecution> getStepExecution(@PathVariable("jobexecutionid") final Long jobExecutionId,
                                                          @PathVariable("stepexecutionid") final Long stepExecutionId) {
        final StepExecution stepExecution = serviceEntry.getStepExecution(jobExecutionId, stepExecutionId);
        return ResponseEntity.ok(stepExecution);
    }

    /**
     * Retrieves the last used {@link JobParameters} of a {@link org.springframework.batch.core.Job} run
     *
     * @param jobName the name of Spring Batch Job
     * @return the JobParamaters
     */
    @RequestMapping(value = JobRestControllerAPI.JOB_PARAMETERS, produces = PRODUCES, method = RequestMethod.GET)
    public ResponseEntity<JobParameters> getLastJobParameters(@RequestParam("jobname") final String jobName) {
        final JobParameters jobParameters = serviceEntry.getLastJobParameters(jobName);
        return ResponseEntity.ok(jobParameters);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        assert serviceEntry != null;
    }
}
