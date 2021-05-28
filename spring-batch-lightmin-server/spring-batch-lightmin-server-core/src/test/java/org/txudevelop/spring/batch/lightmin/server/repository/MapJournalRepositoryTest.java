package org.txudevelop.spring.batch.lightmin.server.repository;

import org.tuxdevelop.spring.batch.lightmin.server.repository.JournalRepository;
import org.tuxdevelop.spring.batch.lightmin.server.repository.MapJournalRepository;

public class MapJournalRepositoryTest extends JournalRepositoryTest {

    private final JournalRepository journalRepository = new MapJournalRepository();

    @Override
    protected JournalRepository getJournalRepository() {
        return this.journalRepository;
    }
}
