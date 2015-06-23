package org.tuxdevelop.spring.batch.lightmin.util;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobFactory;

public class CommonJobFactory implements JobFactory {

    private final Job job;
    private final String jobName;

    public CommonJobFactory(final Job job, final String jobName) {
        this.job = job;
        this.jobName = jobName;
    }

    @Override
    public Job createJob() {
        return job;
    }

    @Override
    public String getJobName() {
        return jobName;
    }
}
