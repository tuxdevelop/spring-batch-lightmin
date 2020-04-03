package org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.JobIncremeterTypeModel;

import java.util.Map;

@Data
public class ServerSchedulerConfigurationModel {

    private Long id;
    private Integer instanceExecutionCount;
    private String cronExpression;
    private String applicationName;
    private String jobName;
    private Boolean retryable;
    private Integer maxRetries;
    private JobIncremeterTypeModel incrementerRead;
    private String incrementer;
    private ServerSchedulerConfigurationStatusModel statusRead;
    private String status;
    private String parameters;
    private Map<String, Object> parametersRead;

}
