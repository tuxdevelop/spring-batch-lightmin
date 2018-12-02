package org.tuxdevelop.spring.batch.lightmin.service;


import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;

import java.util.List;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public class EmbeddedJobServerService implements JobServerService {

    private final ServiceEntry serviceEntry;

    public EmbeddedJobServerService(final ServiceEntry serviceEntry) {
        this.serviceEntry = serviceEntry;
    }

    @Override
    public JobExecution getJobExecution(final Long jobExecutionId, final LightminClientApplication lightminClientApplication) {
        return this.serviceEntry.getByJobExecutionId(jobExecutionId);
    }

    @Override
    public JobInstancePage getJobInstances(final String jobName, final Integer startIndex, final Integer pageSize, final LightminClientApplication lightminClientApplication) {
        final Integer startIndexParam = startIndex != null ? startIndex : 0;
        final Integer pageSizeParam = pageSize != null ? pageSize : 10;
        return this.serviceEntry.getJobInstancesByJobName(jobName, startIndexParam, pageSizeParam);
    }

    @Override
    public JobInfo getJobInfo(final String jobName, final LightminClientApplication lightminClientApplication) {
        return this.serviceEntry.getJobInfo(jobName);
    }

    @Override
    public JobExecutionPage getJobExecutionPage(final Long jobInstanceId,
                                                final Integer startIndex,
                                                final Integer pageSize,
                                                final LightminClientApplication lightminClientApplication) {
        return this.serviceEntry.getJobExecutionPage(jobInstanceId, startIndex, pageSize);
    }

    @Override
    public JobExecutionPage getJobExecutionPage(final Long jobInstanceId,
                                                final LightminClientApplication lightminClientApplication) {
        return this.serviceEntry.getJobExecutionPage(jobInstanceId);
    }

    @Override
    public void restartJobExecution(final Long jobExecutionId, final LightminClientApplication lightminClientApplication) {
        this.serviceEntry.restartJobExecution(jobExecutionId);
    }

    @Override
    public void stopJobExecution(final Long jobExecutionId, final LightminClientApplication lightminClientApplication) {
        this.serviceEntry.stopJobExecution(jobExecutionId);
    }

    @Override
    public StepExecution getStepExecution(final Long jobExecutionId, final Long stepExecutionId, final LightminClientApplication lightminClientApplication) {
        return this.serviceEntry.getStepExecution(jobExecutionId, stepExecutionId);
    }

    @Override
    public void launchJob(final JobLaunch jobLaunch, final LightminClientApplication lightminClientApplication) {
        this.serviceEntry.launchJob(jobLaunch);
    }

    @Override
    public JobParameters getLastJobParameters(final String jobName, final LightminClientApplication lightminClientApplication) {
        return this.serviceEntry.getLastJobParameters(jobName);
    }

    @Override
    public List<JobExecution> findJobExecutions(final String jobName, final LightminClientApplication lightminClientApplication, final Map<String, Object> queryParameter, final Integer resultSize) {
        return this.serviceEntry.findJobExecutions(jobName, queryParameter, resultSize);
    }
}
