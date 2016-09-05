package org.tuxdevelop.spring.batch.lightmin.client.api;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
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
    private List<String> registeredJobs;
    private Map<String, String> externalLinks;

    public LightminClientInformation() {

        this.supportedSchedulerTypes = new LinkedList<>();
        this.supportedSchedulerStatuses = new LinkedList<>();
        this.supportedTaskExecutorTypes = new LinkedList<>();
        this.supportedJobIncrementers = new LinkedList<>();
        this.supportedJobListenerTypes = new LinkedList<>();
        this.supportedListenerStatuses = new LinkedList<>();
        this.registeredJobs = new LinkedList<>();
        this.externalLinks = new HashMap<>();
    }
}
