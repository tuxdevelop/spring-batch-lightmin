package org.tuxdevelop.spring.batch.lightmin.server.scheduler.domain;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;

@Data
public class ExecutionInfo {
    private SchedulerExecution execution;
    private SchedulerConfiguration configuration;
}
