package org.tuxdevelop.spring.batch.lightmin.server.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JobExecutionEventRepository;
import org.tuxdevelop.spring.batch.lightmin.service.MetricServiceBean;
import org.tuxdevelop.spring.batch.lightmin.utils.LightminMetricSource;

import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public class EventServiceBean implements EventService {

    private final JobExecutionEventRepository jobExecutionEventRepository;

    private final MetricServiceBean metricServiceBean;

    public EventServiceBean(final JobExecutionEventRepository jobExecutionEventRepository, final MetricServiceBean metricServiceBean) {
        this.jobExecutionEventRepository = jobExecutionEventRepository;
        this.metricServiceBean = metricServiceBean;
    }

    @Override
    public void handleJobExecutionEvent(final JobExecutionEventInfo jobExecutionEventInfo) {
        this.jobExecutionEventRepository.save(jobExecutionEventInfo);
    }

    @Override
    public void handleMetricEvent(JobExecutionEventInfo jobExecutionEventInfo) {
        this.metricServiceBean.measureJobExecution(LightminMetricSource.SERVER, jobExecutionEventInfo);
    }

    @Override
    public void handleMetricEvent(StepExecutionEventInfo stepExecutionEventInfo) {
        this.metricServiceBean.measureStepExecution(LightminMetricSource.SERVER, stepExecutionEventInfo);
    }

    @Override
    public List<JobExecutionEventInfo> getAllJobExecutionEvents(final int start, final int count) {
        return this.jobExecutionEventRepository.findAll(start, count);
    }

    @Override
    public List<JobExecutionEventInfo> getAllJobExecutionEventsByExitStatus(
            final ExitStatus exitStatus,
            final int start,
            final int count) {
        return this.jobExecutionEventRepository.finalByExitStatus(exitStatus, start, count);
    }

    @Override
    public int getJobExecutionEventInfoCount() {
        return this.jobExecutionEventRepository.getTotalCount();
    }
}
