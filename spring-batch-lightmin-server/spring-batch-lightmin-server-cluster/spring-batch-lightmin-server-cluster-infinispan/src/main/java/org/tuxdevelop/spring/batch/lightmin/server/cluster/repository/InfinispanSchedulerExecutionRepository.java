package org.tuxdevelop.spring.batch.lightmin.server.cluster.repository;

import org.infinispan.Cache;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.annotation.ServerClusterLock;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.service.InfinispanClusterIdService;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.KVSchedulerExecutionRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;

import java.util.concurrent.TimeUnit;

public class InfinispanSchedulerExecutionRepository extends KVSchedulerExecutionRepository<Cache<Long, SchedulerExecution>> {

    private static final String SCHEDULER_EXECUTION_REPOSITORY_ID_LOCK = "scheduler_execution_repository_id_lock";
    private static final String SCHEDULER_EXECUTION_REPOSITORY_ID_KEY = "scheduler_execution_repository_id_key";

    private final InfinispanClusterIdService infinispanClusterIdService;

    public InfinispanSchedulerExecutionRepository(final Cache<Long, SchedulerExecution> store,
                                                  final InfinispanClusterIdService infinispanClusterIdService) {
        super(store);
        this.infinispanClusterIdService = infinispanClusterIdService;
    }

    @Override
    @ServerClusterLock(
            id = SCHEDULER_EXECUTION_REPOSITORY_ID_LOCK,
            waitForLock = true,
            timeout = 5000,
            timeUnit = TimeUnit.MILLISECONDS)
    public Long getNextId() {
        return this.infinispanClusterIdService.getNextId(SCHEDULER_EXECUTION_REPOSITORY_ID_KEY);
    }
}
