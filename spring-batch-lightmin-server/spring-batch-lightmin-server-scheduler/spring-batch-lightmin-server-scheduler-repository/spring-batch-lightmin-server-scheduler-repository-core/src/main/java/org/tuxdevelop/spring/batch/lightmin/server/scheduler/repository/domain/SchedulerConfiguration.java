package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain;

import lombok.Data;

import java.util.Map;

@Data
public class SchedulerConfiguration {

    private Long id;
    private Integer instanceExecutionCount;
    private String cronExpression;
    private String application;
    private String jobName;
    private Map<String, Object> jobParameters;
}
