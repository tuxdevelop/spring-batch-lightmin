package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerValidationException;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ServerSchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerConfigurationNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class KVSchedulerConfigurationRepository<T extends Map<Long, SchedulerConfiguration>> implements SchedulerConfigurationRepository {

    private final T store;

    protected KVSchedulerConfigurationRepository(final T store) {
        this.store = store;
    }

    @Override
    public SchedulerConfiguration save(final SchedulerConfiguration schedulerConfiguration) {
        final Long id;
        if (schedulerConfiguration != null) {
            if (schedulerConfiguration.getId() != null) {
                id = schedulerConfiguration.getId();
            } else {
                id = this.getNextId();
                schedulerConfiguration.setId(id);
            }
            this.store.put(id, this.copy(schedulerConfiguration));
            return schedulerConfiguration;
        } else {
            throw new SchedulerValidationException("schedulerConfiguration must not be null");
        }
    }

    @Override
    public SchedulerConfiguration findById(final Long id) throws SchedulerConfigurationNotFoundException {
        final SchedulerConfiguration schedulerConfiguration;
        if (this.store.containsKey(id)) {
            schedulerConfiguration = this.copy(this.store.get(id));
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
            schedulerConfigurations.addAll(this.copy(this.store.values()));
            this.sort(schedulerConfigurations);
        }
        return schedulerConfigurations;
    }

    @Override
    public List<SchedulerConfiguration> findAll(final int startIndex, final int pageSize) {
        final List<SchedulerConfiguration> schedulerConfigurations = this.findAll();
        return this.subset(schedulerConfigurations, startIndex, pageSize);
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

    protected abstract Long getNextId();

    private List<SchedulerConfiguration> copy(final Collection<SchedulerConfiguration> schedulerConfigurations) {
        final List<SchedulerConfiguration> copy = new ArrayList<>();
        for (final SchedulerConfiguration schedulerConfiguration : schedulerConfigurations) {
            copy.add(this.copy(schedulerConfiguration));
        }
        return copy;
    }

    private SchedulerConfiguration copy(final SchedulerConfiguration schedulerConfiguration) {
        final SchedulerConfiguration copy = new SchedulerConfiguration();
        copy.setId(schedulerConfiguration.getId());
        copy.setCronExpression(schedulerConfiguration.getCronExpression());
        copy.setJobParameters(schedulerConfiguration.getJobParameters());
        copy.setJobIncrementer(schedulerConfiguration.getJobIncrementer());
        copy.setRetryable(schedulerConfiguration.getRetryable());
        copy.setInstanceExecutionCount(schedulerConfiguration.getInstanceExecutionCount());
        copy.setMaxRetries(schedulerConfiguration.getMaxRetries());
        copy.setJobName(schedulerConfiguration.getJobName());
        copy.setApplication(schedulerConfiguration.getApplication());
        copy.setStatus(this.getStatusValue(schedulerConfiguration));
        copy.setRetryInterval(schedulerConfiguration.getRetryInterval());
        return copy;
    }

    private ServerSchedulerStatus getStatusValue(final SchedulerConfiguration schedulerConfiguration) {

        return schedulerConfiguration.getStatus() != null ?
                ServerSchedulerStatus.getByValue(schedulerConfiguration.getStatus().getValue()) : null;
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
