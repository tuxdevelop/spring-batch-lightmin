package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerConfigurationNotFoundException;

import java.util.List;

public interface SchedulerConfigurationRepository {

    SchedulerConfiguration save(SchedulerConfiguration schedulerConfiguration);

    SchedulerConfiguration findById(Long id) throws SchedulerConfigurationNotFoundException;

    void delete(Long id);

    List<SchedulerConfiguration> findAll();

    List<SchedulerConfiguration> findAll(int startIndex, int pageSize);

    List<SchedulerConfiguration> findByApplication(final String application);

    Integer getCount();
}
