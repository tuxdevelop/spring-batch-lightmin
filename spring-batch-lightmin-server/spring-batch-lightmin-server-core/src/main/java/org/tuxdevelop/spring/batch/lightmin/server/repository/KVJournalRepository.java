package org.tuxdevelop.spring.batch.lightmin.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.server.domain.Journal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public abstract class KVJournalRepository<T extends Map<Long, Journal>> implements JournalRepository {

    private final T store;
    private final AtomicLong idStore;

    public KVJournalRepository(final T store) {
        this.store = store;
        this.idStore = new AtomicLong(1L);
    }

    @Override
    public synchronized Journal add(final Journal journal) {
        final Long id = this.idStore.getAndIncrement();
        journal.setId(id);
        this.store.put(id, journal);
        return journal;
    }

    @Override
    public List<Journal> findAll() {
        final List<Journal> result = new ArrayList<>();
        if (!this.store.values().isEmpty()) {
            result.addAll(this.store.values());
        } else {
            log.debug("returning empty journal list");
        }
        return result;
    }

    @Override
    public void clear() {
        this.store.clear();
    }

}
