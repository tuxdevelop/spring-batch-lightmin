package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.validation.annotation.IsCronExpression;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Data
public class JobSchedulerConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotNull
    private JobSchedulerType jobSchedulerType;
    @IsCronExpression
    private String cronExpression;
    @Min(0)
    private Long initialDelay;
    @Min(0)
    private Long fixedDelay;
    @NotNull
    private TaskExecutorType taskExecutorType;
    @NotNull
    private SchedulerStatus schedulerStatus;
}
