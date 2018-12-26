package org.tuxdevelop.spring.batch.lightmin.client.api;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.*;
import org.tuxdevelop.spring.batch.lightmin.client.api.feature.ApiFeature;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Data
public class LightminClientInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<JobSchedulerType> supportedSchedulerTypes;
    private List<SchedulerStatus> supportedSchedulerStatuses;
    private List<TaskExecutorType> supportedTaskExecutorTypes;
    private List<JobIncrementer> supportedJobIncrementers;
    private List<JobListenerType> supportedJobListenerTypes;
    private List<ListenerStatus> supportedListenerStatuses;
    private List<ApiFeature> supportedApiFeatures;
    private List<String> registeredJobs;
    private Map<String, String> externalLinks;

    public LightminClientInformation() {

        this.supportedSchedulerTypes = new ArrayList<>();
        this.supportedSchedulerStatuses = new ArrayList<>();
        this.supportedTaskExecutorTypes = new ArrayList<>();
        this.supportedJobIncrementers = new ArrayList<>();
        this.supportedJobListenerTypes = new ArrayList<>();
        this.supportedListenerStatuses = new ArrayList<>();
        this.supportedApiFeatures = new ArrayList<>();
        this.registeredJobs = new ArrayList<>();
        this.externalLinks = new HashMap<>();
    }
}
