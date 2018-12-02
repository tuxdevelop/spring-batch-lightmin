package org.tuxdevelop.spring.batch.lightmin.event;


import org.springframework.batch.core.JobExecution;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
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
}
