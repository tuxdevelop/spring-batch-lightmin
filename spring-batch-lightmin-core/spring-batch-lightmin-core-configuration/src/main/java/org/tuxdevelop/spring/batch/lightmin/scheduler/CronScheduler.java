package org.tuxdevelop.spring.batch.lightmin.scheduler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.SchedulerConstructorWrapper;
import org.tuxdevelop.spring.batch.lightmin.domain.SchedulerStatus;

import java.util.TimeZone;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Slf4j
@Getter
public class CronScheduler extends AbstractScheduler {

    private final JobConfiguration jobConfiguration;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final JobSchedulerConfiguration jobSchedulerConfiguration;
    private final Job job;
    private final JobRunner jobRunner;
    private final Trigger trigger;
    private final TimeZone timeZone;

    public CronScheduler(final SchedulerConstructorWrapper schedulerConstructorWrapper) {
        this.jobConfiguration = schedulerConstructorWrapper.getJobConfiguration();
        this.threadPoolTaskScheduler = schedulerConstructorWrapper.getThreadPoolTaskScheduler();
        this.jobSchedulerConfiguration = this.jobConfiguration.getJobSchedulerConfiguration();
        this.timeZone = TimeZone.getDefault();
        this.trigger = new CronTrigger(this.jobSchedulerConfiguration.getCronExpression(), this.timeZone);
        this.job = schedulerConstructorWrapper.getJob();

        this.jobRunner = new JobRunner(this.job,
                schedulerConstructorWrapper.getJobLauncher(),
                schedulerConstructorWrapper.getJobParameters(),
                schedulerConstructorWrapper.getJobIncrementer());
        final SchedulerStatus schedulerStatus;
        if (this.jobSchedulerConfiguration.getSchedulerStatus() != null) {
            schedulerStatus = this.jobSchedulerConfiguration.getSchedulerStatus();
        } else {
            schedulerStatus = SchedulerStatus.INITIALIZED;
        }
        this.setStatus(schedulerStatus);
    }

    @Override
    public void schedule() {
        log.info("Scheduling: " + this.jobRunner.getJob().getName() +
                " with Parameters: " + this.jobRunner.getJobParameters().toProperties());
        this.threadPoolTaskScheduler.schedule(this.jobRunner, this.trigger);
        this.setStatus(SchedulerStatus.RUNNING);
    }

    @Override
    public void terminate() {
        this.threadPoolTaskScheduler.shutdown();
        while (this.threadPoolTaskScheduler.getActiveCount() > 0) {
            this.setStatus(SchedulerStatus.IN_TERMINATION);
        }
        this.threadPoolTaskScheduler.initialize();
        this.setStatus(SchedulerStatus.STOPPED);
    }

    @Override
    public void afterPropertiesSet() {
        assert (this.jobConfiguration != null);
        assert (this.threadPoolTaskScheduler != null);
        assert (this.jobSchedulerConfiguration != null);
        assert (this.job != null);
        assert (this.jobRunner != null);
        assert (this.timeZone != null);
        assert (this.trigger != null);
    }

}
