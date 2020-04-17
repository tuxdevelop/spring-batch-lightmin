package org.tuxdevelop.spring.batch.lightmin.server.cluster.repository;

import org.infinispan.Cache;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.repository.KVJobExecutionEventRepository;

public class InfinispanJobExecutionEventRepository extends KVJobExecutionEventRepository<Cache<Long, JobExecutionEventInfo>> {

    public InfinispanJobExecutionEventRepository(final Cache<Long, JobExecutionEventInfo> store) {
        super(store);
    }
}
