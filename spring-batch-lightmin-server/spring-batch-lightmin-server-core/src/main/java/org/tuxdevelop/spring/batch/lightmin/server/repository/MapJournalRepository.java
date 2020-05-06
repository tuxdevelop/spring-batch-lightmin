package org.tuxdevelop.spring.batch.lightmin.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.server.domain.Journal;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class MapJournalRepository extends KVJournalRepository<Map<Long, Journal>> {

    private final AtomicLong idStore;

    public MapJournalRepository() {
        super(Collections.synchronizedMap(new LimitedLinkedHashMap<>(20)));
        this.idStore = new AtomicLong(1L);
    }

    @Override
    public synchronized Long getNextId() {
        return this.idStore.getAndIncrement();
    }
}
