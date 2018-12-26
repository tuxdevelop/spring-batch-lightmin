package org.tuxdevelop.spring.batch.lightmin.listener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminCoreConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.domain.JobListenerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.ListenerStatus;

/**
 * @author Marcel Becker
 * @see FolderListener
 * @since 0.3
 */
public abstract class AbstractListener implements Listener {

    protected StandardIntegrationFlow integrationFlow;
    protected JobConfiguration jobConfiguration;
    protected JobListenerConfiguration jobListenerConfiguration;
    protected JobIncrementer jobIncrementer;
    protected Job job;
    protected JobParameters jobParameters;
    protected JobLauncher jobLauncher;
    protected ApplicationEventPublisher applicationEventPublisher;
    protected SpringBatchLightminCoreConfigurationProperties springBatchLightminCoreConfigurationProperties;
    protected ConfigurableApplicationContext applicationContext;
    protected ListenerStatus listenerStatus;

    @Override
    public void start() {
        this.applicationContext = new AnnotationConfigApplicationContext(FlowConfiguration.class);
        this.applicationContext.getBeanFactory().registerSingleton("integrationFlow", this.integrationFlow);
        this.applicationContext.getBeanFactory().initializeBean(this.integrationFlow, "integrationFlow");
        this.applicationContext.start();
        this.listenerStatus = ListenerStatus.ACTIVE;
    }

    @Override
    public void stop() {
        if (this.applicationContext != null) {
            this.applicationContext.stop();
        }
        this.listenerStatus = ListenerStatus.STOPPED;
    }

    @Override
    public ListenerStatus getListenerStatus() {
        return this.listenerStatus;
    }


    protected void attachJobIncrementer() {
        if (this.jobParameters == null) {
            this.jobParameters = new JobParametersBuilder().toJobParameters();
        }
        if (JobIncrementer.DATE.equals(this.jobIncrementer)) {
            final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder(this.jobParameters);
            this.jobParameters = jobParametersBuilder.addLong(JobIncrementer.DATE.getIncrementerIdentifier(), System.currentTimeMillis()).toJobParameters();
        }
    }

    protected void assertConstructor() {
        assert this.jobConfiguration != null : "jobConfiguration must not be null";
        assert this.jobListenerConfiguration != null : "jobListenerConfiguration must not be null";
        assert this.jobIncrementer != null : "jobIncremeter must not be null";
        assert this.job != null : "job must not be null";
        assert this.jobLauncher != null : "jobLauncher must not be null";
    }

    @Configuration
    @EnableIntegration
    static class FlowConfiguration {

    }

}

