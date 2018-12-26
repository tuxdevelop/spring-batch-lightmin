package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobLaunch;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.api.resource.util.ApiParameterParser;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.launcher.JobLauncherModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

@Slf4j
public class JobLauncherFeService extends CommonFeService {

    private final JobServerService jobServerService;

    public JobLauncherFeService(final RegistrationBean registrationBean, final JobServerService jobServerService) {
        super(registrationBean);
        this.jobServerService = jobServerService;
    }

    public JobLauncherModel getJobLauncherModel(final String jobName,
                                                final String applicationInstanceId) {
        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);
        final JobParameters oldParameters =
                this.jobServerService.getLastJobParameters(jobName, lightminClientApplication);

        final String oldParametersString = ApiParameterParser.parseParametersToString(oldParameters);

        final JobLauncherModel model = new JobLauncherModel();
        model.setJobParameters(oldParametersString);
        model.setJobName(jobName);
        model.setApplicationInstanceId(applicationInstanceId);

        return model;
    }

    public void launchJob(final JobLauncherModel jobLauncherModel) {

        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(jobLauncherModel.getApplicationInstanceId());

        final JobParameters jobParameters =
                ApiParameterParser.parseParametersToJobParameters(jobLauncherModel.getJobParameters());

        this.attachIncremeter(jobLauncherModel, jobParameters);

        final JobLaunch jobLaunch = new JobLaunch();
        jobLaunch.setJobName(jobLauncherModel.getJobName());
        jobLaunch.setJobParameters(jobParameters);

        this.jobServerService.launchJob(jobLaunch, lightminClientApplication);

    }

    void attachIncremeter(final JobLauncherModel jobLauncherModel, final JobParameters jobParameters) {
        final JobIncrementer jobIncrementer = JobIncrementer.valueOf(jobLauncherModel.getJobIncrementer());
        if (JobIncrementer.DATE.equals(jobIncrementer)) {
            final JobParameter jobParameter = new JobParameter();
            jobParameter.setParameter(System.currentTimeMillis());
            jobParameter.setParameterType(ParameterType.LONG);
            jobParameters.getParameters().put(jobIncrementer.getIncrementerIdentifier(), jobParameter);
        } else {
            log.debug("nothing to map for job incremeter: {}", jobIncrementer);
        }
    }

}
