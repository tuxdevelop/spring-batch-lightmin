package org.tuxdevelop.spring.batch.lightmin.server.cluster.repository;

import org.infinispan.Cache;
import org.tuxdevelop.spring.batch.lightmin.server.domain.Journal;
import org.tuxdevelop.spring.batch.lightmin.server.repository.KVJournalRepository;

public class InfinispanJournalRepository extends KVJournalRepository<Cache<Long, Journal>> {

    public InfinispanJournalRepository(final Cache<Long, Journal> store) {
        super(store);
    }
}
