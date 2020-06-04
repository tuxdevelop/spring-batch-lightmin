package org.tuxdevelop.spring.batch.lightmin.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.utils.LightminExitStatus;
import org.tuxdevelop.spring.batch.lightmin.utils.LightminMetricSource;
import org.tuxdevelop.spring.batch.lightmin.utils.LightminMetricUtils;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

@Slf4j
public class MetricServiceBean implements MetricService {

    public static final String TAG_NAME = "name";
    public static final String TAG_STATUS = "status";
    public static final String TAG_JOBNAME = "jobname";
    public static final String TAG_APPNAME = "appname";

    private final MeterRegistry registry;

    public MetricServiceBean(final MeterRegistry registry) {
        this.registry = registry;
    }


    @Override
    public void measureStepExecution(final LightminMetricSource source, final StepExecutionEventInfo stepExecutionEventInfo) {
        requireNonNull(this.registry, "registry");

        // ExitStatus of Job is UNKNOWN while steps are in Execution
        final Tags tags = Tags.of(
                Tag.of(TAG_NAME, stepExecutionEventInfo.getStepName()),
                Tag.of(TAG_STATUS, stepExecutionEventInfo.getExitStatus().getExitCode()),
                Tag.of(TAG_JOBNAME, stepExecutionEventInfo.getJobName()),
                Tag.of(TAG_APPNAME, stepExecutionEventInfo.getApplicationName()));

        //Case FAILED

        if (LightminExitStatus.FAILED.getExitCode().equals(stepExecutionEventInfo.getExitStatus().getExitCode())) {

            final String dataRollback = LightminMetricUtils.getMetricNameOrNull(source, LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_ROLLBACK);

            if (!isNull(dataRollback)) {
                Gauge.builder(dataRollback, stepExecutionEventInfo.getRollbackCount(), Integer::doubleValue)
                        .tags(tags)
                        .strongReference(true)
                        .register(this.registry);
            } else {
                log.info("{}_{} is unknown by Lightmin Context and is therefore not created.", source, LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_ROLLBACK);
            }
        } else {
            log.trace("Nothing to handle for ExitCode FAILED");
        }

        final String dataRead = LightminMetricUtils.getMetricNameOrNull(source, LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_READ);

        if (!isNull(dataRead)) {
            Gauge.builder(dataRead, stepExecutionEventInfo.getReadCount(), Integer::doubleValue)
                    .tags(tags)
                    .strongReference(true)
                    .register(this.registry);
        } else {
            log.info("{}_{} is unknown by Lightmin Context and is therefore not created.", source, LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_READ);
        }
        final String dataWrite = LightminMetricUtils.getMetricNameOrNull(source, LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_WRITE);

        if (!isNull(dataWrite)) {
            Gauge.builder(dataWrite, stepExecutionEventInfo.getWriteCount(), Integer::doubleValue)
                    .tags(tags)
                    .strongReference(true)
                    .register(this.registry);
        } else {
            log.info("{}_{} is unknown by Lightmin Context and is therefore not created.", source, LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_WRITE);
        }
        final String dataCommit = LightminMetricUtils.getMetricNameOrNull(source, LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_WRITE);

        if (!isNull(dataCommit)) {
            Gauge.builder(dataCommit, stepExecutionEventInfo.getCommitCount(), Integer::doubleValue)
                    .tags(tags)
                    .strongReference(true)
                    .register(this.registry);
        } else {
            log.info("{}_{} is unknown by Lightmin Context and is therefore not created.", source, LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_WRITE);
        }
    }

    @Override
    public void measureJobExecution(final LightminMetricSource source, final JobExecutionEventInfo jobExecutionEventInfo) {
        requireNonNull(this.registry, "registry");

        if (!LightminExitStatus.UNKNOWN.getExitCode().equals(jobExecutionEventInfo.getExitStatus().getExitCode())) {
            final String metricName = LightminMetricUtils.getMetricNameOrNull(source, LightminMetricUtils.LightminMetrics.LIGHTMIN_JOB_STATUS);
            if (!isNull(metricName)) {
                Gauge.builder(metricName, jobExecutionEventInfo.getExitStatus().getExitCode(),
                        (exitCode) -> (double) LightminExitStatus.getLightminMetricExitIdByExitStatus(exitCode))
                        .tags(Tags.of(
                                Tag.of(TAG_NAME, jobExecutionEventInfo.getJobName()),
                                Tag.of(TAG_APPNAME, jobExecutionEventInfo.getApplicationName()))
                        ).strongReference(true)
                        .register(this.registry);
            } else {
                log.info("{}_{} is unknown by Lightmin Context and is therefore not created.", source, LightminMetricUtils.LightminMetrics.LIGHTMIN_JOB_STATUS);
            }
        } else {
            // We only want to update the Status when the Job Exited the status
            log.trace("nothing to update, JobExecution not finished, status UNKNOWN");
        }
    }
}
