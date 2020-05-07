package org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.JobIncremeterTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.validator.ValidJobParameters;
import org.tuxdevelop.spring.batch.lightmin.validation.annotation.IsCronExpression;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class ServerSchedulerConfigurationModel {

    private Long id;
    @NotNull
    private Integer instanceExecutionCount;
    @IsCronExpression
    private String cronExpression;
    @NotBlank
    private String applicationName;
    @NotBlank
    private String jobName;
    @NotNull
    private Boolean retryable;
    private Integer maxRetries;
    private Long retryInterval;
    private JobIncremeterTypeModel incrementerRead;
    @NotNull
    private String incrementer;
    private ServerSchedulerConfigurationStatusModel statusRead;
    private String status;
    @ValidJobParameters
    private String parameters;
    private Map<String, Object> parametersRead;

}
