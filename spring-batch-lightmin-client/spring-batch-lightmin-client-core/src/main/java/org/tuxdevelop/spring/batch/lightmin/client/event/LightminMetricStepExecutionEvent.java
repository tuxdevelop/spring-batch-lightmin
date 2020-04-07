package org.tuxdevelop.spring.batch.lightmin.client.event;

import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;

public class LightminMetricStepExecutionEvent extends LightminStepExecutionEvent {

    private static final long serialVersionUID = 1L;

    public LightminMetricStepExecutionEvent(final StepExecutionEventInfo source) {
        super(source);
    }
}
