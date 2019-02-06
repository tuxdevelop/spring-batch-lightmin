package org.tuxdevelop.spring.batch.lightmin.client.api;

import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.util.DomainParameterParser;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public final class ResourceToDomainMapper {

    private ResourceToDomainMapper() {
    }

    public static Collection<JobConfiguration> map(final JobConfigurations jobConfigurations) {
        final Collection<JobConfiguration> response = new LinkedList<>();
        for (final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration jobConfiguration : jobConfigurations.getJobConfigurations()) {
            final JobConfiguration jobConfigurationResponse = map(jobConfiguration);
            response.add(jobConfigurationResponse);
        }
        return response;
    }

    public static JobConfiguration map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration jobConfiguration) {
        final JobConfiguration response = new JobConfiguration();
        response.setJobName(jobConfiguration.getJobName());
        response.setJobConfigurationId(jobConfiguration.getJobConfigurationId());
        response.setJobParameters(mapToMap(jobConfiguration.getJobParameters()));
        response.setJobIncrementer(map(jobConfiguration.getJobIncrementer()));
        response.setJobSchedulerConfiguration(map(jobConfiguration.getJobSchedulerConfiguration()));
        response.setJobListenerConfiguration(map(jobConfiguration.getJobListenerConfiguration()));
        return response;
    }

    public static org.springframework.batch.core.JobParameters map(final JobParameters jobParameters) {
        final Map<String, org.springframework.batch.core.JobParameter> parametersMap = new HashMap<>();
        if (jobParameters != null) {
            for (final Map.Entry<String, JobParameter> entry : jobParameters.getParameters().entrySet()) {
                final org.springframework.batch.core.JobParameter.ParameterType parameterType = map(entry.getValue()
                        .getParameterType());
                final org.springframework.batch.core.JobParameter jobParameter;
                final String parameter = String.valueOf(entry.getValue().getParameter());
                switch (parameterType) {
                    case STRING:
                        jobParameter = new org.springframework.batch.core.JobParameter(parameter);
                        break;
                    case DOUBLE:
                        jobParameter = new org.springframework.batch.core.JobParameter(Double.parseDouble(parameter));
                        break;
                    case LONG:
                        jobParameter = new org.springframework.batch.core.JobParameter(Long.parseLong(parameter));
                        break;
                    case DATE:
                        jobParameter = new org.springframework.batch.core.JobParameter(DomainParameterParser.parseDate(parameter));
                        break;
                    default:
                        throw new SpringBatchLightminApplicationException("Unknown JobParameterType: " + entry.getValue().getParameterType());
                }
                parametersMap.put(entry.getKey(), jobParameter);
            }
        }
        return new org.springframework.batch.core.JobParameters(parametersMap);
    }

    private static JobSchedulerConfiguration map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerConfiguration jobSchedulerConfiguration) {
        final JobSchedulerConfiguration response;
        if (jobSchedulerConfiguration != null) {
            response = new JobSchedulerConfiguration();
            response.setJobSchedulerType(map(jobSchedulerConfiguration.getJobSchedulerType()));
            response.setCronExpression(jobSchedulerConfiguration.getCronExpression());
            response.setFixedDelay(jobSchedulerConfiguration.getFixedDelay());
            response.setInitialDelay(jobSchedulerConfiguration.getInitialDelay());
            response.setSchedulerStatus(map(jobSchedulerConfiguration.getSchedulerStatus()));
            response.setTaskExecutorType(map(jobSchedulerConfiguration.getTaskExecutorType()));
        } else {
            response = null;
        }
        return response;
    }

    private static JobListenerConfiguration map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerConfiguration jobListenerConfiguration) {

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

    private static TaskExecutorType map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.TaskExecutorType taskExecutorType) {
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

    private static SchedulerStatus map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.SchedulerStatus schedulerStatus) {

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

    private static JobSchedulerType map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType jobSchedulerType) {

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

    private static ListenerStatus map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.ListenerStatus listenerStatus) {
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

    private static JobListenerType map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerType jobListenerType) {
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

    private static JobIncrementer map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer jobIncrementer) {
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

    private static Map<String, Object> mapToMap(final JobParameters jobParameters) {
        final Map<String, Object> jobParameterMap = new HashMap<>();
        if (jobParameters != null && jobParameters.getParameters() != null) {
            final Map<String, JobParameter> parameters = jobParameters.getParameters();
            for (final Map.Entry<String, JobParameter> entry : parameters.entrySet()) {
                jobParameterMap.put(entry.getKey(), entry.getValue().getParameter());
            }
        }
        return jobParameterMap;
    }

    private static org.springframework.batch.core.JobParameter.ParameterType map(final ParameterType parameterType) {
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
