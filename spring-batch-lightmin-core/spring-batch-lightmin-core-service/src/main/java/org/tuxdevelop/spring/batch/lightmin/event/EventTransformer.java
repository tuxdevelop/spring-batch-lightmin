package org.tuxdevelop.spring.batch.lightmin.event;


import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.api.BatchToResourceMapper;

public final class EventTransformer {

    private EventTransformer() {
    }

    public static JobExecutionEventInfo transformToJobExecutionEventInfo(final JobExecution jobExecution, final String applicationName) {
        final JobExecutionEventInfo jobExecutionEventInfo = new JobExecutionEventInfo();
        jobExecutionEventInfo.setApplicationName(applicationName);
        jobExecutionEventInfo.setExitStatus(BatchToResourceMapper.map(jobExecution.getExitStatus()));
        jobExecutionEventInfo.setStartDate(jobExecution.getStartTime());
        jobExecutionEventInfo.setEndDate(jobExecution.getEndTime());
        jobExecutionEventInfo.setJobExecutionId(jobExecution.getId());
        jobExecutionEventInfo.setJobName(jobExecution.getJobInstance().getJobName());
        return jobExecutionEventInfo;
    }

    public static StepExecutionEventInfo transformToStepExecutionEventInfo(final StepExecution stepExecution, final String applicationName) {
        final StepExecutionEventInfo stepExecutionEventInfo = new StepExecutionEventInfo();
        stepExecutionEventInfo.setApplicationName(applicationName);

        stepExecutionEventInfo.setJobName(stepExecution.getJobExecution().getJobInstance().getJobName());
        stepExecutionEventInfo.setStepName(stepExecution.getStepName());
        stepExecutionEventInfo.setExitStatus(BatchToResourceMapper.map(stepExecution.getExitStatus()));

        stepExecutionEventInfo.setReadCount(stepExecution.getReadCount());
        stepExecutionEventInfo.setWriteCount(stepExecution.getWriteCount());
        stepExecutionEventInfo.setCommitCount(stepExecution.getCommitCount());
        stepExecutionEventInfo.setRollbackCount(stepExecution.getRollbackCount());
        stepExecutionEventInfo.setReadSkipCount(stepExecution.getReadSkipCount());
        stepExecutionEventInfo.setProcessSkipCount(stepExecution.getProcessSkipCount());
        stepExecutionEventInfo.setWriteSkipCount(stepExecution.getWriteSkipCount());
        return stepExecutionEventInfo;
    }
}
