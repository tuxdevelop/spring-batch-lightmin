package org.tuxdevelop.spring.batch.lightmin.client.event;

import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;

public class LightminMetricJobExecutionEvent extends LightminJobExecutionEvent {

    public LightminMetricJobExecutionEvent(JobExecutionEventInfo source) {
        super(source);
    }
}
