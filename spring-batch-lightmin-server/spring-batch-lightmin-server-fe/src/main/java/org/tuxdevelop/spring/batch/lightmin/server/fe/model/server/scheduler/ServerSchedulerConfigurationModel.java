package org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.JobIncremeterTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.validator.ValidJobParameters;
import org.tuxdevelop.spring.batch.lightmin.validation.annotation.IsCronExpression;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
public class ServerSchedulerConfigurationModel implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @AssertTrue(message = "{org.tuxdevelop.spring.batch.lightmin.validation.javax.validator.isMaxRetrySet.message}")
    public boolean isMaxRetrySet() {
        boolean valid = true;
        if (retryable.booleanValue()) {
            valid = maxRetries != null;
        }
        return valid;
    }

    @AssertTrue(message = "{org.tuxdevelop.spring.batch.lightmin.validation.javax.validator.isRetryIntervalSet.message}")
    public boolean isRetryIntervalSet() {
        boolean valid = true;
        if (retryable.booleanValue()) {
            valid = retryInterval != null;
        }
        return valid;
    }
}
