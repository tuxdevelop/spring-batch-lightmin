package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class MapSchedulerExecutionRepository extends KVSchedulerExecutionRepository<Map<Long, SchedulerExecution>> {

    private final AtomicLong currentId = new AtomicLong(1L);

    public MapSchedulerExecutionRepository() {
        super(new HashMap<>());
    }

    @Override
    public synchronized Long getNextId() {
        return this.currentId.getAndIncrement();
    }
}
