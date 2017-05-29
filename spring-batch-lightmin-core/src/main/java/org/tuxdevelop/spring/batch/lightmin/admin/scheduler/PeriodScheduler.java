package org.tuxdevelop.spring.batch.lightmin.admin.scheduler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerConstructorWrapper;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerStatus;

import java.util.Date;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Slf4j
@Getter
public class PeriodScheduler extends AbstractScheduler {

    private final JobConfiguration jobConfiguration;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final JobSchedulerConfiguration jobSchedulerConfiguration;
    private final Job job;
    private final JobRunner jobRunner;

    public PeriodScheduler(final SchedulerConstructorWrapper schedulerConstructorWrapper) {
        this.jobConfiguration = schedulerConstructorWrapper.getJobConfiguration();
        this.threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        this.threadPoolTaskScheduler.setPoolSize(1);
        this.threadPoolTaskScheduler.afterPropertiesSet();
        this.jobSchedulerConfiguration = this.jobConfiguration.getJobSchedulerConfiguration();
        this.job = schedulerConstructorWrapper.getJob();
        this.jobRunner = new JobRunner(this.job, schedulerConstructorWrapper.getJobLauncher(),
                schedulerConstructorWrapper.getJobParameters(),
                schedulerConstructorWrapper.getJobIncrementer());
        final SchedulerStatus schedulerStatus;
        if (this.jobSchedulerConfiguration.getSchedulerStatus() != null) {
            schedulerStatus = this.jobSchedulerConfiguration.getSchedulerStatus();
        } else {
            schedulerStatus = SchedulerStatus.INITIALIZED;
        }
        setStatus(schedulerStatus);
    }

    @Override
    public void schedule() {
        final Date initialDelay = new Date(System.currentTimeMillis() + this.jobSchedulerConfiguration.getInitialDelay());
        log.debug("Scheduling: " + this.jobRunner.getJob().getName() +
                " with Parameters: " + this.jobRunner.getJobParameters().toProperties());
        this.threadPoolTaskScheduler.scheduleWithFixedDelay(this.jobRunner, initialDelay, this.jobSchedulerConfiguration.getFixedDelay());
        setStatus(SchedulerStatus.RUNNING);
    }

    @Override
    public void terminate() {
        this.threadPoolTaskScheduler.shutdown();
        while (this.threadPoolTaskScheduler.getActiveCount() > 0) {
            setStatus(SchedulerStatus.IN_TERMINATION);
        }
        this.threadPoolTaskScheduler.initialize();
        setStatus(SchedulerStatus.STOPPED);
    }

    @Override
    public void afterPropertiesSet() {
        assert (this.jobConfiguration != null);
        assert (this.threadPoolTaskScheduler != null);
        assert (this.jobSchedulerConfiguration != null);
        assert (this.job != null);
        assert (this.jobRunner != null);
    }

}
