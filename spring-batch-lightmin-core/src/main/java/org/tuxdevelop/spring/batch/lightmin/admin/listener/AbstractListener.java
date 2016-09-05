package org.tuxdevelop.spring.batch.lightmin.admin.listener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobListenerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.ListenerStatus;

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
    protected ConfigurableApplicationContext applicationContext;
    protected ListenerStatus listenerStatus;

    public void start() {
        applicationContext = new AnnotationConfigApplicationContext(FlowConfiguration.class);
        applicationContext.getBeanFactory().registerSingleton("integrationFflow", integrationFlow);
        applicationContext.getBeanFactory().initializeBean(integrationFlow, "integrationFlow");
        applicationContext.start();
        listenerStatus = ListenerStatus.ACTIVE;
    }

    public void stop() {
        if (applicationContext != null) {
            applicationContext.stop();
        }
        listenerStatus = ListenerStatus.STOPPED;
    }

    public ListenerStatus getListenerStatus() {
        return listenerStatus;
    }


    protected void attachJobIncrementer() {
        if (jobParameters == null) {
            jobParameters = new JobParametersBuilder().toJobParameters();
        }
        if (JobIncrementer.DATE.equals(jobIncrementer)) {
            final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder(jobParameters);
            jobParameters = jobParametersBuilder.addLong(JobIncrementer.DATE.getIncrementerIdentifier(), System.currentTimeMillis()).toJobParameters();
        }
    }

    protected void assertConstructor() {
        assert jobConfiguration != null : "jobConfiguration must not be null";
        assert jobListenerConfiguration != null : "jobListenerConfiguration must not be null";
        assert jobIncrementer != null : "jobIncremeter must not be null";
        assert job != null : "job must not be null";
        assert jobLauncher != null : "jobLauncher must not be null";
    }

    @Configuration
    @EnableIntegration
    static class FlowConfiguration {

    }

}

