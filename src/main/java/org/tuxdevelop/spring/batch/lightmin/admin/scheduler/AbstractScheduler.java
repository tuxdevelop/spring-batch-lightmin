package org.tuxdevelop.spring.batch.lightmin.admin.scheduler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.InitializingBean;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerStatus;

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

        // TODO exception handling
        @Override
        public void run() {
            try {
                jobLauncher.run(job, jobParameters);
            } catch (final JobExecutionAlreadyRunningException e) {
                e.printStackTrace();
            } catch (final JobRestartException e) {
                e.printStackTrace();
            } catch (final JobInstanceAlreadyCompleteException e) {
                e.printStackTrace();
            } catch (final JobParametersInvalidException e) {
                e.printStackTrace();
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
