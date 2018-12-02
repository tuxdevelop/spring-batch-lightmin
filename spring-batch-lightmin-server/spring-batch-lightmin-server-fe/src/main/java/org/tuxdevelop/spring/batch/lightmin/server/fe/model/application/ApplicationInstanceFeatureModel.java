package org.tuxdevelop.spring.batch.lightmin.server.fe.model.application;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.TaskExecutorTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.ListenerStatusModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.ListenerTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.SchedulerStatusModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.SchedulerTypeModel;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApplicationInstanceFeatureModel {

    private List<SchedulerTypeModel> supportedSchedulers;
    private List<SchedulerStatusModel> supportedSchedulerStatus;
    private List<ListenerTypeModel> supportedListeners;
    private List<ListenerStatusModel> supportedListenerStatus;
    private List<ApplicationApiFeatureModel> applicationApiFeatures;
    private List<TaskExecutorTypeModel> supportedTaskExecutorType;

    public ApplicationInstanceFeatureModel() {
        this.supportedSchedulers = new ArrayList<>();
        this.supportedSchedulers = new ArrayList<>();
        this.supportedListeners = new ArrayList<>();
        this.supportedListenerStatus = new ArrayList<>();
        this.applicationApiFeatures = new ArrayList<>();
        this.supportedTaskExecutorType = new ArrayList<>();
    }

}
