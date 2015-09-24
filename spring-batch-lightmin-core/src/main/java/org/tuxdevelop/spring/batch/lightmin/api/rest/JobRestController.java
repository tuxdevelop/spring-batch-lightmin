package org.tuxdevelop.spring.batch.lightmin.api.rest;


import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tuxdevelop.spring.batch.lightmin.api.domain.JobInstanceExecutions;
import org.tuxdevelop.spring.batch.lightmin.api.domain.JobInstances;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;

import java.util.Collection;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@RestController
public class JobRestController extends AbstractRestController {

    @Autowired
    private JobService jobService;

    @RequestMapping(value = JobRestControllerAPI.JOB_EXECUTIONS_JOB_EXECUTION_ID, produces = PRODUCES, method = RequestMethod.GET)
    public JobExecution getJobExecutionById(@PathVariable(value = "jobExecutionId") final Long jobExecutionId) {
        return jobService.getJobExecution(jobExecutionId);
    }

    @RequestMapping(value = JobRestControllerAPI.JOB_EXECUTIONS_JOB_INSTANCES_JOB_INSTANCE_ID, produces = PRODUCES, method = RequestMethod.GET)
    public JobInstanceExecutions getJobExecutionsByJobInstanceId(@PathVariable("jobInstanceId") final Long jobInstanceId) {
        final JobInstance jobInstance = jobService.getJobInstance(jobInstanceId);
        final Collection<JobExecution> jobExecutions = jobService.getJobExecutions(jobInstance);
        final JobInstanceExecutions jobInstanceExecutions = new JobInstanceExecutions();
        jobInstanceExecutions.setJobName(jobInstance.getJobName());
        jobInstanceExecutions.setJobInstanceId(jobInstanceId);
        jobInstanceExecutions.setJobExecutions(jobExecutions);
        return jobInstanceExecutions;
    }

    @RequestMapping(value = JobRestControllerAPI.JOB_INSTANCES_JOB_NAME, produces = PRODUCES, method = RequestMethod.GET)
    public JobInstances getJobInstancesByJobName(@PathVariable("jobName") final String jobName, @RequestParam(value =
            "startIndex", defaultValue = "0") final int startIndex, @RequestParam(value = "pageSize", defaultValue = "10") final int pageSize) {
        final Collection<JobInstance> jobInstanceCollection = jobService.getJobInstances(jobName, startIndex, pageSize);
        final JobInstances jobInstances = new JobInstances();
        jobInstances.setJobName(jobName);
        jobInstances.setJobInstances(jobInstanceCollection);
        jobInstances.setPageSize(pageSize);
        jobInstances.setStartIndex(startIndex);
        return jobInstances;
    }

}
