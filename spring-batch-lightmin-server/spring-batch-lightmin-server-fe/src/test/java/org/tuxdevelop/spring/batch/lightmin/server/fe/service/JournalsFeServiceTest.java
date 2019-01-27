package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.tuxdevelop.spring.batch.lightmin.server.domain.Journal;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.journal.JournalModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.JournalServiceBean;

import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JournalsFeServiceTest {

    private JournalsFeService journalsFeService;
    @Mock
    private JournalServiceBean journalServiceBean;

    @Test
    public void testGetAll() {
        final int count = 10;
        final List<Journal> journals = ServiceTestHelper.createJournals(count);

        when(this.journalServiceBean.findAll()).thenReturn(journals);

        final List<JournalModel> result = this.journalsFeService.getAll();

        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.size()).isEqualTo(count);
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.journalsFeService = new JournalsFeService(this.journalServiceBean);
    }

}
