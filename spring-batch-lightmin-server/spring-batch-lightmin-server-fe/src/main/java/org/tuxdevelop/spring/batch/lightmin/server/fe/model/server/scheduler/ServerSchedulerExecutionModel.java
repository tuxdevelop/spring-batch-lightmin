package org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler;

import lombok.Data;

import java.util.Date;

@Data
public class ServerSchedulerExecutionModel {

    private Long id;
    private Long schedulerConfigurationId;
    private Date nextFireTime;
    private Integer executionCount;
    private ServerSchedulerExecutionStatusModel statusRead;
    private Integer status;
    private Date lastUpdate;
}
