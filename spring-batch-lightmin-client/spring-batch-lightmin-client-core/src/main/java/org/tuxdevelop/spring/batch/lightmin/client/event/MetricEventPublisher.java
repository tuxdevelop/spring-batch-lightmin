package org.tuxdevelop.spring.batch.lightmin.client.event;

import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;

public interface MetricEventPublisher {


    void publishEvent(JobExecutionEventInfo jobExecutionEventInfo);

    void publishEvent(StepExecutionEventInfo stepExecutionEventInfo);

}
