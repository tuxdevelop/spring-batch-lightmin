package org.tuxdevelop.spring.batch.lightmin.api.resource;

import org.tuxdevelop.spring.batch.lightmin.admin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public final class ResourceToAdminMapper {

    private ResourceToAdminMapper() {
    }

    public static JobConfiguration map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration
                                               jobConfiguration) {
        final JobConfiguration response = new JobConfiguration();
        response.setJobName(jobConfiguration.getJobName());
        response.setJobConfigurationId(jobConfiguration.getJobConfigurationId());
        response.setJobParameters(mapToMap(jobConfiguration.getJobParameters()));
        response.setJobIncrementer(map(jobConfiguration.getJobIncrementer()));
        response.setJobSchedulerConfiguration(map(jobConfiguration.getJobSchedulerConfiguration()));
        return response;
    }

    static JobSchedulerConfiguration map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerConfiguration jobSchedulerConfiguration) {
        final JobSchedulerConfiguration response = new JobSchedulerConfiguration();
        response.setJobSchedulerType(map(jobSchedulerConfiguration.getJobSchedulerType()));
        response.setCronExpression(jobSchedulerConfiguration.getCronExpression());
        response.setFixedDelay(jobSchedulerConfiguration.getFixedDelay());
        response.setInitialDelay(jobSchedulerConfiguration.getInitialDelay());
        response.setSchedulerStatus(map(jobSchedulerConfiguration.getSchedulerStatus()));
        response.setTaskExecutorType(map(jobSchedulerConfiguration.getTaskExecutorType()));
        return response;
    }

    static TaskExecutorType map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.TaskExecutorType taskExecutorType) {
        final TaskExecutorType response;
        switch (taskExecutorType) {
            case SYNCHRONOUS:
                response = TaskExecutorType.SYNCHRONOUS;
                break;
            case ASYNCHRONOUS:
                response = TaskExecutorType.ASYNCHRONOUS;
                break;
            default:
                throw new SpringBatchLightminApplicationException("Unknown TaskExecutorType: " + taskExecutorType);

        }

        return response;
    }

    static SchedulerStatus map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.SchedulerStatus schedulerStatus) {

        final SchedulerStatus response;
        switch (schedulerStatus) {
            case RUNNING:
                response = SchedulerStatus.RUNNING;
                break;
            case STOPPED:
                response = SchedulerStatus.STOPPED;
                break;
            case INITIALIZED:
                response = SchedulerStatus.INITIALIZED;
                break;
            case IN_TERMINATION:
                response = SchedulerStatus.IN_TERMINATION;
                break;
            default:
                throw new SpringBatchLightminApplicationException("Unknown SchedulerStatus: " + schedulerStatus);
        }
        return response;
    }

    static JobSchedulerType map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType jobSchedulerType) {

        final JobSchedulerType response;

        switch (jobSchedulerType) {
            case CRON:
                response = JobSchedulerType.CRON;
                break;
            case PERIOD:
                response = JobSchedulerType.PERIOD;
                break;
            default:
                throw new SpringBatchLightminApplicationException("Unknown JobSchedulerType: " + jobSchedulerType);
        }

        return response;
    }

    static JobIncrementer map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer jobIncrementer) {
        final JobIncrementer response;

        switch (jobIncrementer) {
            case DATE:
                response = JobIncrementer.DATE;
                break;
            case NONE:
            default:
                response = JobIncrementer.NONE;
                break;
        }
        return response;
    }

    static Map<String, Object> mapToMap(final JobParameters jobParameters) {
        final Map<String, Object> jobParameterMap = new HashMap<>();
        final Map<String, JobParameter> parameters = jobParameters.getParameters();
        for (final Map.Entry<String, JobParameter> entry : parameters.entrySet()) {
            jobParameterMap.put(entry.getKey(), entry.getValue().getParameter());
        }
        return jobParameterMap;
    }

    public static org.springframework.batch.core.JobParameters map(final JobParameters jobParameters) {
        final Map<String, org.springframework.batch.core.JobParameter> parametersMap = new HashMap<>();
        for (final Map.Entry<String, JobParameter> entry : jobParameters.getParameters().entrySet()) {
            final org.springframework.batch.core.JobParameter.ParameterType parameterType = map(entry.getValue()
                    .getParameterType());
            final org.springframework.batch.core.JobParameter jobParameter;
            switch (parameterType) {
                case STRING:
                    jobParameter = new org.springframework.batch.core.JobParameter((String) entry.getValue()
                            .getParameter());
                    break;
                case DOUBLE:
                    jobParameter = new org.springframework.batch.core.JobParameter((Double) entry.getValue()
                            .getParameter());
                    break;
                case LONG:
                    jobParameter = new org.springframework.batch.core.JobParameter((Long) entry.getValue()
                            .getParameter());
                    break;
                case DATE:
                    jobParameter = new org.springframework.batch.core.JobParameter((Date) entry.getValue()
                            .getParameter());
                    break;
                default:
                    throw new SpringBatchLightminApplicationException("Unknown JobParameterType: " + entry.getValue().getParameterType());
            }
            parametersMap.put(entry.getKey(), jobParameter);
        }
        return new org.springframework.batch.core.JobParameters(parametersMap);
    }

    public static org.springframework.batch.core.JobParameter.ParameterType map(final ParameterType parameterType) {
        final org.springframework.batch.core.JobParameter.ParameterType response;
        switch (parameterType) {
            case DATE:
                response = org.springframework.batch.core.JobParameter.ParameterType.DATE;
                break;
            case STRING:
                response = org.springframework.batch.core.JobParameter.ParameterType.STRING;
                break;
            case LONG:
                response = org.springframework.batch.core.JobParameter.ParameterType.LONG;
                break;
            case DOUBLE:
                response = org.springframework.batch.core.JobParameter.ParameterType.DOUBLE;
                break;
            default:
                throw new SpringBatchLightminApplicationException("Unknown ParameterType: " + parameterType);
        }
        return response;
    }

}
