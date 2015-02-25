package org.tuxdevelop.spring.batch.lightmin.admin.domain;

import lombok.Data;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

@Data
public class SchedulerConstructorWrapper {

    private JobConfiguration jobConfiguration;
    private Job job;
    private JobParameters jobParameters;
    private JobIncrementer jobIncrementer;
    private JobLauncher jobLauncher;

}
