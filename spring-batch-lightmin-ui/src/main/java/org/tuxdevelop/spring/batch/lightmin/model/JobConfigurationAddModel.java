package org.tuxdevelop.spring.batch.lightmin.model;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.*;

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
    private String jobParameters;
    private SchedulerStatus schedulerStatus;
    private JobListenerType jobListenerType;
    private ListenerStatus listenerStatus;
    private String sourceFolder;
    private String filePattern;
    private Long pollerPeriod;
    private JobIncrementer jobIncrementer;
    private TaskExecutorType taskExecutorType;
    private String applicationId;
}
