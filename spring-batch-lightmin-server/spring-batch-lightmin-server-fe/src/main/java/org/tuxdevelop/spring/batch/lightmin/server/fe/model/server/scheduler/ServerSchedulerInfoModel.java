package org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler;

import lombok.Data;

@Data
public class ServerSchedulerInfoModel {

    private ServerSchedulerExecutionModel execution;
    private ServerSchedulerConfigurationModel config;
}
