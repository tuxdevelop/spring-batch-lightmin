package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientInformation;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.application.ApplicationClusterModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.application.ApplicationInstanceFeatureModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.application.ApplicationInstanceModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch.RegisteredJobModel;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import java.util.*;

public class LightminClientApplicationFeService extends CommonFeService {

    private final RegistrationBean registrationBean;

    public LightminClientApplicationFeService(final RegistrationBean registrationBean) {
        super(registrationBean);
        this.registrationBean = registrationBean;
    }

    public List<ApplicationClusterModel> getApplicationClusterModels() {
        final List<ApplicationClusterModel> clusterModels = new ArrayList<>();
        final Map<String, Set<LightminClientApplication>> clusters = this.registrationBean.findAllasMap();
        for (final Map.Entry<String, Set<LightminClientApplication>> cluster : clusters.entrySet()) {
            final ApplicationClusterModel applicationClusterModel = new ApplicationClusterModel();
            applicationClusterModel.setName(cluster.getKey());
            applicationClusterModel.addAll(this.mapApplicationInstanceModels(cluster.getValue()));
            clusterModels.add(applicationClusterModel);
        }
        return clusterModels;
    }

    public ApplicationInstanceModel get(final String applicationId) {
        final LightminClientApplication instance = this.registrationBean.findById(applicationId);
        return this.mapApplicationInstance(instance);
    }

    public void removeApplicationInstance(final String applicationInstanceId) {
        this.registrationBean.deleteRegistration(applicationInstanceId);
    }

    private Set<ApplicationInstanceModel> mapApplicationInstanceModels(
            final Collection<LightminClientApplication> applications) {
        final Set<ApplicationInstanceModel> applicationInstanceModels = new HashSet<>();
        for (final LightminClientApplication application : applications) {
            final ApplicationInstanceModel applicationInstanceModel = this.mapApplicationInstance(application);
            applicationInstanceModels.add(applicationInstanceModel);
        }
        return applicationInstanceModels;
    }

    private ApplicationInstanceModel mapApplicationInstance(final LightminClientApplication lightminClientApplication) {
        final ApplicationInstanceModel instance = new ApplicationInstanceModel();
        instance.setId(lightminClientApplication.getId());
        instance.setName(lightminClientApplication.getName());
        instance.setServiceUrl(lightminClientApplication.getServiceUrl());
        instance.setStatus(lightminClientApplication.getLightminClientApplicationStatus().getStatus());
        instance.setFeature(this.mapFeatureModel(lightminClientApplication.getLightminClientInformation()));
        instance.setExternalLinks(lightminClientApplication.getLightminClientInformation().getExternalLinks());
        instance.setHealthUrl(lightminClientApplication.getHealthUrl());
        instance.setManagementUrl(lightminClientApplication.getManagementUrl());
        instance.setRegisteredJobs(this.mapRegisteredJobModels(lightminClientApplication.getLightminClientInformation()));
        return instance;
    }

    private ApplicationInstanceFeatureModel mapFeatureModel(final LightminClientInformation information) {
        final ApplicationInstanceFeatureModel model = new ApplicationInstanceFeatureModel();
        model.setSupportedListeners(LightminTypeMapper.getListenerTypeModels(information));
        model.setSupportedListenerStatus(LightminTypeMapper.getListenerStatusModels(information));
        model.setSupportedSchedulers(LightminTypeMapper.getSchedulerTypeModels(information));
        model.setSupportedSchedulerStatus(LightminTypeMapper.getSchedulerStatusModels(information));
        model.setSupportedTaskExecutorType(LightminTypeMapper.getTaskExecutorTypeModels(information));
        model.setApplicationApiFeatures(LightminTypeMapper.getApplicationApiFeatureModels(information));
        return model;
    }

    private List<RegisteredJobModel> mapRegisteredJobModels(final LightminClientInformation lightminClientInformation) {
        final List<RegisteredJobModel> registeredJobModels = new ArrayList<>();
        for (final String registeredJob : lightminClientInformation.getRegisteredJobs()) {
            final RegisteredJobModel model = new RegisteredJobModel();
            model.setJobName(registeredJob);
            registeredJobModels.add(model);
        }
        return registeredJobModels;
    }

}
