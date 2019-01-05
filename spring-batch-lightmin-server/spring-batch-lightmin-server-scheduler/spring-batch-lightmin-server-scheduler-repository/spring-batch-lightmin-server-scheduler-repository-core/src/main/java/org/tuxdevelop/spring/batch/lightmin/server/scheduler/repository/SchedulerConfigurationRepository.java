package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;

import java.util.List;

public interface SchedulerConfigurationRepository {

    SchedulerConfiguration save(SchedulerConfiguration schedulerConfiguration);

    SchedulerConfiguration findById(Long id);

    void delete(Long id);

    List<SchedulerConfiguration> findAll();

    List<SchedulerConfiguration> findByApplication(final String application);

}
