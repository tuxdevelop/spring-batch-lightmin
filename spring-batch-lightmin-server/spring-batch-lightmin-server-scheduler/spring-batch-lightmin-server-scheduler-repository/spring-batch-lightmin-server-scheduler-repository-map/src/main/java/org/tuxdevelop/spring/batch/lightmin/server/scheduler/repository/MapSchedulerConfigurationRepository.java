package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerConfigurationNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class MapSchedulerConfigurationRepository implements SchedulerConfigurationRepository {

    private final Map<Long, SchedulerConfiguration> store = new HashMap<>();
    private final AtomicLong currentId = new AtomicLong(1L);

    @Override
    public SchedulerConfiguration save(final SchedulerConfiguration schedulerConfiguration) {
        final Long id;
        if (schedulerConfiguration.getId() != null) {
            id = schedulerConfiguration.getId();
        } else {
            id = getNextId();
            schedulerConfiguration.setId(id);
        }
        this.store.put(id, schedulerConfiguration);
        return schedulerConfiguration;
    }

    @Override
    public SchedulerConfiguration findById(final Long id) throws SchedulerConfigurationNotFoundException {
        final SchedulerConfiguration schedulerConfiguration;
        if (this.store.containsKey(id)) {
            schedulerConfiguration = this.store.get(id);
        } else {
            throw new SchedulerConfigurationNotFoundException("Could not find a SchedulerConfiguration for id " + id);
        }
        return schedulerConfiguration;
    }

    @Override
    public void delete(final Long id) {
        final SchedulerConfiguration schedulerConfiguration;
        if (this.store.containsKey(id)) {
            schedulerConfiguration = this.store.get(id);
            this.store.remove(id, schedulerConfiguration);
        } else {
            log.warn("Could not delete SchedulerConfiguration with id {}, not present!", id);
        }
    }

    @Override
    public List<SchedulerConfiguration> findAll() {

        final List<SchedulerConfiguration> schedulerConfigurations = new ArrayList<>();
        if (this.store.isEmpty()) {
            log.debug("No SchedulerConfigurations available");
        } else {
            schedulerConfigurations.addAll(this.store.values());
            this.sort(schedulerConfigurations);
        }
        return schedulerConfigurations;
    }

    @Override
    public List<SchedulerConfiguration> findAll(final int startIndex, final int pageSize) {
        final List<SchedulerConfiguration> schedulerConfigurations = findAll();
        return subset(schedulerConfigurations, startIndex, pageSize);
    }

    @Override
    public List<SchedulerConfiguration> findByApplication(final String application) {

        final List<SchedulerConfiguration> schedulerConfigurations = new ArrayList<>();

        final List<SchedulerConfiguration> all = this.findAll();

        for (final SchedulerConfiguration schedulerConfiguration : all) {
            if (schedulerConfiguration.getApplication().equals(application)) {
                schedulerConfigurations.add(schedulerConfiguration);
            } else {
                log.trace("No SchedulerConfiguration for {}", application);
            }
        }
        this.sort(schedulerConfigurations);
        return schedulerConfigurations;
    }

    @Override
    public Integer getCount() {
        return this.store.size();
    }

    private synchronized Long getNextId() {
        return this.currentId.getAndIncrement();
    }

    private List<SchedulerConfiguration> subset(final List<SchedulerConfiguration> schedulerConfigurations,
                                                final int start,
                                                final int count) {
        final int end = count > 0 ? start + count : schedulerConfigurations.size();
        final int startIndex = Math.min(start, schedulerConfigurations.size());
        final int endIndex = Math.min(end, schedulerConfigurations.size());
        return schedulerConfigurations.subList(startIndex, endIndex);
    }

    private void sort(final List<SchedulerConfiguration> schedulerConfigurations) {
        schedulerConfigurations.sort((schedulerConfiguration, schedulerConfigurationToCompare)
                -> Long.signum(schedulerConfiguration.getId() - schedulerConfigurationToCompare.getId()));
    }

}
