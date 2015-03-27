package org.tuxdevelop.spring.batch.lightmin.admin.scheduler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerConstructorWrapper;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerStatus;

import java.util.TimeZone;

@Slf4j
@Getter
public class CronScheduler extends AbstractScheduler implements Scheduler{

    private final JobConfiguration jobConfiguration;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final JobSchedulerConfiguration jobSchedulerConfiguration;
    private final Job job;
    private final JobRunner jobRunner;
    private final Trigger trigger;
    private final TimeZone timeZone;

    public CronScheduler(final SchedulerConstructorWrapper schedulerConstructorWrapper) {
        this.jobConfiguration = schedulerConstructorWrapper.getJobConfiguration();
        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.afterPropertiesSet();
        jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
        timeZone = TimeZone.getDefault();
        trigger = new CronTrigger(jobSchedulerConfiguration.getCronExpression(), timeZone);
        this.job = schedulerConstructorWrapper.getJob();
        jobRunner = new JobRunner(job, schedulerConstructorWrapper.getJobLauncher(),
                schedulerConstructorWrapper.getJobParameters(),
                schedulerConstructorWrapper.getJobIncrementer());
        setStatus(SchedulerStatus.INITIALIZED);
    }

    @Override
    public void schedule() {
        log.info("Scheduling: " + jobRunner.getJob().getName() + " with Parameters: "
                + jobRunner.getJobParameters().toProperties());
        threadPoolTaskScheduler.schedule(jobRunner, trigger);
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
        assert (timeZone != null);
        assert (trigger != null);
    }

}
