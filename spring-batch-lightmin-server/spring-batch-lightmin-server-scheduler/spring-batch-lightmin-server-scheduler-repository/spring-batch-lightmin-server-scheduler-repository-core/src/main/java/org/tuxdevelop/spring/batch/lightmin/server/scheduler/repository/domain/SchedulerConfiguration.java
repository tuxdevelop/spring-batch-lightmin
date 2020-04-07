package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;

import java.util.Map;

@Data
public class SchedulerConfiguration {

    //TODO: add validations
    private Long id;
    private Integer instanceExecutionCount;
    private String cronExpression;
    private String application;
    private String jobName;
    private Boolean retryable;
    private Integer maxRetries;
    private JobIncrementer jobIncrementer;
    private ServerSchedulerStatus status;
    private Map<String, Object> jobParameters;


    public void validate() {
        if (this.instanceExecutionCount == null || this.instanceExecutionCount < 1) {
            throw new SchedulerValidationException("instanceExecutionCount must not be null or lower than 1");
        }
        if (this.cronExpression == null) {
            throw new SchedulerValidationException("cronExpression must not be null");
        }
        if (this.application == null) {
            throw new SchedulerValidationException("application must not be null");
        }
        if (this.jobName == null) {
            throw new SchedulerValidationException("jobName must not be null");
        }
        if (this.retryable == null) {
            throw new SchedulerValidationException("retriable must not be null");
        }
        if ((this.maxRetries == null || this.maxRetries < 0) && Boolean.TRUE.equals(this.retryable)) {
            throw new SchedulerValidationException("maxRetries must not be null or lower than 1 if retryable is true");
        }
        if (this.jobIncrementer == null) {
            throw new SchedulerValidationException("jobIncrementer must not be null");
        }
        if (this.status == null) {
            throw new SchedulerValidationException("status must not be null");

        }
    }
}
