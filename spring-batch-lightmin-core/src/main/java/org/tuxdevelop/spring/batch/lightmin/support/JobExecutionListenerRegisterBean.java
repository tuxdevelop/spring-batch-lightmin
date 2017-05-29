package org.tuxdevelop.spring.batch.lightmin.support;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.job.SimpleJob;

@Slf4j
public class JobExecutionListenerRegisterBean {

    private final JobExecutionListener jobExecutionFinishedJobExecutionListener;

    public JobExecutionListenerRegisterBean(final JobExecutionListener jobExecutionFinishedJobExecutionListener) {
        this.jobExecutionFinishedJobExecutionListener = jobExecutionFinishedJobExecutionListener;
    }

    public void registerListener(final Job job) {
        try {
            final SimpleJob simpleJob = (SimpleJob) job;
            final JobExecutionListener[] jobExecutionListeners = new JobExecutionListener[]{this.jobExecutionFinishedJobExecutionListener};
            simpleJob.setJobExecutionListeners(jobExecutionListeners);
        } catch (final Exception e) {
            log.error("Could not cast {} to rg.springframework.batch.core.job.SimpleJob ", job);
        }
    }
}
