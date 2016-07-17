package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Data
public class JobSchedulerConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    private JobSchedulerType jobSchedulerType;
    private String cronExpression;
    private Long initialDelay;
    private Long fixedDelay;
    private TaskExecutorType taskExecutorType;
    private SchedulerStatus schedulerStatus;
}
