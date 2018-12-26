package org.tuxdevelop.spring.batch.lightmin.event.listener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminCoreConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.event.JobExecutionEvent;

@Slf4j
public class JobExecutionFinishedJobExecutionListener implements JobExecutionListener, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;
    private final SpringBatchLightminCoreConfigurationProperties springBatchLightminCoreConfigurationProperties;

    public JobExecutionFinishedJobExecutionListener(
            final SpringBatchLightminCoreConfigurationProperties springBatchLightminCoreConfigurationProperties) {
        this.springBatchLightminCoreConfigurationProperties = springBatchLightminCoreConfigurationProperties;
    }

    @Override
    public void beforeJob(final JobExecution jobExecution) {
        log.debug("Started Listening on JobExecution {}", jobExecution);
    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
        final JobExecutionEvent jobExecutionEvent =
                new JobExecutionEvent(jobExecution, this.springBatchLightminCoreConfigurationProperties.getApplicationName());
        this.applicationEventPublisher.publishEvent(jobExecutionEvent);
    }

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
