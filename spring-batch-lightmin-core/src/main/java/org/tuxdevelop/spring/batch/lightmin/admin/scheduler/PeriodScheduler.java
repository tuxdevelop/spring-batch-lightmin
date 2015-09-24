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
public class PeriodScheduler extends AbstractScheduler implements Scheduler {

    private final JobConfiguration jobConfiguration;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final JobSchedulerConfiguration jobSchedulerConfiguration;
    private final Job job;
    private final JobRunner jobRunner;

    public PeriodScheduler(final SchedulerConstructorWrapper schedulerConstructorWrapper) {
        this.jobConfiguration = schedulerConstructorWrapper.getJobConfiguration();
        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.afterPropertiesSet();
        jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
        this.job = schedulerConstructorWrapper.getJob();
        jobRunner = new JobRunner(job, schedulerConstructorWrapper.getJobLauncher(),
                schedulerConstructorWrapper.getJobParameters(),
                schedulerConstructorWrapper.getJobIncrementer());
        final SchedulerStatus schedulerStatus;
        if (jobSchedulerConfiguration.getSchedulerStatus() != null) {
            schedulerStatus = jobSchedulerConfiguration.getSchedulerStatus();
        } else {
            schedulerStatus = SchedulerStatus.INITIALIZED;
        }
        setStatus(schedulerStatus);
    }

    @Override
    public void schedule() {
        final Date initialDelay = new Date(System.currentTimeMillis() + jobSchedulerConfiguration.getInitialDelay());
        log.debug("Scheduling: " + jobRunner.getJob().getName() + " with Parameters: "
                + jobRunner.getJobParameters().toProperties());
        threadPoolTaskScheduler.scheduleWithFixedDelay(jobRunner, initialDelay,
                jobSchedulerConfiguration.getFixedDelay());
        setStatus(SchedulerStatus.RUNNING);
    }

    @Override
    public void terminate() {
        threadPoolTaskScheduler.shutdown();
        while (threadPoolTaskScheduler.getActiveCount() > 0) {
            setStatus(SchedulerStatus.IN_TERMINATION);
        }
        threadPoolTaskScheduler.initialize();
        setStatus(SchedulerStatus.STOPPED);
    }

    @Override
    public void afterPropertiesSet() {
        assert (jobConfiguration != null);
        assert (threadPoolTaskScheduler != null);
        assert (jobSchedulerConfiguration != null);
        assert (job != null);
        assert (jobRunner != null);
    }

}
