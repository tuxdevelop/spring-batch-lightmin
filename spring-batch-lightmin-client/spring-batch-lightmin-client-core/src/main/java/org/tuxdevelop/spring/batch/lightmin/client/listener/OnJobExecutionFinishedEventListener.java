package org.tuxdevelop.spring.batch.lightmin.client.listener;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationListener;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.event.JobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.util.LightminExitStatus;
import org.tuxdevelop.spring.batch.lightmin.event.EventTransformer;
import org.tuxdevelop.spring.batch.lightmin.event.JobExecutionEvent;

import static java.util.Objects.requireNonNull;
import static org.tuxdevelop.spring.batch.lightmin.utils.LightminMetricName.*;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class OnJobExecutionFinishedEventListener implements ApplicationListener<JobExecutionEvent> {
    private final JobExecutionEventPublisher jobExecutionEventPublisher;

    private final MeterRegistry registry;

    public OnJobExecutionFinishedEventListener(final JobExecutionEventPublisher jobExecutionEventPublisher, final MeterRegistry registry) {
        this.jobExecutionEventPublisher = jobExecutionEventPublisher;
        this.registry = registry;
    }

    @Override
    public void onApplicationEvent(final JobExecutionEvent jobExecutionEvent) {
        final JobExecution jobExecution = jobExecutionEvent.getJobExecution();
        if (jobExecution != null) {
            final ExitStatus exitStatus = jobExecution.getExitStatus();
            if (exitStatus != null) {
                log.info(jobExecution.getJobInstance().getJobName() + ", Status: " + jobExecution.getStatus().getBatchStatus() +
                        ", Exit: " + jobExecution.getExitStatus().getExitCode());
                final JobExecutionEventInfo jobExecutionEventInfo =
                        EventTransformer.transformToJobExecutionEventInfo(
                                jobExecution,
                                jobExecutionEvent.getApplicationName());
                this.jobExecutionEventPublisher.publishJobExecutionEvent(jobExecutionEventInfo);

                doMetric(jobExecution);

            } else {
                log.debug("could not fire JobExcutionEvent, exitStatus was null");
            }
        } else {
            log.debug("could not fire JobExcutionEvent, jobExecution was null");
        }
    }

    private String getStatusFromExecution(JobExecution jobExecution) {
        return jobExecution.getExitStatus().equals(ExitStatus.UNKNOWN) ? jobExecution.getStatus().name() : jobExecution.getExitStatus().getExitCode();
    }

    private void doMetric(JobExecution jobExecution) {
        requireNonNull(registry, "registry");

        // ExitStatus of Job is UNKNOWN while steps are in Execution
        jobExecution.getStepExecutions().stream().forEach(stepExecution ->
                {
                    Tags tags = Tags.of(
                            Tag.of("name", stepExecution.getStepName()),
                            Tag.of("status", stepExecution.getExitStatus().getExitCode()),
                            Tag.of("jobname", jobExecution.getJobInstance().getJobName()));

                    if (jobExecution.getExitStatus().equals(ExitStatus.FAILED)) {
                        Gauge.builder(LIGHTMIN_STEP_DATA_ROLLBACK, stepExecution.getRollbackCount(), n -> (double) n)
                                .tags(tags)
                                .strongReference(true)
                                .register(registry);

                    }
                    Gauge.builder(LIGHTMIN_STEP_DATA_READ, stepExecution.getReadCount(), n -> (double) n)
                            .tags(tags)
                            .strongReference(true)
                            .register(registry);

                    Gauge.builder(LIGHTMIN_STEP_DATA_WRITE, stepExecution.getWriteCount(), n -> (double) n)
                            .tags(tags)
                            .strongReference(true)
                            .register(registry);

                    Gauge.builder(LIGHTMIN_STEP_DATA_COMMIT, stepExecution.getCommitCount(), n -> (double) n)
                            .tags(tags)
                            .strongReference(true)
                            .register(registry);

                }
        );

        // We only want to update the Status when the Job Exited the status
        if (!jobExecution.getExitStatus().equals(ExitStatus.UNKNOWN)) {

            Gauge.builder(LIGHTMIN_JOB_STATUS, jobExecution.getExitStatus().getExitCode(),
                    (exitCode) -> {
                        return (double) LightminExitStatus.getLightminMetricExitIdByExitStatus(exitCode);
                    })
                    .tags(Tags.of("name", jobExecution.getJobInstance().getJobName()))
                    .strongReference(true)
                    .register(registry);

        }
    }


}
