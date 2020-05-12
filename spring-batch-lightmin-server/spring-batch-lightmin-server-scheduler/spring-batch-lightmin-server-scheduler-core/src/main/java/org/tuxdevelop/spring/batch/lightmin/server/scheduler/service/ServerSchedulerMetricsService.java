package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import io.micrometer.core.instrument.MeterRegistry;

public class ServerSchedulerMetricsService {

    private final MeterRegistry meterRegistry;

    public ServerSchedulerMetricsService(final MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void addSchedulerExecutionMetric(final Integer executionState) {

    }
}
