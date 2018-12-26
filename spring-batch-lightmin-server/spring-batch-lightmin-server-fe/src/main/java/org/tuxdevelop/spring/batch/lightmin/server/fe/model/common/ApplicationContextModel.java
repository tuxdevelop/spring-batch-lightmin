package org.tuxdevelop.spring.batch.lightmin.server.fe.model.common;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.ListenerStatusModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.ListenerTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.SchedulerStatusModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.SchedulerTypeModel;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApplicationContextModel {

    private String applicationInstanceId;
    private String applicationName;
    private String jobName;
    private Long jobExecutionId;
    private List<String> registeredJobs;
    private List<JobIncremeterTypeModel> jobIncrementers;
    private List<SchedulerTypeModel> schedulerTypes;
    private List<SchedulerStatusModel> schedulerStatus;
    private List<TaskExecutorTypeModel> taskExecutorTypes;
    private List<ListenerTypeModel> listenerTypes;
    private List<ListenerStatusModel> listenerStatus;

    public ApplicationContextModel() {
        this.registeredJobs = new ArrayList<>();
        this.jobIncrementers = new ArrayList<>();
        this.schedulerStatus = new ArrayList<>();
        this.taskExecutorTypes = new ArrayList<>();
        this.listenerTypes = new ArrayList<>();
        this.listenerStatus = new ArrayList<>();
    }

}
