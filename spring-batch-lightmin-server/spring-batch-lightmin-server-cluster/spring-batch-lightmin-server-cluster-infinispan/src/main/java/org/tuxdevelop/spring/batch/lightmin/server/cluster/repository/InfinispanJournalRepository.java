package org.tuxdevelop.spring.batch.lightmin.server.cluster.repository;

import org.infinispan.Cache;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.annotation.ServerClusterLock;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.service.InfinispanClusterIdService;
import org.tuxdevelop.spring.batch.lightmin.server.domain.Journal;
import org.tuxdevelop.spring.batch.lightmin.server.repository.KVJournalRepository;

import java.util.concurrent.TimeUnit;

public class InfinispanJournalRepository extends KVJournalRepository<Cache<Long, Journal>> {

    private static final String JOURNAL_REPOSITORY_ID_LOCK = "journal_repository_id_lock";
    private static final String JOURNAL_REPOSITORY_ID_KEY = "journal_repository_id_key";

    private final InfinispanClusterIdService infinispanClusterIdService;

    public InfinispanJournalRepository(final Cache<Long, Journal> store,
                                       final InfinispanClusterIdService infinispanClusterIdService) {
        super(store);
        this.infinispanClusterIdService = infinispanClusterIdService;
    }

    @Override
    @ServerClusterLock(
            id = JOURNAL_REPOSITORY_ID_LOCK,
            waitForLock = true,
            timeout = 5000,
            timeUnit = TimeUnit.MILLISECONDS)
    public synchronized Long getNextId() {
        return this.infinispanClusterIdService.getNextId(JOURNAL_REPOSITORY_ID_KEY);
    }
    
}
