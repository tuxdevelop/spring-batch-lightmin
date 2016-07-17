package org.tuxdevelop.spring.batch.lightmin.client.api;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.SchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.TaskExecutorType;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
public class LightminClientInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<JobSchedulerType> supportedSchedulerTypes;
    private List<SchedulerStatus> supportedSchedulerStatuses;
    private List<TaskExecutorType> supportedTaskExecutorTypes;
    private List<JobIncrementer> supportedJobIncrementers;
    private List<String> registeredJobs;

    public LightminClientInformation() {

        this.supportedSchedulerTypes = new LinkedList<>();
        this.supportedSchedulerStatuses = new LinkedList<>();
        this.supportedTaskExecutorTypes = new LinkedList<>();
        this.supportedJobIncrementers = new LinkedList<>();
        this.registeredJobs = new LinkedList<>();
    }
}
