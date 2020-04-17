package org.tuxdevelop.spring.batch.lightmin.server.repository;

import org.tuxdevelop.spring.batch.lightmin.server.domain.Journal;

import java.util.List;

public interface JournalRepository {


    Journal add(Journal journal);

    List<Journal> findAll();

    void clear();
}
