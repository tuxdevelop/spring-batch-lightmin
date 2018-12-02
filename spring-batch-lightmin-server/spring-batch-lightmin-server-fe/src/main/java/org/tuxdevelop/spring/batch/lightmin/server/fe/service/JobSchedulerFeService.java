package org.tuxdevelop.spring.batch.lightmin.server.fe.service;


import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.*;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.TaskExecutorTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.MapJobSchedulerConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.SchedulerJobConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.JobSchedulerModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.SchedulerStatusModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.SchedulerTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.AdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import java.util.Map;

@Slf4j
public class JobSchedulerFeService extends CommonJobConfigurationFeService {

    private final AdminServerService adminServerService;

    public JobSchedulerFeService(final RegistrationBean registrationBean, final AdminServerService adminServerService) {
        super(registrationBean);
        this.adminServerService = adminServerService;
    }

    public MapJobSchedulerConfigurationModel getMapJobConfigurationModel(final String applicationInstanceId) {

        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);

        final Map<String, JobConfigurations> jobConfigurations =
                this.adminServerService.getJobConfigurationsMap(lightminClientApplication);

        final MapJobSchedulerConfigurationModel mapJobSchedulerConfigurationModel = new MapJobSchedulerConfigurationModel();
        for (final Map.Entry<String, JobConfigurations> entry : jobConfigurations.entrySet()) {
            for (final JobConfiguration jobConfiguration : entry.getValue().getJobConfigurations()) {
                if (jobConfiguration.getJobSchedulerConfiguration() != null) {
                    final SchedulerJobConfigurationModel model = this.map(jobConfiguration);
                    mapJobSchedulerConfigurationModel.add(entry.getKey(), model);
                } else {
                    log.trace("No Scheduler configuration, nothing to map");
                }
            }
        }

        return mapJobSchedulerConfigurationModel;
    }

    public SchedulerJobConfigurationModel getJobConfigurationModel(final Long jobConfigurationId,
                                                                   final String applicationInstanceId) {

        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);

        final JobConfiguration jobConfiguration =
                this.adminServerService.getJobConfiguration(jobConfigurationId, lightminClientApplication);

        return this.map(jobConfiguration);

    }

    public void startScheduler(final Long jobConfigurationId, final String applicationInstanceId) {

        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);

        this.adminServerService.startJobConfigurationScheduler(jobConfigurationId, lightminClientApplication);

    }

    public void stopScheduler(final Long jobConfigurationId, final String applicationInstanceId) {

        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);

        this.adminServerService.stopJobConfigurationScheduler(jobConfigurationId, lightminClientApplication);

    }

    public void deleteSchedulerConfiguration(final Long jobConfigurationId, final String applicationInstanceId) {
        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);
        this.adminServerService.deleteJobConfiguration(jobConfigurationId, lightminClientApplication);
    }

    public void addSchedulerConfiguration(final SchedulerJobConfigurationModel schedulerModel,
                                          final String applicationInstanceId) {
        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);
        final JobConfiguration jobConfiguration = this.map(schedulerModel);
        this.adminServerService.saveJobConfiguration(jobConfiguration, lightminClientApplication);
    }

    public void updateSchedulerConfiguration(final SchedulerJobConfigurationModel schedulerModel,
                                             final String applicationInstanceId) {
        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);
        final JobConfiguration jobConfiguration = this.map(schedulerModel);
        this.adminServerService.updateJobConfiguration(jobConfiguration, lightminClientApplication);
    }

    //Mappers

    //to model

    private SchedulerJobConfigurationModel map(final JobConfiguration jobConfiguration) {

        final JobSchedulerModel jobSchedulerModel = new JobSchedulerModel();
        jobSchedulerModel.setCronExpression(jobConfiguration.getJobSchedulerConfiguration().getCronExpression());
        jobSchedulerModel.setFixedDelay(jobConfiguration.getJobSchedulerConfiguration().getFixedDelay());
        jobSchedulerModel.setInitialDelay(jobConfiguration.getJobSchedulerConfiguration().getInitialDelay());
        jobSchedulerModel.setStatus(SchedulerStatusModel.map(jobConfiguration.getJobSchedulerConfiguration().getSchedulerStatus()).name());
        jobSchedulerModel.setStatusRead(new SchedulerStatusModel(SchedulerStatusModel.map(jobConfiguration.getJobSchedulerConfiguration().getSchedulerStatus())));
        jobSchedulerModel.setType(SchedulerTypeModel.map(jobConfiguration.getJobSchedulerConfiguration().getJobSchedulerType()).name());
        jobSchedulerModel.setTypeRead(new SchedulerTypeModel(SchedulerTypeModel.map(jobConfiguration.getJobSchedulerConfiguration().getJobSchedulerType())));
        jobSchedulerModel.setTaskExecutor(TaskExecutorTypeModel.map(jobConfiguration.getJobSchedulerConfiguration().getTaskExecutorType()).name());
        jobSchedulerModel.setTaskExecutorRead(new TaskExecutorTypeModel(TaskExecutorTypeModel.map(jobConfiguration.getJobSchedulerConfiguration().getTaskExecutorType())));

        final SchedulerJobConfigurationModel model = new SchedulerJobConfigurationModel();
        this.mapCommonJobConfigurationModel(model, jobConfiguration);
        model.setConfig(jobSchedulerModel);

        return model;

    }

    //from model
    private JobConfiguration map(final SchedulerJobConfigurationModel schedulerModel) {

        final JobConfiguration jobConfiguration = new JobConfiguration();

        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();

        jobSchedulerConfiguration.setCronExpression(schedulerModel.getConfig().getCronExpression());
        jobSchedulerConfiguration.setInitialDelay(schedulerModel.getConfig().getInitialDelay());
        jobSchedulerConfiguration.setFixedDelay(schedulerModel.getConfig().getFixedDelay());
        jobSchedulerConfiguration.setSchedulerStatus(this.mapSchedulerStatus(schedulerModel.getConfig().getStatus()));
        jobSchedulerConfiguration.setJobSchedulerType(this.mapSchedulerType(schedulerModel.getConfig().getType()));
        jobSchedulerConfiguration.setTaskExecutorType(this.mapTaskExecutor(schedulerModel.getConfig().getTaskExecutor()));

        jobConfiguration.setJobName(schedulerModel.getJobName());
        jobConfiguration.setJobConfigurationId(schedulerModel.getId());
        jobConfiguration.setJobIncrementer(this.mapIncrementer(schedulerModel.getIncrementer()));
        jobConfiguration.setJobParameters(this.mapJobParameters(schedulerModel.getParameters()));
        jobConfiguration.setJobSchedulerConfiguration(jobSchedulerConfiguration);

        return jobConfiguration;
    }


    private SchedulerStatus mapSchedulerStatus(final String status) {
        return SchedulerStatus.getByValue(status);
    }

    private JobSchedulerType mapSchedulerType(final String schedulerType) {
        return JobSchedulerType.valueOf(schedulerType);
    }
}
