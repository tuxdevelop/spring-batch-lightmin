package org.tuxdevelop.spring.batch.lightmin.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.server.domain.Journal;

import java.util.Collections;
import java.util.Map;

@Slf4j
public class MapJournalRepository extends KVJournalRepository<Map<Long, Journal>> {

    public MapJournalRepository() {
        super(Collections.synchronizedMap(new LimitedLinkedHashMap<>(20)));
    }
}
