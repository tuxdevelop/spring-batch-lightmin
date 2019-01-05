package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain;

import lombok.Data;

import java.util.Date;

@Data
public class SchedulerExecution {

    private Long id;
    private Long schedulerConfigurationId;
    private Date nextSchedule;
    private Integer executionCount;
    private Integer state;

}
