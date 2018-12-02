package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.server.domain.Journal;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.journal.JournalModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.JournalServiceBean;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JournalsFeService {

    private final JournalServiceBean journalServiceBean;

    public JournalsFeService(final JournalServiceBean journalServiceBean) {
        this.journalServiceBean = journalServiceBean;
    }

    public List<JournalModel> getAll() {
        final List<Journal> journals = this.journalServiceBean.findAll();
        return map(journals);
    }

    private List<JournalModel> map(final List<Journal> journals) {
        final List<JournalModel> journalModels = new ArrayList<>();
        if (journals != null && !journals.isEmpty()) {
            for (final Journal journal : journals) {
                final JournalModel journalModel = mapJournal(journal);
                journalModels.add(journalModel);
            }
        } else {
            log.debug("No journals to map");
        }
        return journalModels;
    }

    private JournalModel mapJournal(final Journal journal) {
        final JournalModel journalModel = new JournalModel();
        journalModel.setApplicationName(journal.getApplicationName());
        journalModel.setHost(journal.getHost());
        journalModel.setNewStatus(journal.getNewStatus());
        journalModel.setOldStatus(journal.getOldStatus());
        journalModel.setTimestamp(journal.getTimestamp());
        return journalModel;
    }
}
