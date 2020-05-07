package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerValidationException;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerExecutionNotFoundException;

import java.util.*;

@Slf4j
public abstract class KVSchedulerExecutionRepository<T extends Map<Long, SchedulerExecution>> implements SchedulerExecutionRepository {

    private final T store;

    protected KVSchedulerExecutionRepository(final T store) {
        this.store = store;
    }

    @Override
    public synchronized SchedulerExecution save(final SchedulerExecution schedulerExecution) {
        final Long id;
        if (schedulerExecution != null) {
            if (schedulerExecution.getId() != null) {
                id = schedulerExecution.getId();
            } else {
                id = this.getNextId();
                schedulerExecution.setId(id);
            }
            this.store.put(id, this.copy(schedulerExecution));
            return schedulerExecution;
        } else {
            throw new SchedulerValidationException("schedulerExecution must not be null");
        }

    }

    @Override
    public SchedulerExecution findById(final Long id) throws SchedulerExecutionNotFoundException {
        final SchedulerExecution schedulerExecution;
        if (this.store.containsKey(id)) {
            schedulerExecution = this.copy(this.store.get(id));
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
            schedulerExecutions.addAll(this.copy(this.store.values()));
            this.sort(schedulerExecutions);
        }
        return schedulerExecutions;
    }

    @Override
    public List<SchedulerExecution> findAll(final int startIndex, final int pageSize) {
        final List<SchedulerExecution> schedulerExecutions = this.findAll();
        return this.subset(schedulerExecutions, startIndex, pageSize);
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
        return this.subset(schedulerExecutions, startIndex, pageSize);
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
    public Integer getExecutionCount(final Integer state) {
        final int result;
        if (state != null) {
            result = this.findByState(state).size();
        } else {
            result = this.store.size();
        }
        return result;
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

    @Override
    public void deleteByConfigurationAndState(final Long configurationId, final Integer state) {
        final List<SchedulerExecution> executions = this.findBySchedulerConfigurationId(configurationId);
        executions.stream()
                .filter(
                        execution -> execution.getState().equals(state)
                ).forEach(
                execution -> {
                    this.delete(execution.getId());
                }
        );
    }

    public abstract Long getNextId();

    private List<SchedulerExecution> subset(final List<SchedulerExecution> schedulerExecutions,
                                            final int start,
                                            final int count) {
        final int end = count > 0 ? start + count : schedulerExecutions.size();
        final int startIndex = Math.min(start, schedulerExecutions.size());
        final int endIndex = Math.min(end, schedulerExecutions.size());
        return schedulerExecutions.subList(startIndex, endIndex);
    }

    private void sort(final List<SchedulerExecution> schedulerExecutions) {
        if (schedulerExecutions != null && !schedulerExecutions.isEmpty() && schedulerExecutions.size() > 1) {
            schedulerExecutions.sort((schedulerExecution, schedulerExecutionToCompare)
                    -> Long.signum(schedulerExecutionToCompare.getId() - schedulerExecution.getId()));
        } else {
            log.trace("Empty Collection, nothing to sort");
        }
    }

    private List<SchedulerExecution> copy(final Collection<SchedulerExecution> schedulerExecutions) {
        final List<SchedulerExecution> copy = new ArrayList<>();
        for (final SchedulerExecution schedulerExecution : schedulerExecutions) {
            copy.add(this.copy(schedulerExecution));
        }
        return copy;
    }

    private SchedulerExecution copy(final SchedulerExecution schedulerExecution) {
        final SchedulerExecution copy = new SchedulerExecution();
        copy.setSchedulerConfigurationId(schedulerExecution.getSchedulerConfigurationId());
        copy.setNextFireTime(schedulerExecution.getNextFireTime());
        copy.setState(schedulerExecution.getState());
        copy.setExecutionCount(schedulerExecution.getExecutionCount());
        copy.setId(schedulerExecution.getId());
        copy.setLastUpdate(schedulerExecution.getLastUpdate());
        return copy;
    }
}
