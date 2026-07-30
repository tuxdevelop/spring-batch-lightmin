package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.validation.annotation.IsCronExpression;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

@Data
public class SchedulerConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    @NotNull
    private Integer instanceExecutionCount;
    @IsCronExpression
    private String cronExpression;
    @NotBlank
    private String application;
    @NotBlank
    private String jobName;
    private Boolean retryable;
    private Integer maxRetries;
    private Long retryInterval;
    @NotNull
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
            throw new SchedulerValidationException("retryable must not be null");
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
