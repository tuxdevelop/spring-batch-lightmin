package org.tuxdevelop.spring.batch.lightmin.client.api;


import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.*;
import org.tuxdevelop.spring.batch.lightmin.client.api.feature.ApiFeature;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Data
public class LightminClientApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String managementUrl;
    private String healthUrl;
    private String serviceUrl;
    private LightminClientApplicationStatus lightminClientApplicationStatus;
    private LightminClientInformation lightminClientInformation;

    public static LightminClientApplication createApplication(final List<String> jobNames,
                                                              final LightminClientProperties lightminClientProperties) {
        final LightminClientInformation lightminClientInformation = new LightminClientInformation();
        lightminClientInformation.setRegisteredJobs(jobNames);
        lightminClientInformation.setSupportedJobIncrementers(Arrays.asList(JobIncrementer.values()));
        lightminClientInformation.setSupportedSchedulerTypes(Arrays.asList(JobSchedulerType.values()));
        lightminClientInformation.setSupportedSchedulerStatuses(Arrays.asList(SchedulerStatus.values()));
        lightminClientInformation.setSupportedTaskExecutorTypes(Arrays.asList(TaskExecutorType.values()));
        lightminClientInformation.setSupportedJobListenerTypes(Arrays.asList(JobListenerType.values()));
        lightminClientInformation.setSupportedApiFeatures(Arrays.asList(ApiFeature.values()));
        lightminClientInformation.setSupportedListenerStatuses(Arrays.asList(ListenerStatus.values()));
        lightminClientInformation.setExternalLinks(lightminClientProperties.getExternalLinks());

        final LightminClientApplication lightminClientApplication = new LightminClientApplication();
        lightminClientApplication.setHealthUrl(lightminClientProperties.getHealthUrl());
        lightminClientApplication.setName(lightminClientProperties.getName());
        lightminClientApplication.setServiceUrl(lightminClientProperties.getServiceUrl());
        lightminClientApplication.setManagementUrl(lightminClientProperties.getManagementUrl());
        lightminClientApplication.setLightminClientInformation(lightminClientInformation);
        return lightminClientApplication;
    }

}
