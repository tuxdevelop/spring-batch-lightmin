package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerExecutionNotFoundException;

import java.util.Date;
import java.util.List;

public interface SchedulerExecutionRepository {

    SchedulerExecution save(SchedulerExecution schedulerExecution);

    SchedulerExecution findById(Long id) throws SchedulerExecutionNotFoundException;

    void delete(Long id);

    void deleteBySchedulerConfigurationId(Long schedulerConfigurationId);

    List<SchedulerExecution> findAll();

    List<SchedulerExecution> findAll(int startIndex, int pageSize);

    List<SchedulerExecution> findNextExecutions(Date date);

    List<SchedulerExecution> findByState(Integer state);

    List<SchedulerExecution> findByState(Integer state, int startIndex, int pageSize);

    List<SchedulerExecution> findByStateAndDate(Integer state, Date date);

    List<SchedulerExecution> findBySchedulerConfigurationId(Long schedulerConfigurationId);

    Integer getExecutionCount(Integer state);

    void deleteByState(Integer state);

    void deleteByConfigurationAndState(Long configurationId, Integer state);

}
