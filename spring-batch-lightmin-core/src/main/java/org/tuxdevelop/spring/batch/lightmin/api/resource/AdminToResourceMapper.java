package org.tuxdevelop.spring.batch.lightmin.api.resource;


import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class AdminToResourceMapper {

    private AdminToResourceMapper() {
    }

    public static JobConfigurations map(final Collection<org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration>
                                                jobConfigurations) {
        final JobConfigurations response = new JobConfigurations();
        if (jobConfigurations != null && !jobConfigurations.isEmpty()) {
            for (final org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration jobConfiguration : jobConfigurations) {
                final JobConfiguration jobConfigurationReponse = map(jobConfiguration);
                response.getJobConfigurations().add(jobConfigurationReponse);
            }
        }
        return response;
    }

    public static JobConfiguration map(final org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration
                                               jobConfiguration) {
        final JobConfiguration response = new JobConfiguration();
        response.setJobConfigurationId(jobConfiguration.getJobConfigurationId());
        response.setJobName(jobConfiguration.getJobName());
        response.setJobIncrementer(map(jobConfiguration.getJobIncrementer()));
        response.setJobParameters(map(jobConfiguration.getJobParameters()));
        response.setJobSchedulerConfiguration(map(jobConfiguration.getJobSchedulerConfiguration()));
        return response;
    }

    public static JobSchedulerConfiguration map(final org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration jobSchedulerConfiguration) {

        final JobSchedulerConfiguration response = new JobSchedulerConfiguration();
        response.setCronExpression(jobSchedulerConfiguration.getCronExpression());
        response.setFixedDelay(jobSchedulerConfiguration.getFixedDelay());
        response.setInitialDelay(jobSchedulerConfiguration.getInitialDelay());
        response.setJobSchedulerType(map(jobSchedulerConfiguration.getJobSchedulerType()));
        response.setSchedulerStatus(map(jobSchedulerConfiguration.getSchedulerStatus()));
        response.setTaskExecutorType(map(jobSchedulerConfiguration.getTaskExecutorType()));
        return response;
    }

    static TaskExecutorType map(final org.tuxdevelop.spring.batch.lightmin.admin.domain.TaskExecutorType
                                        taskExecutorType) {
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

    static SchedulerStatus map(final org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerStatus schedulerStatus) {

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

    static JobSchedulerType map(final org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerType jobSchedulerType) {

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

    public static JobParameters map(final Map<String, Object> jobParameters) {

        final Map<String, JobParameter> jobParameterMap = new HashMap<>();
        if (jobParameters != null && !jobParameters.isEmpty()) {
            for (final Map.Entry<String, Object> entry : jobParameters.entrySet()) {
                final JobParameter jobParameter = mapJobParameter(entry.getValue());
                jobParameterMap.put(entry.getKey(), jobParameter);
            }
        }
        return new JobParameters(jobParameterMap);
    }

    static JobParameter mapJobParameter(final Object value) {
        final JobParameter response = new JobParameter();
        final ParameterType parameterType = mapObjectToParameterType(value);
        response.setParameterType(parameterType);
        response.setParameter(value);
        return response;
    }


    static JobIncrementer map(final org.tuxdevelop.spring.batch.lightmin.admin.domain.JobIncrementer
                                      jobIncrementer) {
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

    static ParameterType mapObjectToParameterType(final Object value) {
        final ParameterType response;
        if (value instanceof Long) {
            response = ParameterType.LONG;
        } else if (value instanceof String) {
            response = ParameterType.STRING;
        } else if (value instanceof Double) {
            response = ParameterType.DOUBLE;
        } else if (value instanceof Date) {
            response = ParameterType.DATE;
        } else {
            throw new SpringBatchLightminApplicationException("Unknown ParameterType:" + value.getClass().getName());
        }
        return response;
    }

}
