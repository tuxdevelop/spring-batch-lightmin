package org.tuxdevelop.spring.batch.lightmin.client.api;


import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public final class DomainToResourceMapper {

    private DomainToResourceMapper() {
    }

    public static JobConfigurations map(final Collection<org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration>
                                                jobConfigurations) {
        final JobConfigurations response = new JobConfigurations();
        if (jobConfigurations != null && !jobConfigurations.isEmpty()) {
            for (final org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration jobConfiguration : jobConfigurations) {
                final JobConfiguration jobConfigurationReponse = map(jobConfiguration);
                response.getJobConfigurations().add(jobConfigurationReponse);
            }
        }
        return response;
    }

    public static JobConfiguration map(final org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration
                                               jobConfiguration) {
        final JobConfiguration response = new JobConfiguration();
        response.setJobConfigurationId(jobConfiguration.getJobConfigurationId());
        response.setJobName(jobConfiguration.getJobName());
        response.setJobIncrementer(map(jobConfiguration.getJobIncrementer()));
        response.setJobParameters(map(jobConfiguration.getJobParameters()));
        response.setJobSchedulerConfiguration(map(jobConfiguration.getJobSchedulerConfiguration()));
        response.setJobListenerConfiguration(map(jobConfiguration.getJobListenerConfiguration()));
        return response;
    }

    private static JobSchedulerConfiguration map(final org.tuxdevelop.spring.batch.lightmin.domain.JobSchedulerConfiguration jobSchedulerConfiguration) {

        final JobSchedulerConfiguration response;
        if (jobSchedulerConfiguration != null) {
            response = new JobSchedulerConfiguration();
            response.setCronExpression(jobSchedulerConfiguration.getCronExpression());
            response.setFixedDelay(jobSchedulerConfiguration.getFixedDelay());
            response.setInitialDelay(jobSchedulerConfiguration.getInitialDelay());
            response.setJobSchedulerType(map(jobSchedulerConfiguration.getJobSchedulerType()));
            response.setSchedulerStatus(map(jobSchedulerConfiguration.getSchedulerStatus()));
            response.setTaskExecutorType(map(jobSchedulerConfiguration.getTaskExecutorType()));
        } else {
            response = null;
        }
        return response;
    }

    private static JobListenerConfiguration map(final org.tuxdevelop.spring.batch.lightmin.domain.JobListenerConfiguration jobListenerConfiguration) {

        final JobListenerConfiguration response;
        if (jobListenerConfiguration != null) {
            response = new JobListenerConfiguration();
            response.setListenerStatus(map(jobListenerConfiguration.getListenerStatus()));
            response.setTaskExecutorType(map(jobListenerConfiguration.getTaskExecutorType()));
            response.setFilePattern(jobListenerConfiguration.getFilePattern());
            response.setSourceFolder(jobListenerConfiguration.getSourceFolder());
            response.setJobListenerType(map(jobListenerConfiguration.getJobListenerType()));
            response.setPollerPeriod(jobListenerConfiguration.getPollerPeriod());
        } else {
            response = null;
        }
        return response;
    }

    private static TaskExecutorType map(final org.tuxdevelop.spring.batch.lightmin.domain.TaskExecutorType
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

    private static SchedulerStatus map(final org.tuxdevelop.spring.batch.lightmin.domain.SchedulerStatus schedulerStatus) {

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

    private static ListenerStatus map(final org.tuxdevelop.spring.batch.lightmin.domain.ListenerStatus listenerStatus) {
        final ListenerStatus response;
        switch (listenerStatus) {
            case ACTIVE:
                response = ListenerStatus.ACTIVE;
                break;
            case STOPPED:
                response = ListenerStatus.STOPPED;
                break;
            default:
                throw new SpringBatchLightminApplicationException("Unknown ListenerStatus: " + listenerStatus);

        }
        return response;
    }

    private static JobListenerType map(final org.tuxdevelop.spring.batch.lightmin.domain.JobListenerType jobListenerType) {
        final JobListenerType response;
        switch (jobListenerType) {
            case LOCAL_FOLDER_LISTENER:
                response = JobListenerType.LOCAL_FOLDER_LISTENER;
                break;
            default:
                throw new SpringBatchLightminApplicationException("Unknown JobListenerType: " + jobListenerType);

        }
        return response;
    }

    private static JobSchedulerType map(final org.tuxdevelop.spring.batch.lightmin.domain.JobSchedulerType jobSchedulerType) {

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

    private static JobParameter mapJobParameter(final Object value) {
        final JobParameter response = new JobParameter();
        final ParameterType parameterType = mapObjectToParameterType(value);
        response.setParameterType(parameterType);
        response.setParameter(value);
        return response;
    }


    private static JobIncrementer map(final org.tuxdevelop.spring.batch.lightmin.domain.JobIncrementer
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

    private static ParameterType mapObjectToParameterType(final Object value) {
        final ParameterType response;
        if (value instanceof Long || value instanceof Integer) {
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
