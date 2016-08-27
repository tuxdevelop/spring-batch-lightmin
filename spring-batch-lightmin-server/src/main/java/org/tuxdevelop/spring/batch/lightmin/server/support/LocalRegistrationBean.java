package org.tuxdevelop.spring.batch.lightmin.server.support;


import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.SchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.TaskExecutorType;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplicationStatus;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientInformation;
import org.tuxdevelop.spring.batch.lightmin.server.event.LightminClientApplicationRegisteredEvent;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class LocalRegistrationBean implements ApplicationListener<ContextRefreshedEvent>, ApplicationEventPublisherAware {

    private final LightminApplicationRepository lightminApplicationRepository;
    private final Environment environment;
    private final JobRegistry jobRegistry;

    private ApplicationEventPublisher applicationEventPublisher;

    public LocalRegistrationBean(final LightminApplicationRepository lightminApplicationRepository, final Environment environment, final JobRegistry jobRegistry) {
        this.lightminApplicationRepository = lightminApplicationRepository;
        this.environment = environment;
        this.jobRegistry = jobRegistry;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        final Collection<String> jobNames = jobRegistry.getJobNames();
        final LightminClientInformation lightminClientInformation = new LightminClientInformation();
        lightminClientInformation.setRegisteredJobs(new LinkedList<>(jobNames));
        lightminClientInformation.setSupportedJobIncrementers(Arrays.asList(JobIncrementer.values()));
        lightminClientInformation.setSupportedSchedulerStatuses(Arrays.asList(SchedulerStatus.values()));
        lightminClientInformation.setSupportedSchedulerTypes(Arrays.asList(JobSchedulerType.values()));
        lightminClientInformation.setSupportedTaskExecutorTypes(Arrays.asList(TaskExecutorType.values()));
        final LightminClientApplication lightminClientApplication = new LightminClientApplication();
        final String applicationName = environment.getProperty("spring.application.name");
        lightminClientApplication.setName(applicationName);
        lightminClientApplication.setLightminClientApplicationStatus(LightminClientApplicationStatus.ofUp());
        lightminClientApplication.setHealthUrl("http://localhost");
        lightminClientApplication.setLightminClientInformation(lightminClientInformation);
        final String applicationId = ApplicationUrlIdGenerator.generateId(lightminClientApplication);
        lightminClientApplication.setId(applicationId);
        lightminApplicationRepository.save(lightminClientApplication);
        applicationEventPublisher.publishEvent(new LightminClientApplicationRegisteredEvent(lightminClientApplication));
    }

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
