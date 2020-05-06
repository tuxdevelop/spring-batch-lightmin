package org.tuxdevelop.spring.batch.lightmin.server.cluster.repository;

import org.infinispan.Cache;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.annotation.ServerClusterLock;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.service.InfinispanClusterIdService;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.KVSchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;

import java.util.concurrent.TimeUnit;

public class InfinispanSchedulerConfigurationRepository extends KVSchedulerConfigurationRepository<Cache<Long, SchedulerConfiguration>> {

    private static final String SCHEDULER_CONFIGURATION_REPOSITORY_ID_LOCK = "scheduler_configuration_repository_id_lock";
    private static final String SCHEDULER_CONFIGURATION__REPOSITORY_ID_KEY = "scheduler_configuration_repository_id_key";

    private final InfinispanClusterIdService infinispanClusterIdService;

    public InfinispanSchedulerConfigurationRepository(final Cache<Long, SchedulerConfiguration> store,
                                                      final InfinispanClusterIdService infinispanClusterIdService) {
        super(store);
        this.infinispanClusterIdService = infinispanClusterIdService;
    }

    @Override
    @ServerClusterLock(
            id = SCHEDULER_CONFIGURATION_REPOSITORY_ID_LOCK,
            waitForLock = true,
            timeout = 5000,
            timeUnit = TimeUnit.MILLISECONDS)
    public Long getNextId() {
        return this.infinispanClusterIdService.getNextId(SCHEDULER_CONFIGURATION__REPOSITORY_ID_KEY);
    }
}
