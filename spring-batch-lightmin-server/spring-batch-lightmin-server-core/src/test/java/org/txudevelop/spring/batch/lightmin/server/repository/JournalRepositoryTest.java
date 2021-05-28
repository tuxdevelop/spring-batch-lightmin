package org.txudevelop.spring.batch.lightmin.server.repository;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.server.domain.Journal;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JournalRepository;

import java.util.List;

public abstract class JournalRepositoryTest {


    @Test
    public void testAdd() {
        final Journal journal = ServerDomainHelper.createJournal();
        final Journal saved = this.getJournalRepository().add(journal);
        final List<Journal> all = this.getJournalRepository().findAll();
        BDDAssertions.then(all).hasSize(1);
        BDDAssertions.then(all).contains(saved);
    }

    @Test
    public void testLimit() {
        final int limit = 20;
        final int count = 2 * limit;
        for (int i = 0; i < count; i++) {
            final Journal journal = ServerDomainHelper.createJournal();
            this.getJournalRepository().add(journal);
        }
        final List<Journal> all = this.getJournalRepository().findAll();
        BDDAssertions.then(all).hasSize(limit);
    }

    public void init() {
        this.getJournalRepository().clear();
    }

    protected abstract JournalRepository getJournalRepository();

}

