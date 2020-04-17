package org.tuxdevelop.spring.batch.lightmin.server.repository;

public class MapJournalRepositoryTest extends JournalRepositoryTest {

    private final JournalRepository journalRepository = new MapJournalRepository();

    @Override
    protected JournalRepository getJournalRepository() {
        return this.journalRepository;
    }
}
