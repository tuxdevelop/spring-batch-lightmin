package org.tuxdevelop.spring.batch.lightmin.server.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.domain.JobExecutionPublishEvent;
import org.tuxdevelop.spring.batch.lightmin.server.domain.StepExecutionPublishEvent;
import org.tuxdevelop.spring.batch.lightmin.server.repository.JobExecutionEventRepository;
import org.tuxdevelop.spring.batch.lightmin.service.MetricServiceBean;
import org.tuxdevelop.spring.batch.lightmin.utils.LightminMetricSource;

import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public class EventServiceBean implements EventService, ApplicationEventPublisherAware {

    private final JobExecutionEventRepository jobExecutionEventRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    public EventServiceBean(final JobExecutionEventRepository jobExecutionEventRepository) {
        this.jobExecutionEventRepository = jobExecutionEventRepository;
    }


    @Override
    public void handleJobExecutionEvent(final JobExecutionEventInfo jobExecutionEventInfo) {
        this.jobExecutionEventRepository.save(jobExecutionEventInfo);
        this.applicationEventPublisher.publishEvent(new JobExecutionPublishEvent(jobExecutionEventInfo));
    }

    @Override
    public void handleStepExecutionEvent(final StepExecutionEventInfo stepExecutionEventInfo) {
        this.applicationEventPublisher.publishEvent(new StepExecutionPublishEvent(stepExecutionEventInfo));
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

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
