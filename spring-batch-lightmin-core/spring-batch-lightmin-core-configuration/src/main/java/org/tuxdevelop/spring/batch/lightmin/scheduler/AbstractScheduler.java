package org.tuxdevelop.spring.batch.lightmin.scheduler;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.InitializingBean;
import org.tuxdevelop.spring.batch.lightmin.domain.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.domain.SchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

/**
 * @author Marcel Becker
 * @version 0.1
 * @see PeriodScheduler
 * @see CronScheduler
 */
@Slf4j
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

        JobRunner(final Job job, final JobLauncher jobLauncher,
                  final JobParameters jobParameters,
                  final JobIncrementer jobIncrementer) {
            this.job = job;
            this.jobLauncher = jobLauncher;
            this.jobParameters = jobParameters;
            this.jobIncrementer = jobIncrementer;
        }

        @Override
        public void run() {
            try {
                this.attachJobIncrementer();
                log.debug("Launching Job {}", this.job.getName());
                this.jobLauncher.run(this.job, this.jobParameters);
            } catch (final Exception e) {
                throw new SpringBatchLightminApplicationException(e, e.getMessage());
            }
        }

        private void attachJobIncrementer() {
            if (this.jobParameters == null) {
                this.jobParameters = new JobParametersBuilder().toJobParameters();
            }
            if (JobIncrementer.DATE.equals(this.jobIncrementer)) {
                final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder(this.jobParameters);
                this.jobParameters = jobParametersBuilder.addLong(JobIncrementer.DATE.getIncrementerIdentifier(), System.currentTimeMillis()).toJobParameters();
            }
        }

    }

    @Override
    public SchedulerStatus getSchedulerStatus() {
        return this.status;
    }
}
