package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration.ServerSchedulerCoreConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;

import java.util.Date;
import java.util.List;

public abstract class AbstractExecutionPollerService {

    private final ExecutionRunnerService executionRunnerService;
    private final SchedulerExecutionService schedulerExecutionService;
    private final ServerSchedulerCoreConfigurationProperties properties;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    protected AbstractExecutionPollerService(final ExecutionRunnerService executionRunnerService,
                                             final SchedulerExecutionService schedulerExecutionService,
                                             final ServerSchedulerCoreConfigurationProperties properties) {
        this.executionRunnerService = executionRunnerService;
        this.schedulerExecutionService = schedulerExecutionService;
        this.properties = properties;
        this.threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        this.threadPoolTaskExecutor.setCorePoolSize(properties.getThreadPoolCoreSize());
        this.threadPoolTaskExecutor.setMaxPoolSize(properties.getThreadPoolSize());
        this.threadPoolTaskExecutor.afterPropertiesSet();
    }

    public void triggerScheduledExecutions() {
        final List<SchedulerExecution> executions =
                this.schedulerExecutionService.findScheduledExecutions(ExecutionStatus.NEW, new Date());
        runExecutions(executions);
    }

    public void triggerRetryExecutions() {
        final List<SchedulerExecution> executions =
                this.schedulerExecutionService.findScheduledExecutions(ExecutionStatus.FAILED);
        runExecutions(executions);
    }

    private void runExecutions(final List<SchedulerExecution> executions) {
        executions.parallelStream()
                .forEach(
                        schedulerExecution -> {
                            final ExecutionRunner runner =
                                    new ExecutionRunner(
                                            schedulerExecution,
                                            this.executionRunnerService);
                            this.threadPoolTaskExecutor.execute(runner);
                        }
                );
    }


}
