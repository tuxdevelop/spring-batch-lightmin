package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain;

import lombok.Data;

import java.util.Date;

@Data
public class SchedulerExecution {

    private Long id;
    private Long schedulerConfigurationId;
    private Date nextFireTime;
    private Integer executionCount;
    private Integer state;

    public void incrementExecutionCount() {
        if (this.executionCount == null) {
            this.executionCount = 1;
        } else {
            this.executionCount++;
        }
    }

    public void validate() {
        if (this.schedulerConfigurationId == null) {
            throw new SchedulerValidationException("schedulerConfigurationId must not be null");
        }
        if (this.nextFireTime == null) {
            throw new SchedulerValidationException("nextFireTime must not be null");
        }
        if (this.executionCount == null || this.executionCount < 0) {
            throw new SchedulerValidationException("executionCount must not be null or negative");
        }
        if (this.state == null) {
            throw new SchedulerValidationException("state must not be null");
        }
    }
}
