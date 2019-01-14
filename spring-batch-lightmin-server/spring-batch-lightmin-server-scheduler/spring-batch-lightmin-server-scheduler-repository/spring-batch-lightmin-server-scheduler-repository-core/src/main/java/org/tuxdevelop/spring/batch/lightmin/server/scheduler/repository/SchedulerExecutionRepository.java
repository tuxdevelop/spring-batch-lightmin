package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;

import java.util.Date;
import java.util.List;

public interface SchedulerExecutionRepository {

    SchedulerExecution save(SchedulerExecution schedulerExecution);

    SchedulerExecution findById(Long id);

    void delete(Long id);

    List<SchedulerExecution> findAll();

    List<SchedulerExecution> findNextExecutions(Date date);

    List<SchedulerExecution> findByState(Integer state);

    List<SchedulerExecution> findByStateAndDate(Integer state, Date date);

    List<SchedulerExecution> findBySchedulerConfigurationId(Long schedulerConfigurationId);

    void deleteByState(Integer state);

}
