package org.tuxdevelop.spring.batch.lightmin.model;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.SchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.TaskExecutorType;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Data
public class JobConfigurationAddModel {

    private Long jobConfigurationId;
    private String jobName;
    private JobSchedulerType jobSchedulerType;
    private String cronExpression;
    private Long fixedDelay;
    private Long initialDelay;
    private TaskExecutorType taskExecutorType;
    private String jobParameters;
    private SchedulerStatus schedulerStatus;
    private JobIncrementer jobIncrementer;
    private String applicationId;
}
