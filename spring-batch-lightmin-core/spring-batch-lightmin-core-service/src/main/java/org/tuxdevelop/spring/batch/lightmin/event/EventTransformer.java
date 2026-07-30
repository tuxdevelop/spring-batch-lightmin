package org.tuxdevelop.spring.batch.lightmin.event;


import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.step.StepExecution;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.api.BatchToResourceMapper;

import java.time.ZoneId;
import java.util.Date;

public final class EventTransformer {

    private EventTransformer() {
    }

    public static JobExecutionEventInfo transformToJobExecutionEventInfo(final JobExecution jobExecution, final String applicationName) {
        final JobExecutionEventInfo jobExecutionEventInfo = new JobExecutionEventInfo();
        jobExecutionEventInfo.setApplicationName(applicationName);
        jobExecutionEventInfo.setExitStatus(BatchToResourceMapper.map(jobExecution.getExitStatus()));
        jobExecutionEventInfo.setStartDate(toDate(jobExecution.getStartTime()));
        jobExecutionEventInfo.setEndDate(toDate(jobExecution.getEndTime()));
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

        stepExecutionEventInfo.setReadCount(Math.toIntExact(stepExecution.getReadCount()));
        stepExecutionEventInfo.setWriteCount(Math.toIntExact(stepExecution.getWriteCount()));
        stepExecutionEventInfo.setCommitCount(Math.toIntExact(stepExecution.getCommitCount()));
        stepExecutionEventInfo.setRollbackCount(Math.toIntExact(stepExecution.getRollbackCount()));
        stepExecutionEventInfo.setReadSkipCount(Math.toIntExact(stepExecution.getReadSkipCount()));
        stepExecutionEventInfo.setProcessSkipCount(Math.toIntExact(stepExecution.getProcessSkipCount()));
        stepExecutionEventInfo.setWriteSkipCount(Math.toIntExact(stepExecution.getWriteSkipCount()));
        return stepExecutionEventInfo;
    }

    private static Date toDate(final java.time.LocalDateTime dateTime) {
        return dateTime == null ? null : Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
