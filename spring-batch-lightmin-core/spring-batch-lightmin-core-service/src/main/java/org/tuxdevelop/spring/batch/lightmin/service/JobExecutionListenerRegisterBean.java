package org.tuxdevelop.spring.batch.lightmin.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.job.flow.FlowJob;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

@Slf4j
public class JobExecutionListenerRegisterBean {

    private final JobExecutionListener jobExecutionFinishedJobExecutionListener;

    public JobExecutionListenerRegisterBean(final JobExecutionListener jobExecutionFinishedJobExecutionListener) {
        this.jobExecutionFinishedJobExecutionListener = jobExecutionFinishedJobExecutionListener;
    }

    public void registerListener(final Job job) {
        if (job != null) {
            try {
                if (job instanceof SimpleJob) {
                    final SimpleJob simpleJob = (SimpleJob) job;
                    final JobExecutionListener[] jobExecutionListeners = this.getJobExecutionListeners();
                    simpleJob.setJobExecutionListeners(jobExecutionListeners);
                } else if (job instanceof FlowJob) {
                    final FlowJob flowJob = (FlowJob) job;
                    final JobExecutionListener[] jobExecutionListeners = this.getJobExecutionListeners();
                    flowJob.setJobExecutionListeners(jobExecutionListeners);
                } else {
                    throw new SpringBatchLightminApplicationException("Could not cast" + job +
                            "to org.springframework.batch.core.job.SimpleJob or " +
                            "org.springframework.batch.core.job.FlowJob");
                }
            } catch (final Exception e) {
                log.error("Could not cast {} to org.springframework.batch.core.job.SimpleJob or " +
                        "org.springframework.batch.core.job.FlowJob", job);
            }
        } else {
            throw new SpringBatchLightminApplicationException("Could not register listener for a null job");
        }
    }

    private JobExecutionListener[] getJobExecutionListeners() {
        return new JobExecutionListener[]{this.jobExecutionFinishedJobExecutionListener};
    }
}
