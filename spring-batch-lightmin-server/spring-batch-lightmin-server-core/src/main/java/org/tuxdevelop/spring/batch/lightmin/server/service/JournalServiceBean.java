package org.tuxdevelop.spring.batch.lightmin.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.domain.Journal;
import org.tuxdevelop.spring.batch.lightmin.server.event.LightminClientApplicationChangedEvent;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JournalRepository;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Slf4j
public class JournalServiceBean {

    private final JournalRepository journalRepository;

    public JournalServiceBean(final JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @EventListener(LightminClientApplicationChangedEvent.class)
    public void addJournal(final LightminClientApplicationChangedEvent event) {
        final Journal journal = new Journal();
        final LightminClientApplication lightminClientApplication = (LightminClientApplication) event.getSource();
        journal.setApplicationName(lightminClientApplication.getName());
        journal.setHost(lightminClientApplication.getServiceUrl());
        journal.setTimestamp(new Date(event.getEventDateInMillis()));
        journal.setOldStatus(event.getOldStatus());
        journal.setNewStatus(event.getNewStatus());
        log.info("Journal change event: {}", journal);
        this.journalRepository.add(journal);
    }

    public List<Journal> findAll() {
        final List<Journal> result = this.journalRepository.findAll();
        this.sort(result);
        return result;
    }

    private void sort(final List<Journal> journals) {
        if (journals != null && !journals.isEmpty()) {
            journals.sort(Comparator.comparing(Journal::getId));
        } else {
            log.debug("Nothing to sort");
        }
    }
}
