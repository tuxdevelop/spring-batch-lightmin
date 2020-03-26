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

    private final MeterRegistry registry;

    public MetricServiceBean(final MeterRegistry registry) {
        this.registry = registry;
    }


    @Override
    public void measureStepExecution(LightminMetricSource source, StepExecutionEventInfo stepExecutionEventInfo) {
        requireNonNull(registry, "registry");

        // ExitStatus of Job is UNKNOWN while steps are in Execution
        Tags tags = Tags.of(
                Tag.of("name", stepExecutionEventInfo.getStepName()),
                Tag.of("status", stepExecutionEventInfo.getExitStatus().getExitCode()),
                Tag.of("jobname", stepExecutionEventInfo.getJobName()),
                Tag.of("appname", stepExecutionEventInfo.getApplicationName()));

        if (stepExecutionEventInfo.getExitStatus().getExitCode().equals(LightminExitStatus.FAILED)) {

            String dataRollback = LightminMetricUtils.getMetricName(source, LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_ROLLBACK);

            if (!isNull(dataRollback)) {
                Gauge.builder(dataRollback, stepExecutionEventInfo.getRollbackCount(), Integer::doubleValue)
                        .tags(tags)
                        .strongReference(true)
                        .register(registry);
            } else {
                log.info(source + "_" + LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_ROLLBACK + "  is unknown by Lightmin Context and is therefore not created.");
            }
        }

        String dataRead = LightminMetricUtils.getMetricName(source, LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_READ);

        if (!isNull(dataRead)) {
            Gauge.builder(dataRead, stepExecutionEventInfo.getReadCount(), Integer::doubleValue)
                    .tags(tags)
                    .strongReference(true)
                    .register(registry);
        } else {
            log.info(source + "_" + LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_READ + "  is unknown by Lightmin Context and is therefore not created.");
        }
        String dataWrite = LightminMetricUtils.getMetricName(source, LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_WRITE);

        if (!isNull(dataWrite)) {
            Gauge.builder(dataWrite, stepExecutionEventInfo.getWriteCount(), Integer::doubleValue)
                    .tags(tags)
                    .strongReference(true)
                    .register(registry);
        } else {
            log.info(source + "_" + LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_WRITE + "  is unknown by Lightmin Context and is therefore not created.");
        }
        String dataCommit = LightminMetricUtils.getMetricName(source, LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_WRITE);
        if (!isNull(dataCommit)) {
            Gauge.builder(dataCommit, stepExecutionEventInfo.getCommitCount(), Integer::doubleValue)
                    .tags(tags)
                    .strongReference(true)
                    .register(registry);
        } else {
            log.info(source + "_" + LightminMetricUtils.LightminMetrics.LIGHTMIN_STEP_DATA_WRITE + " is unknown by Lightmin Context and is therefore not created.");
        }
    }

    @Override
    public void measureJobExecution(LightminMetricSource source, JobExecutionEventInfo jobExecutionEventInfo) {
        requireNonNull(registry, "registry");

        if (!jobExecutionEventInfo.getExitStatus().getExitCode().equals(LightminExitStatus.UNKNOWN.getExitCode())) {
            String metricName = LightminMetricUtils.getMetricName(source, LightminMetricUtils.LightminMetrics.LIGHTMIN_JOB_STATUS);
            if (!isNull(metricName)) {
                Gauge.builder(metricName, jobExecutionEventInfo.getExitStatus().getExitCode(),
                        (exitCode) -> (double) LightminExitStatus.getLightminMetricExitIdByExitStatus(exitCode))
                        .tags(Tags.of("name", jobExecutionEventInfo.getJobName()))
                        .strongReference(true)
                        .register(registry);
            } else {
                log.info(source + "_" + LightminMetricUtils.LightminMetrics.LIGHTMIN_JOB_STATUS + " is unknown by Lightmin Context and is therefore not created.");
            }
        } else {
            // We only want to update the Status when the Job Exited the status
        }
    }

}
