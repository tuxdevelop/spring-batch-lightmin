package org.tuxdevelop.spring.batch.lightmin.service;

import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.utils.LightminMetricSource;

public interface MetricService {

    void measureStepExecution(LightminMetricSource source, StepExecutionEventInfo stepExecutionEventInfo);

    void measureJobExecution(LightminMetricSource source, JobExecutionEventInfo jobExecutionEventInfo);
}
