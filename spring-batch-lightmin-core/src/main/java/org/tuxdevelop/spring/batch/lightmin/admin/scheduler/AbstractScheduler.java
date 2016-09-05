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

/**
 * @author Marcel Becker
 * @version 0.1
 * @see PeriodScheduler
 * @see CronScheduler
 */
abstract class AbstractScheduler implements Scheduler, InitializingBean {

    @Setter
    private SchedulerStatus status;

    static class JobRunner implements Runnable {

        @Getter
        private final Job job;
        private final JobLauncher jobLauncher;
        @Getter
        private JobParameters jobParameters;
        private final JobIncrementer jobIncrementer;

        JobRunner(final Job job, final JobLauncher jobLauncher, final JobParameters jobParameters,
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
                attachJobIncrementer();
                jobLauncher.run(job, jobParameters);
            } catch (final Exception e) {
                throw new SpringBatchLightminApplicationException(e, e.getMessage());
            }
        }

        private void attachJobIncrementer() {
            if (jobParameters == null) {
                jobParameters = new JobParametersBuilder().toJobParameters();
            }
            if (JobIncrementer.DATE.equals(jobIncrementer)) {
                final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder(jobParameters);
                jobParameters = jobParametersBuilder.addLong(JobIncrementer.DATE.getIncrementerIdentifier(), System.currentTimeMillis()).toJobParameters();
            }
        }

    }

    public SchedulerStatus getSchedulerStatus() {
        return this.status;
    }
}
