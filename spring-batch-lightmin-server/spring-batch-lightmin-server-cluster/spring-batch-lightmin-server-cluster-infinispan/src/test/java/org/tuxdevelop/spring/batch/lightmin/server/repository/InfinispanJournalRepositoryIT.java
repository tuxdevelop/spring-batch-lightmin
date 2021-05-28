package org.tuxdevelop.spring.batch.lightmin.server.repository;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.test.configuration.InfinispanITConfiguration;
import org.txudevelop.spring.batch.lightmin.server.repository.JournalRepositoryTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {InfinispanITConfiguration.class})
public class InfinispanJournalRepositoryIT extends JournalRepositoryTest {

    @Autowired
    private JournalRepository journalRepository;

    @Override
    protected JournalRepository getJournalRepository() {
        return this.journalRepository;
    }
}
