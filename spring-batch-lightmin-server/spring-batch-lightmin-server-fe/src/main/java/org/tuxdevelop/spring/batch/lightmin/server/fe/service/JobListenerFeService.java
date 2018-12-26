package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.*;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.TaskExecutorTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.ListenerJobConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.MapJobListenerConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.JobListenerModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.ListenerStatusModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.ListenerTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.AdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import java.util.Map;


@Slf4j
public class JobListenerFeService extends CommonJobConfigurationFeService {

    private final AdminServerService adminServerService;

    public JobListenerFeService(final RegistrationBean registrationBean, final AdminServerService adminServerService) {
        super(registrationBean);
        this.adminServerService = adminServerService;
    }


    public MapJobListenerConfigurationModel getMapJobConfigurationModel(final String applicationInstanceId) {

        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);

        final Map<String, JobConfigurations> jobConfigurations =
                this.adminServerService.getJobConfigurationsMap(lightminClientApplication);


        final MapJobListenerConfigurationModel mapJobListenerConfigurationModel = new MapJobListenerConfigurationModel();
        for (final Map.Entry<String, JobConfigurations> entry : jobConfigurations.entrySet()) {
            for (final JobConfiguration jobConfiguration : entry.getValue().getJobConfigurations()) {
                if (jobConfiguration.getJobListenerConfiguration() != null) {
                    final ListenerJobConfigurationModel model = this.map(jobConfiguration);
                    mapJobListenerConfigurationModel.add(entry.getKey(), model);
                } else {
                    log.trace("No Scheduler configuration, nothing to map");
                }
            }
        }

        return mapJobListenerConfigurationModel;

    }

    public ListenerJobConfigurationModel getJobConfigurationModel(final Long jobConfigurationId,
                                                                  final String applicationInstanceId) {

        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);

        final JobConfiguration jobConfiguration =
                this.adminServerService.getJobConfiguration(jobConfigurationId, lightminClientApplication);

        return this.map(jobConfiguration);

    }

    public void startListener(final Long jobConfigurationId, final String applicationInstanceId) {

        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);

        this.adminServerService.startJobConfigurationScheduler(jobConfigurationId, lightminClientApplication);

    }

    public void stopListener(final Long jobConfigurationId, final String applicationInstanceId) {

        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);

        this.adminServerService.stopJobConfigurationScheduler(jobConfigurationId, lightminClientApplication);

    }

    public void deleteListenerConfiguration(final Long jobConfigurationId, final String applicationInstanceId) {
        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);
        this.adminServerService.deleteJobConfiguration(jobConfigurationId, lightminClientApplication);
    }

    public void addListenerConfiguration(final ListenerJobConfigurationModel listenerModel,
                                         final String applicationInstanceId) {
        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);
        final JobConfiguration jobConfiguration = this.map(listenerModel);
        this.adminServerService.saveJobConfiguration(jobConfiguration, lightminClientApplication);
    }

    public void updateListenerConfiguration(final ListenerJobConfigurationModel listenerModel,
                                            final String applicationInstanceId) {
        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);
        final JobConfiguration jobConfiguration = this.map(listenerModel);
        this.adminServerService.updateJobConfiguration(jobConfiguration, lightminClientApplication);
    }

    //Mapper

    //to model

    private ListenerJobConfigurationModel map(final JobConfiguration jobConfiguration) {

        final JobListenerModel jobListenerModel = new JobListenerModel();

        jobListenerModel.setFilePattern(jobConfiguration.getJobListenerConfiguration().getFilePattern());
        jobListenerModel.setPollerPeriod(jobConfiguration.getJobListenerConfiguration().getPollerPeriod());
        jobListenerModel.setSourceFolder(jobConfiguration.getJobListenerConfiguration().getSourceFolder());
        jobListenerModel.setStatus(ListenerStatusModel.map(jobConfiguration.getJobListenerConfiguration().getListenerStatus()).name());
        jobListenerModel.setStatusRead(new ListenerStatusModel(ListenerStatusModel.map(jobConfiguration.getJobListenerConfiguration().getListenerStatus())));
        jobListenerModel.setType(ListenerTypeModel.map(jobConfiguration.getJobListenerConfiguration().getJobListenerType()).name());
        jobListenerModel.setTypeRead(new ListenerTypeModel(ListenerTypeModel.map(jobConfiguration.getJobListenerConfiguration().getJobListenerType())));
        jobListenerModel.setTaskExecutor(TaskExecutorTypeModel.map(jobConfiguration.getJobListenerConfiguration().getTaskExecutorType()).name());
        jobListenerModel.setTaskExecutorRead(new TaskExecutorTypeModel(TaskExecutorTypeModel.map(jobConfiguration.getJobListenerConfiguration().getTaskExecutorType())));


        final ListenerJobConfigurationModel model = new ListenerJobConfigurationModel();
        this.mapCommonJobConfigurationModel(model, jobConfiguration);
        model.setConfig(jobListenerModel);

        return model;
    }

    //from model
    JobConfiguration map(final ListenerJobConfigurationModel listenerModel) {

        final JobConfiguration jobConfiguration = new JobConfiguration();

        final JobListenerConfiguration jobListenerConfiguration = new JobListenerConfiguration();

        jobListenerConfiguration.setSourceFolder(listenerModel.getConfig().getSourceFolder());
        jobListenerConfiguration.setFilePattern(listenerModel.getConfig().getFilePattern());
        jobListenerConfiguration.setPollerPeriod(listenerModel.getConfig().getPollerPeriod());
        jobListenerConfiguration.setListenerStatus(this.mapListenerStatus(listenerModel.getConfig().getStatus()));
        jobListenerConfiguration.setJobListenerType(this.mapListenerType(listenerModel.getConfig().getType()));
        jobListenerConfiguration.setTaskExecutorType(this.mapTaskExecutor(listenerModel.getConfig().getTaskExecutor()));

        jobConfiguration.setJobName(listenerModel.getJobName());
        jobConfiguration.setJobConfigurationId(listenerModel.getId());
        jobConfiguration.setJobIncrementer(this.mapIncrementer(listenerModel.getIncrementer()));
        jobConfiguration.setJobParameters(this.mapJobParameters(listenerModel.getParameters()));
        jobConfiguration.setJobListenerConfiguration(jobListenerConfiguration);

        return jobConfiguration;
    }


    private ListenerStatus mapListenerStatus(final String status) {
        return ListenerStatus.getByValue(status);
    }

    private JobListenerType mapListenerType(final String schedulerType) {
        return JobListenerType.valueOf(schedulerType);
    }

}