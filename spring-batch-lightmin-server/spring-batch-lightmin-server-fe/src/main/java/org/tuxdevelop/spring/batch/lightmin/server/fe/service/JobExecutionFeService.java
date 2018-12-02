package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ContentPageModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch.JobExecutionDetailsModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch.JobExecutionModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch.JobParameterModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch.StepExecutionModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobExecutionFeService extends CommonFeService {

    private final JobServerService jobServerService;

    public JobExecutionFeService(final RegistrationBean registrationBean, final JobServerService jobServerService) {
        super(registrationBean);
        this.jobServerService = jobServerService;
    }


    public ContentPageModel<List<JobExecutionModel>> getJobExecutionModelPage(final String applicationInstanceId,
                                                                              final String jobName,
                                                                              final Integer startIndex,
                                                                              final Integer pageSize) {
        final LightminClientApplication lightminClientApplication = this.findLightminClientApplicatonById(applicationInstanceId);
        final JobInstancePage jobInstancesPage =
                this.jobServerService.getJobInstances(jobName, startIndex, pageSize, lightminClientApplication);

        final ContentPageModel<List<JobExecutionModel>> page =
                new ContentPageModel<>(startIndex, pageSize, jobInstancesPage.getTotalJobInstanceCount());

        final List<JobExecutionModel> jobExecutionModels = new ArrayList<>();

        for (final JobInstance jobInstance : jobInstancesPage.getJobInstances()) {
            final JobExecutionPage jobExecutionsForInstance =
                    this.jobServerService.getJobExecutionPage(jobInstance.getId(), lightminClientApplication);
            final List<JobExecution> jobExecutions = jobExecutionsForInstance.getJobExecutions();
            for (final JobExecution jobExecution : jobExecutions) {
                final JobExecutionModel jobExecutionModel = new JobExecutionModel();
                this.mapJobExecution(jobExecution, jobExecutionModel);
                jobExecutionModels.add(jobExecutionModel);
            }
        }
        page.setValue(jobExecutionModels);
        return page;
    }

    public JobExecutionDetailsModel getJobExecutionDetailsModel(final Long jobExecutionId,
                                                                final String applicationInstanceId) {
        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);

        final JobExecution jobExecution = this.jobServerService.getJobExecution(jobExecutionId, lightminClientApplication);
        final List<StepExecutionModel> stepExecutionModels = this.mapStepExecutions(jobExecution.getStepExecutions());
        final List<JobParameterModel> jobParameterModels = this.mapJobParameters(jobExecution.getJobParameters());
        final JobExecutionDetailsModel jobExecutionDetailsModel = new JobExecutionDetailsModel();
        this.mapJobExecution(jobExecution, jobExecutionDetailsModel);
        jobExecutionDetailsModel.setExitMessage(jobExecution.getExitStatus().getExitDescription());
        jobExecutionDetailsModel.setStepExecutions(stepExecutionModels);
        jobExecutionDetailsModel.setJobParameters(jobParameterModels);
        return jobExecutionDetailsModel;
    }

    public void restartJobExecution(final Long jobExecutionId, final String applicationInstanceId) {
        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);
        try {
            this.jobServerService.restartJobExecution(jobExecutionId, lightminClientApplication);
        } catch (final Exception e) {
            throw new SpringBatchLightminApplicationException("Could not restart Job");
        }
    }

    public void stopJobExecution(final Long jobExecutionId, final String applicationInstanceId) {
        final LightminClientApplication lightminClientApplication =
                this.findLightminClientApplicatonById(applicationInstanceId);
        try {
            this.jobServerService.stopJobExecution(jobExecutionId, lightminClientApplication);
        } catch (final Exception e) {
            throw new SpringBatchLightminApplicationException("Could not stop Job");
        }
    }

    private void mapJobExecution(final JobExecution jobExecution, final JobExecutionModel jobExecutionModel) {
        jobExecutionModel.setInstanceId(jobExecution.getJobInstance().getId());
        jobExecutionModel.setId(jobExecution.getId());
        jobExecutionModel.setCreateTime(jobExecution.getCreateTime());
        jobExecutionModel.setStartTime(jobExecution.getStartTime());
        jobExecutionModel.setEndTime(jobExecution.getEndTime());
        jobExecutionModel.setStatus(jobExecution.getStatus().name().toLowerCase());
        jobExecutionModel.setExitStatus(jobExecution.getExitStatus() != null ? jobExecution.getExitStatus().getExitCode().toLowerCase() : "");
    }

    private List<StepExecutionModel> mapStepExecutions(final List<StepExecution> stepExecutions) {
        final List<StepExecutionModel> stepExecutionModels = new ArrayList<>();
        for (final StepExecution stepExecution : stepExecutions) {
            final StepExecutionModel stepExecutionModel = this.mapStepExecution(stepExecution);
            stepExecutionModels.add(stepExecutionModel);
        }
        return stepExecutionModels;
    }

    private StepExecutionModel mapStepExecution(final StepExecution stepExecution) {
        final StepExecutionModel stepExecutionModel = new StepExecutionModel();
        stepExecutionModel.setId(stepExecution.getId());
        stepExecutionModel.setStatus(stepExecution.getStatus().name().toLowerCase());
        stepExecutionModel.setExitStatus(stepExecution.getExitStatus() != null ? stepExecution.getExitStatus().getExitCode() : "");
        stepExecutionModel.setStartTime(stepExecution.getStartTime());
        stepExecutionModel.setEndTime(stepExecution.getEndTime());
        stepExecutionModel.setCommitCount(stepExecution.getCommitCount());
        stepExecutionModel.setReadCount(stepExecution.getReadCount());
        stepExecutionModel.setWriteCount(stepExecution.getWriteCount());
        stepExecutionModel.setProcessSkipCount(stepExecution.getProcessSkipCount());
        stepExecutionModel.setReadSkipCount(stepExecution.getReadSkipCount());
        stepExecutionModel.setWriteSkipCount(stepExecution.getWriteSkipCount());
        stepExecutionModel.setRollbackCount(stepExecution.getRollbackCount());
        stepExecutionModel.setStepName(stepExecution.getStepName());
        stepExecutionModel.setExitMessage(stepExecution.getExitStatus().getExitDescription());
        return stepExecutionModel;
    }

    private List<JobParameterModel> mapJobParameters(final JobParameters jobParameters) {
        final List<JobParameterModel> jobParameterModels = new ArrayList<>();
        for (final Map.Entry<String, JobParameter> entry : jobParameters.getParameters().entrySet()) {
            final JobParameterModel model = this.mapJobParameter(entry.getKey(), entry.getValue());
            jobParameterModels.add(model);
        }
        return jobParameterModels;
    }

    private JobParameterModel mapJobParameter(final String key, final JobParameter jobParameter) {
        final JobParameterModel jobParameterModel = new JobParameterModel();
        jobParameterModel.setKey(key);
        jobParameterModel.setValue(jobParameter.getParameter());
        return jobParameterModel;
    }
}
