package org.tuxdevelop.spring.batch.lightmin.admin.scheduler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.InitializingBean;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.Date;

public abstract class AbstractScheduler implements InitializingBean {

    @Getter
    @Setter
    private SchedulerStatus status;

    protected static class JobRunner implements Runnable {

        @Getter
        private final Job job;
        private final JobLauncher jobLauncher;
        @Getter
        private JobParameters jobParameters;
        private final JobIncrementer jobIncrementer;

        public JobRunner(final Job job, final JobLauncher jobLauncher, final JobParameters jobParameters,
                         final JobIncrementer jobIncrementer) {
            this.job = job;
            this.jobLauncher = jobLauncher;
            this.jobParameters = jobParameters;
            this.jobIncrementer = jobIncrementer;
            this.jobParameters = jobParameters;
        }

        @Override
        public void run() {
            try {
                attacheJobIncrementer();
                jobLauncher.run(job, jobParameters);
            } catch (Exception e) {
                throw new SpringBatchLightminApplicationException(e, e.getMessage());
            }
        }

        private void attacheJobIncrementer() {
            if (jobParameters == null) {
                jobParameters = new JobParametersBuilder().toJobParameters();
            }
            if (JobIncrementer.DATE.equals(jobIncrementer)) {
                final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder(jobParameters);
                jobParameters = jobParametersBuilder.addDate(JobIncrementer.DATE.getIncrementerIdentifier(), new Date())
                        .toJobParameters();
            }
        }

    }
}
