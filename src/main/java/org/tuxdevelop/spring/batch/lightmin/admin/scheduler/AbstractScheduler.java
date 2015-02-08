package org.tuxdevelop.spring.batch.lightmin.admin.scheduler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.InitializingBean;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.execption.SpringBatchLightminApplicationException;

import java.util.Date;

public abstract class AbstractScheduler implements InitializingBean {

    @Getter
    @Setter
    private SchedulerStatus status;

    protected abstract void schedule();

    protected abstract void terminate();

    protected static class JobRunner implements Runnable {

        @Getter
        private final Job job;
        private final JobLauncher jobLauncher;
        @Getter
        private final JobParameters jobParameters;
        private final JobIncrementer jobIncrementer;

        public JobRunner(final Job job, final JobLauncher jobLauncher, final JobParameters jobParameters,
                         final JobIncrementer jobIncrementer) {
            this.job = job;
            this.jobLauncher = jobLauncher;
            this.jobParameters = jobParameters;
            this.jobIncrementer = jobIncrementer;
            attacheJobIncrementer(jobParameters);
        }

        @Override
        public void run() {
            try {
                jobLauncher.run(job, jobParameters);
            } catch (Exception e) {
                throw new SpringBatchLightminApplicationException(e, e.getMessage());
            }
        }

        private void attacheJobIncrementer(JobParameters jobParameters) {
            if (jobParameters == null) {
                jobParameters = new JobParametersBuilder().toJobParameters();
            }
            if (JobIncrementer.DATE.equals(jobIncrementer)) {
                jobParameters.getParameters().put(JobIncrementer.DATE.getIncrementerIdentifier(),
                        new JobParameter(new Date()));
            }
        }

    }
}
