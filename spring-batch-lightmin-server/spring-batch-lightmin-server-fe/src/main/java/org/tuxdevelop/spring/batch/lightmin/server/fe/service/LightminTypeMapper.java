package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.*;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientInformation;
import org.tuxdevelop.spring.batch.lightmin.client.api.feature.ApiFeature;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.application.ApplicationApiFeatureModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.JobIncremeterTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.TaskExecutorTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.ListenerStatusModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.ListenerTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.SchedulerStatusModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.SchedulerTypeModel;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class LightminTypeMapper {

    private LightminTypeMapper() {

    }

    public static List<SchedulerTypeModel> getSchedulerTypeModels(final LightminClientInformation lightminClientInformation) {
        final List<SchedulerTypeModel> schedulerTypes = new ArrayList<>();
        if (lightminClientInformation != null && lightminClientInformation.getSupportedSchedulerTypes() != null) {
            for (final JobSchedulerType supportedSchedulerType : lightminClientInformation.getSupportedSchedulerTypes()) {
                final SchedulerTypeModel model =
                        new SchedulerTypeModel(SchedulerTypeModel.map(supportedSchedulerType));
                schedulerTypes.add(model);
            }
        } else {
            log.debug("nothing to map");
        }
        return schedulerTypes;
    }

    public static List<SchedulerStatusModel> getSchedulerStatusModels(final LightminClientInformation lightminClientInformation) {
        final List<SchedulerStatusModel> schedulerStatusModels = new ArrayList<>();
        if (lightminClientInformation != null && lightminClientInformation.getSupportedSchedulerStatuses() != null) {
            for (final SchedulerStatus status : lightminClientInformation.getSupportedSchedulerStatuses()) {
                final SchedulerStatusModel model =
                        new SchedulerStatusModel(SchedulerStatusModel.map(status));
                schedulerStatusModels.add(model);
            }
        } else {
            log.debug("nothing to map");
        }
        return schedulerStatusModels;
    }

    public static List<ListenerStatusModel> getListenerStatusModels(final LightminClientInformation lightminClientInformation) {
        final List<ListenerStatusModel> listenerStatusModels = new ArrayList<>();
        if (lightminClientInformation != null && lightminClientInformation.getSupportedListenerStatuses() != null) {
            for (final ListenerStatus status : lightminClientInformation.getSupportedListenerStatuses()) {
                final ListenerStatusModel model = new ListenerStatusModel(ListenerStatusModel.map(status));
                listenerStatusModels.add(model);
            }
        } else {
            log.debug("nothing to map");
        }
        return listenerStatusModels;
    }

    public static List<ListenerTypeModel> getListenerTypeModels(final LightminClientInformation lightminClientInformation) {
        final List<ListenerTypeModel> listenerTypeModels = new ArrayList<>();
        if (lightminClientInformation != null && lightminClientInformation.getSupportedJobListenerTypes() != null) {
            for (final JobListenerType type : lightminClientInformation.getSupportedJobListenerTypes()) {
                final ListenerTypeModel model = new ListenerTypeModel(ListenerTypeModel.map(type));
                listenerTypeModels.add(model);
            }
        } else {
            log.debug("nothing to map");
        }
        return listenerTypeModels;
    }

    public static List<TaskExecutorTypeModel> getTaskExecutorTypeModels(final LightminClientInformation lightminClientInformation) {
        final List<TaskExecutorTypeModel> taskExecutorTypeModels = new ArrayList<>();
        if (lightminClientInformation != null && lightminClientInformation.getSupportedTaskExecutorTypes() != null) {
            for (final TaskExecutorType type : lightminClientInformation.getSupportedTaskExecutorTypes()) {
                final TaskExecutorTypeModel model = new TaskExecutorTypeModel(TaskExecutorTypeModel.map(type));
                taskExecutorTypeModels.add(model);
            }
        } else {
            log.debug("LightminClientInformation is null, nothing to map");
        }
        return taskExecutorTypeModels;
    }

    public static List<ApplicationApiFeatureModel> getApplicationApiFeatureModels(final LightminClientInformation lightminClientInformation) {
        final List<ApplicationApiFeatureModel> applicationApiFeatureModels = new ArrayList<>();
        if (lightminClientInformation != null && lightminClientInformation.getSupportedApiFeatures() != null) {
            for (final ApiFeature supportedApiFeature : lightminClientInformation.getSupportedApiFeatures()) {
                final ApplicationApiFeatureModel model = new ApplicationApiFeatureModel();
                model.setApiFeature(supportedApiFeature);
                applicationApiFeatureModels.add(model);
            }
        } else {
            log.debug("LightminClientInformation is null, nothing to map");
        }
        return applicationApiFeatureModels;
    }

    public static List<JobIncremeterTypeModel> getJobIncremeterTypeModels(final List<JobIncrementer> jobIncrementers) {
        final List<JobIncremeterTypeModel> jobIncremeterTypeModels = new ArrayList<>();
        if (jobIncrementers != null) {
            for (final JobIncrementer jobIncrementer : jobIncrementers) {
                final JobIncremeterTypeModel model = new JobIncremeterTypeModel(JobIncremeterTypeModel.map(jobIncrementer));
                jobIncremeterTypeModels.add(model);
            }
        } else {
            log.debug("IobIncrementers are null, nothing to map");
        }
        return jobIncremeterTypeModels;
    }

}
