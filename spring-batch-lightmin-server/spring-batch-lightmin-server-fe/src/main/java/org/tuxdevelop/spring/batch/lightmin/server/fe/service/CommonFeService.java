package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ApplicationContextModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.JobIncremeterTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.TaskExecutorTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.ListenerStatusModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.ListenerTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.SchedulerStatusModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.SchedulerTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import java.util.List;

public abstract class CommonFeService {

    private final RegistrationBean registrationBean;

    protected CommonFeService(final RegistrationBean registrationBean) {
        this.registrationBean = registrationBean;
    }

    public String getApplicationNameById(final String instanceId) {
        return this.registrationBean.getApplicationNameById(instanceId);
    }

    public ApplicationContextModel getApplicationContextModel(final String instanceId) {
        final ApplicationContextModel model = new ApplicationContextModel();
        final LightminClientApplication lightminClientApplication = this.findLightminClientApplicatonById(instanceId);
        final List<JobIncremeterTypeModel> jobIncremeters =
                LightminTypeMapper.getJobIncremeterTypeModels(
                        lightminClientApplication.getLightminClientInformation().getSupportedJobIncrementers());
        final List<SchedulerTypeModel> schedulerTypes =
                LightminTypeMapper.getSchedulerTypeModels(lightminClientApplication.getLightminClientInformation());
        final List<SchedulerStatusModel> schedulerStatus =
                LightminTypeMapper.getSchedulerStatusModels(lightminClientApplication.getLightminClientInformation());
        final List<TaskExecutorTypeModel> taskExecutorTypes =
                LightminTypeMapper.getTaskExecutorTypeModels(lightminClientApplication.getLightminClientInformation());
        final List<ListenerTypeModel> listenerTypes =
                LightminTypeMapper.getListenerTypeModels(lightminClientApplication.getLightminClientInformation());
        final List<ListenerStatusModel> listenerStatus =
                LightminTypeMapper.getListenerStatusModels(lightminClientApplication.getLightminClientInformation());
        model.setApplicationInstanceId(instanceId);
        model.setApplicationName(lightminClientApplication.getName());
        model.setRegisteredJobs(lightminClientApplication.getLightminClientInformation().getRegisteredJobs());
        model.setJobIncrementers(jobIncremeters);
        model.setSchedulerTypes(schedulerTypes);
        model.setSchedulerStatus(schedulerStatus);
        model.setTaskExecutorTypes(taskExecutorTypes);
        model.setListenerTypes(listenerTypes);
        model.setListenerStatus(listenerStatus);
        return model;
    }

    public LightminClientApplication findLightminClientApplicatonById(final String applicationInstanceId) {
        return this.registrationBean.findById(applicationInstanceId);
    }

    public String getApplicationInstanceIdByName(final String name) {
        return this.registrationBean.getIdByApplicationName(name);
    }
}
