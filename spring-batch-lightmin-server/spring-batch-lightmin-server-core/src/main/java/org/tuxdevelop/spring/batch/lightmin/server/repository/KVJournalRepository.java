package org.tuxdevelop.spring.batch.lightmin.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.server.domain.Journal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class KVJournalRepository<T extends Map<Long, Journal>> implements JournalRepository {

    private final T store;
    
    public KVJournalRepository(final T store) {
        this.store = store;
    }

    @Override
    public synchronized Journal add(final Journal journal) {
        final Long id = this.getNextId();
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

    public abstract Long getNextId();

}
