package org.tuxdevelop.spring.batch.lightmin.admin.event.listener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.tuxdevelop.spring.batch.lightmin.admin.event.JobExecutionEvent;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;

@Slf4j
public class JobExecutionFinishedJobExecutionListener implements JobExecutionListener, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;
    private final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;

    public JobExecutionFinishedJobExecutionListener(final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties) {
        this.springBatchLightminConfigurationProperties = springBatchLightminConfigurationProperties;
    }

    @Override
    public void beforeJob(final JobExecution jobExecution) {
        log.debug("Started Listening on JobExecution {}", jobExecution);
    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
        final JobExecutionEvent jobExecutionEvent = new JobExecutionEvent(jobExecution, this.springBatchLightminConfigurationProperties.getApplicationName());
        this.applicationEventPublisher.publishEvent(jobExecutionEvent);
    }

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
