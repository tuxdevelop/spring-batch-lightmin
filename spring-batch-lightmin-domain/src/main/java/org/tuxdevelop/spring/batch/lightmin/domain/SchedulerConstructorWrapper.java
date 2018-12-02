package org.tuxdevelop.spring.batch.lightmin.domain;

import lombok.Data;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Data
public class SchedulerConstructorWrapper {

    private JobConfiguration jobConfiguration;
    private Job job;
    private JobParameters jobParameters;
    private JobIncrementer jobIncrementer;
    private JobLauncher jobLauncher;
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

}
