package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;

import java.text.ParseException;
import java.util.Date;

@Slf4j
public class SchedulerExecutionService {

    private final SchedulerExecutionRepository schedulerExecutionRepository;

    public SchedulerExecutionService(final SchedulerExecutionRepository schedulerExecutionRepository) {
        this.schedulerExecutionRepository = schedulerExecutionRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SchedulerExecution save(final SchedulerExecution schedulerExecution) {
        //TODO: validate
        return this.schedulerExecutionRepository.save(schedulerExecution);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void createNextExecution(final SchedulerExecution schedulerExecution, final String cronExpression) {
        //1. set the id to null, so a new entry will be created
        schedulerExecution.setId(null);
        //2. set count to 0
        schedulerExecution.setExecutionCount(0);
        //3. set status to new
        schedulerExecution.setState(ExecutionStatus.NEW);
        //4. determine next fire time
        final Date nextFireTime = getNextFireTime(cronExpression);
        schedulerExecution.setNextFireTime(nextFireTime);
        //5. save
        this.save(schedulerExecution);
    }

    public Date getNextFireTime(final String cronExpression) {
        final CronTriggerImpl cronTrigger = new CronTriggerImpl();
        try {
            cronTrigger.setCronExpression(cronExpression);
            return cronTrigger.getNextFireTime();
        } catch (final ParseException e) {
            //TODO: handle exception
            throw new RuntimeException(e);
        }
    }
}
