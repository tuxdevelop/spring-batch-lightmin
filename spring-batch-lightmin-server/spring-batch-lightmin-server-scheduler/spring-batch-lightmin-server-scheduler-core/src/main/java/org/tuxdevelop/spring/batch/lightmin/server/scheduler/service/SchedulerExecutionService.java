package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.exception.SchedulerRuntimException;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ExecutionStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerValidationException;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Slf4j
public class SchedulerExecutionService {

    private final SchedulerExecutionRepository schedulerExecutionRepository;

    public SchedulerExecutionService(final SchedulerExecutionRepository schedulerExecutionRepository) {
        this.schedulerExecutionRepository = schedulerExecutionRepository;
    }

    @Transactional(transactionManager = "lightminServerSchedulerTransactionManager", propagation = Propagation.REQUIRED)
    public SchedulerExecution save(final SchedulerExecution schedulerExecution) {
        if (schedulerExecution == null) {
            throw new SchedulerValidationException("schedulerExecution must not be null");
        } else {
            schedulerExecution.validate();
            return this.schedulerExecutionRepository.save(schedulerExecution);
        }
    }

    @Transactional(transactionManager = "lightminServerSchedulerTransactionManager", propagation = Propagation.REQUIRED)
    public void createNextExecution(final SchedulerExecution schedulerExecution, final String cronExpression) {
        if (schedulerExecution != null) {
            //1. set the id to null, so a new entry will be created
            schedulerExecution.setId(null);
            //2. set count to 0
            schedulerExecution.setExecutionCount(0);
            //3. set status to new
            schedulerExecution.setState(ExecutionStatus.NEW);
            //4. determine next fire time
            final Date nextFireTime = this.getNextFireTime(cronExpression);
            schedulerExecution.setNextFireTime(nextFireTime);
            //5. save
            this.save(schedulerExecution);
        } else {
            throw new SchedulerValidationException("schedulerExecution must not be null");
        }
    }

    public List<SchedulerExecution> findScheduledExecutions(final Integer state, final Date date) {
        return this.schedulerExecutionRepository.findByStateAndDate(state, date);
    }

    public List<SchedulerExecution> findScheduledExecutions(final Integer state) {
        return this.schedulerExecutionRepository.findByState(state);
    }

    public Date getNextFireTime(final String cronExpression) {
        final CronTriggerImpl cronTrigger = new CronTriggerImpl();
        try {
            cronTrigger.setCronExpression(cronExpression);
            return cronTrigger.getFireTimeAfter(new Date());
        } catch (final ParseException e) {
            throw new SchedulerRuntimException("Could not determine next fire date", e);
        }
    }
}
