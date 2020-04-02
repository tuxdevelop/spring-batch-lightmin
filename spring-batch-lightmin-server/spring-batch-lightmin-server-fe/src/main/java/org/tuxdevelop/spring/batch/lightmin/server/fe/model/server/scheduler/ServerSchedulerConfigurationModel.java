package org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.JobIncremeterTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ServerSchedulerStatus;

import java.util.Map;

@Data
public class ServerSchedulerConfigurationModel {

    private Long id;
    private Integer instanceExecutionCount;
    private String cronExpression;
    private String application;
    private String jobName;
    private Boolean retriable;
    private Integer maxRetries;
    private JobIncremeterTypeModel incrementerRead;
    private String incrementer;
    private ServerSchedulerStatus status;
    private Map<String, Object> jobParameters;

}
