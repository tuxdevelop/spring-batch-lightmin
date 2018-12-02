package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.TaskExecutorType;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.util.ApiParameterParser;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.JobIncremeterTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.CommonJobConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import java.util.HashMap;
import java.util.Map;

public abstract class CommonJobConfigurationFeService extends CommonFeService {

    protected CommonJobConfigurationFeService(final RegistrationBean registrationBean) {
        super(registrationBean);
    }

    protected Map<String, Object> mapParameters(final JobParameters jobParameters) {
        final Map<String, Object> parametersMap = new HashMap<>();
        for (final Map.Entry<String, JobParameter> entry : jobParameters.getParameters().entrySet()) {
            parametersMap.put(entry.getKey(), entry.getValue().getParameter());
        }
        return parametersMap;
    }

    protected JobParameters mapJobParameters(final String parameters) {
        return ApiParameterParser.parseParametersToJobParameters(parameters);
    }

    protected JobIncrementer mapIncrementer(final String incrementer) {
        return JobIncrementer.valueOf(incrementer);
    }

    protected TaskExecutorType mapTaskExecutor(final String taskExecutor) {
        return TaskExecutorType.valueOf(taskExecutor);
    }

    protected void mapCommonJobConfigurationModel(final CommonJobConfigurationModel model, final JobConfiguration jobConfiguration) {
        model.setId(jobConfiguration.getJobConfigurationId());
        model.setJobName(jobConfiguration.getJobName());
        model.setParameters(ApiParameterParser.parseParametersToString(jobConfiguration.getJobParameters()));
        model.setParametersRead(this.mapParameters(jobConfiguration.getJobParameters()));
        model.setIncrementerRead(new JobIncremeterTypeModel(JobIncremeterTypeModel.map(jobConfiguration.getJobIncrementer())));
        model.setIncrementer(JobIncremeterTypeModel.map(jobConfiguration.getJobIncrementer()).name());

    }

}
