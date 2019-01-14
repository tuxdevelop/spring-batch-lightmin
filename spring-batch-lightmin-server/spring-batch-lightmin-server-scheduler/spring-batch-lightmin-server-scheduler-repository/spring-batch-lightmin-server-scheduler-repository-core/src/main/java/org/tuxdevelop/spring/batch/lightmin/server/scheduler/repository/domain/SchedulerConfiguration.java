package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;

import java.util.Map;

@Data
public class SchedulerConfiguration {

    private Long id;
    private Integer instanceExecutionCount;
    private String cronExpression;
    private String application;
    private String jobName;
    private JobIncrementer jobIncrementer;
    private Map<String, Object> jobParameters;
}
