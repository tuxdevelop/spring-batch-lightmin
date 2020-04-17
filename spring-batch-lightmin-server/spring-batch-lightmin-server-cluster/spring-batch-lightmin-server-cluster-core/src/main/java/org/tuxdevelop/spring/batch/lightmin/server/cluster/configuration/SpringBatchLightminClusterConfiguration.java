package org.tuxdevelop.spring.batch.lightmin.server.cluster.configuration;

import org.tuxdevelop.spring.batch.lightmin.server.cluster.lock.LightminServerLockManager;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JobExecutionEventRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JournalRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.service.ExecutionCleanUpService;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.service.ExecutionPollerService;

public interface SpringBatchLightminClusterConfiguration {

    LightminApplicationRepository lightminApplicationRepository();

    JournalRepository journalRepository();

    JobExecutionEventRepository jobExecutionEventRepository();

    ExecutionPollerService executionPollerService();

    ExecutionCleanUpService executionCleanUpService();

    LightminServerLockManager lightminServerLockManager();

}
