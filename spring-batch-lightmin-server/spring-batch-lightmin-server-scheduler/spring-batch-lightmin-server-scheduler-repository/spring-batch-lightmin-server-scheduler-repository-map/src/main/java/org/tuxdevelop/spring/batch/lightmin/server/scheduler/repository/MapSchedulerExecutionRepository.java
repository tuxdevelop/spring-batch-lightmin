package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerExecutionNotFoundException;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class MapSchedulerExecutionRepository implements SchedulerExecutionRepository {

    final Map<Long, SchedulerExecution> store = new HashMap<>();
    private final AtomicLong currentId = new AtomicLong(1L);

    @Override
    public SchedulerExecution save(final SchedulerExecution schedulerExecution) {
        final Long id;
        if (schedulerExecution.getId() != null) {
            id = schedulerExecution.getId();
        } else {
            id = getNextId();
            schedulerExecution.setId(id);
        }
        this.store.put(id, schedulerExecution);
        return schedulerExecution;
    }

    @Override
    public SchedulerExecution findById(final Long id) throws SchedulerExecutionNotFoundException {
        final SchedulerExecution schedulerExecution;
        if (this.store.containsKey(id)) {
            schedulerExecution = this.store.get(id);
        } else {
            throw new SchedulerExecutionNotFoundException("Could not find a SchedulerExecution for id " + id);
        }
        return schedulerExecution;
    }

    @Override
    public void delete(final Long id) {
        final SchedulerExecution schedulerExecution;
        if (this.store.containsKey(id)) {
            schedulerExecution = this.store.get(id);
            this.store.remove(id, schedulerExecution);
        } else {
            log.warn("Could not delete SchedulerExecution with id {}, not present!", id);
        }
    }

    @Override
    public void deleteBySchedulerConfigurationId(final Long schedulerConfigurationId) {
        final List<SchedulerExecution> schedulerExecutions = new ArrayList<>();
        for (final SchedulerExecution schedulerExecution : this.store.values()) {
            if (schedulerExecution.getSchedulerConfigurationId().equals(schedulerConfigurationId)) {
                schedulerExecutions.add(schedulerExecution);
            } else {
                log.trace("No SchedulerExecution for SchedulerConfiguration id {} ", schedulerConfigurationId);
            }
        }
        for (final SchedulerExecution schedulerExecution : schedulerExecutions) {
            this.delete(schedulerExecution.getId());
        }
    }

    @Override
    public List<SchedulerExecution> findAll() {
        final List<SchedulerExecution> schedulerExecutions = new ArrayList<>();
        if (this.store.isEmpty()) {
            log.debug("No SchedulerExecutions available");
        } else {
            schedulerExecutions.addAll(this.store.values());
            this.sort(schedulerExecutions);
        }
        return schedulerExecutions;
    }

    @Override
    public List<SchedulerExecution> findAll(final int startIndex, final int pageSize) {
        final List<SchedulerExecution> schedulerExecutions = findAll();
        return subset(schedulerExecutions, startIndex, pageSize);
    }

    @Override
    public List<SchedulerExecution> findNextExecutions(final Date date) {
        final List<SchedulerExecution> schedulerExecutions = new ArrayList<>();
        for (final SchedulerExecution schedulerExecution : this.findAll()) {
            if (schedulerExecution.getNextFireTime().getTime() <= date.getTime()) {
                schedulerExecutions.add(schedulerExecution);
            } else {
                log.trace("Skipping ...");
            }
        }
        return schedulerExecutions;
    }

    @Override
    public List<SchedulerExecution> findByState(final Integer state) {
        final List<SchedulerExecution> schedulerExecutions = new ArrayList<>();
        for (final SchedulerExecution schedulerExecution : this.findAll()) {
            if (schedulerExecution.getState().equals(state)) {
                schedulerExecutions.add(schedulerExecution);
            } else {
                log.trace("Skipping ...");
            }
        }
        return schedulerExecutions;
    }

    @Override
    public List<SchedulerExecution> findByState(final Integer state, final int startIndex, final int pageSize) {
        final List<SchedulerExecution> schedulerExecutions = this.findByState(state);
        return subset(schedulerExecutions, startIndex, pageSize);
    }

    @Override
    public List<SchedulerExecution> findByStateAndDate(final Integer state, final Date date) {
        final List<SchedulerExecution> schedulerExecutions = this.findByState(state);
        final List<SchedulerExecution> result = new ArrayList<>();
        for (final SchedulerExecution schedulerExecution : schedulerExecutions) {
            if (schedulerExecution.getNextFireTime().getTime() <= date.getTime()) {
                result.add(schedulerExecution);
            } else {
                log.trace("Skipping ...");
            }
        }
        return result;
    }

    @Override
    public List<SchedulerExecution> findBySchedulerConfigurationId(final Long schedulerConfigurationId) {
        final List<SchedulerExecution> schedulerExecutions = new ArrayList<>();
        for (final SchedulerExecution schedulerExecution : this.findAll()) {
            if (schedulerExecution.getSchedulerConfigurationId().equals(schedulerConfigurationId)) {
                schedulerExecutions.add(schedulerExecution);
            } else {
                log.trace("Skipping ...");
            }
        }
        return schedulerExecutions;
    }

    @Override
    public void deleteByState(final Integer state) {
        final List<SchedulerExecution> schedulerExecutions = new ArrayList<>();
        for (final SchedulerExecution schedulerExecution : this.store.values()) {
            if (schedulerExecution.getState().equals(state)) {
                schedulerExecutions.add(schedulerExecution);
            } else {
                log.trace("No SchedulerExecution for state {} ", schedulerExecution);
            }
        }
        for (final SchedulerExecution schedulerExecution : schedulerExecutions) {
            this.delete(schedulerExecution.getId());
        }
    }

    private synchronized Long getNextId() {
        return this.currentId.getAndIncrement();
    }

    private List<SchedulerExecution> subset(final List<SchedulerExecution> schedulerExecutions,
                                            final int start,
                                            final int count) {
        final int end = count > 0 ? start + count : schedulerExecutions.size();
        final int startIndex = Math.min(start, schedulerExecutions.size());
        final int endIndex = Math.min(end, schedulerExecutions.size());
        return schedulerExecutions.subList(startIndex, endIndex);
    }

    private void sort(final List<SchedulerExecution> schedulerExecutions) {
        schedulerExecutions.sort((schedulerExecution, schedulerExecutionToCompare)
                -> Long.signum(schedulerExecution.getId() - schedulerExecutionToCompare.getId()));
    }
}
