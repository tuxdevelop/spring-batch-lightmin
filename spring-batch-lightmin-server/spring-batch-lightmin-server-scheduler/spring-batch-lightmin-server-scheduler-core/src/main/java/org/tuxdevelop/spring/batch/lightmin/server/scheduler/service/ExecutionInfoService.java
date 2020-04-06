package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.domain.ExecutionInfo;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.domain.ExecutionInfoPage;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ServerSchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerConfigurationNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ExecutionInfoService {

    private final SchedulerExecutionService schedulerExecutionService;
    private final SchedulerConfigurationService schedulerConfigurationService;

    public ExecutionInfoService(final SchedulerExecutionService schedulerExecutionService,
                                final SchedulerConfigurationService schedulerConfigurationService) {
        this.schedulerExecutionService = schedulerExecutionService;
        this.schedulerConfigurationService = schedulerConfigurationService;
    }

    public ExecutionInfoPage findAll(final Integer state, final int startIndex, final int count) {

        final List<SchedulerExecution> executions;

        if (state != null) {
            executions = this.schedulerExecutionService.findByState(state, startIndex, count);
        } else {
            executions = this.schedulerExecutionService.findAll(startIndex, count);
        }

        final List<ExecutionInfo> executionInfos = executions.stream()
                .map(execution -> {
                    final ExecutionInfo info = new ExecutionInfo();
                    try {
                        info.setExecution(execution);
                        final SchedulerConfiguration
                                configuration = this.schedulerConfigurationService.findById(execution.getSchedulerConfigurationId());
                        info.setConfiguration(configuration);
                    } catch (SchedulerConfigurationNotFoundException e) {
                        log.info("could not find SchedulerConfiguration with id {} for SchedulerExecution with the id {}",
                                execution.getSchedulerConfigurationId(), execution.getId());
                        final SchedulerConfiguration configuration = new SchedulerConfiguration();
                        this.createSchedulerConfigurationForUnknowConfigurationId(configuration);
                        info.getExecution().setSchedulerConfigurationId(configuration.getId());
                        info.setConfiguration(configuration);
                    }
                    return info;
                }).collect(Collectors.toList());

        final ExecutionInfoPage executionInfoPage = new ExecutionInfoPage();
        executionInfoPage.setExecutionInfos(executionInfos);
        executionInfoPage.setStartIndex(startIndex);
        executionInfoPage.setPageSize(executionInfos.size());
        executionInfoPage.setTotalCount(this.getTotalExecutionCount(state));
        return executionInfoPage;
    }

    public Integer getTotalExecutionCount(final Integer status) {
        return this.schedulerExecutionService.getExecutionCount(status);
    }

    private void createSchedulerConfigurationForUnknowConfigurationId(final SchedulerConfiguration configuration) {
        configuration.setStatus(ServerSchedulerStatus.STOPPED);
        configuration.setApplication("UNKNOWN");
        configuration.setCronExpression("");
        configuration.setId(-1L);
        configuration.setInstanceExecutionCount(0);
        configuration.setJobName("UNKNOWN");
        configuration.setJobParameters(new HashMap<>());
        configuration.setMaxRetries(0);
        configuration.setRetryable(Boolean.FALSE);
        configuration.setJobIncrementer(JobIncrementer.NONE);
    }
}
