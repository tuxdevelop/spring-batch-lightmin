package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration.ServerSchedulerCoreConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ExecutionStatus;

import java.time.Duration;
import java.util.Date;

@Slf4j
public abstract class AbstractExecutionCleanUpService implements ExecutionCleanUpService {

    private final SchedulerExecutionRepository schedulerExecutionRepository;
    private final ServerSchedulerCoreConfigurationProperties properties;

    protected AbstractExecutionCleanUpService(final SchedulerExecutionRepository schedulerExecutionRepository,
                                              final ServerSchedulerCoreConfigurationProperties properties) {
        this.schedulerExecutionRepository = schedulerExecutionRepository;
        this.properties = properties;
    }

    public void cleanRepository() {
        //clean up failed
        final ServerSchedulerCoreConfigurationProperties.RepositoryProperties repoProperties = this.properties.getRepository();
        if (repoProperties.getDeleteFailed()) {
            final Duration failedDuration = repoProperties.getKeepFailed();
            log.debug("Deleting FAILED executions oder than {} millis", failedDuration.toMillis());
            final Date date = new Date(System.currentTimeMillis() - failedDuration.toMillis());
            this.schedulerExecutionRepository.findByStateAndDate(ExecutionStatus.FAILED, date).forEach(
                    schedulerExecution -> {
                        log.debug("Deleting SchedulerExecution with the id {}", schedulerExecution.getId());
                        this.schedulerExecutionRepository.delete(schedulerExecution.getId());
                    }
            );
        } else {
            log.trace("Deletion of failed executions is disabled, nothing todo. ");
        }
        if (repoProperties.getDeleteFinished()) {
            final Duration finishedDuration = repoProperties.getKeepFinished();
            log.debug("Deleting FINISHED executions oder than {} millis", finishedDuration.toMillis());
            final Date date = new Date(System.currentTimeMillis() - finishedDuration.toMillis());
            this.schedulerExecutionRepository.findByStateAndDate(ExecutionStatus.FINISHED, date).forEach(
                    schedulerExecution -> {
                        log.debug("Deleting SchedulerExecution with the id {}", schedulerExecution.getId());
                        this.schedulerExecutionRepository.delete(schedulerExecution.getId());
                    }
            );
        } else {
            log.trace("Deletion of finished executions is disabled, nothing todo. ");
        }
        if (repoProperties.getDeleteLost()) {
            final Duration lostDuration = repoProperties.getKeepLost();
            log.debug("Deleting LOST executions oder than {} millis", lostDuration.toMillis());
            final Date date = new Date(System.currentTimeMillis() - lostDuration.toMillis());
            this.schedulerExecutionRepository.findByStateAndDate(ExecutionStatus.LOST, date).forEach(
                    schedulerExecution -> {
                        log.debug("Deleting SchedulerExecution with the id {}", schedulerExecution.getId());
                        this.schedulerExecutionRepository.delete(schedulerExecution.getId());
                    }
            );
        } else {
            log.trace("Deletion of failed executions is disabled, nothing todo. ");
        }
    }

}
