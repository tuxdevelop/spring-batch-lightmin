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
}
