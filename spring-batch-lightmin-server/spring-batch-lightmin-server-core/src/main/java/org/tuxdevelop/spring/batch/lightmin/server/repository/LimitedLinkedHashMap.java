package org.tuxdevelop.spring.batch.lightmin.server.repository;

import java.util.LinkedHashMap;
import java.util.Map;

public class LimitedLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

    private final Integer limit;

    LimitedLinkedHashMap(final Integer limit) {
        this.limit = limit;
    }

    @Override
    public boolean removeEldestEntry(final Map.Entry eldest) {
        return this.limit < this.size();
    }

}