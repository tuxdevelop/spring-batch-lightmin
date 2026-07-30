package org.tuxdevelop.spring.batch.lightmin.client.api;


import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.*;
import java.time.ZoneId;

@Slf4j
public final class BatchToResourceMapper {

    private BatchToResourceMapper() {
    }

    public static List<JobExecution> mapExecutions(final Collection<org.springframework.batch.core.job.JobExecution>
                                                           jobExecutions) {
        final List<JobExecution> response = new LinkedList<>();
        for (final org.springframework.batch.core.job.JobExecution jobExecution : jobExecutions) {
            response.add(map(jobExecution));
        }
        return response;
    }

    public static JobExecution map(final org.springframework.batch.core.job.JobExecution jobExecution) {
        final JobExecution response = new JobExecution();
        response.setId(jobExecution.getId());
        response.setCreateTime(toDate(jobExecution.getCreateTime()));
        response.setEndTime(toDate(jobExecution.getEndTime()));
        response.setExitStatus(map(jobExecution.getExitStatus()));
        response.setFailureExceptions(jobExecution.getFailureExceptions());
        response.setJobConfigurationName(jobExecution.getJobInstance().getJobName());
        response.setJobInstance(map(jobExecution.getJobInstance()));
        response.setJobParameters(map(jobExecution.getJobParameters()));
        response.setLastUpdated(toDate(jobExecution.getLastUpdated()));
        response.setStartTime(toDate(jobExecution.getStartTime()));
        response.setStatus(map(jobExecution.getStatus()));
        response.setStepExecutions(mapStepExecutions(jobExecution.getStepExecutions()));
        response.setVersion(jobExecution.getVersion());
        return response;
    }

    public static List<JobInstance> mapInstances(final Collection<org.springframework.batch.core.job.JobInstance>
                                                         jobInstances) {
        final List<JobInstance> response = new LinkedList<>();
        for (final org.springframework.batch.core.job.JobInstance jobInstance : jobInstances) {
            response.add(map(jobInstance));
        }
        return response;
    }

    public static JobInstance map(final org.springframework.batch.core.job.JobInstance jobInstance) {
        final JobInstance response = new JobInstance();
        if (jobInstance != null) {
            response.setVersion(jobInstance.getVersion());
            response.setId(jobInstance.getInstanceId());
            response.setJobName(jobInstance.getJobName());
        } else {
            log.info("JobInstance was null, nothing to map");
        }
        return response;
    }

    public static JobParameters map(final org.springframework.batch.core.job.parameters.JobParameters jobParameters) {
        final JobParameters response = new JobParameters();
        final Set<org.springframework.batch.core.job.parameters.JobParameter<?>> parameters = jobParameters.parameters();
        if (!parameters.isEmpty()) {
            response.setParameters(new HashMap<>());
            for (final org.springframework.batch.core.job.parameters.JobParameter<?> entry : parameters) {
                final JobParameter jobParameter = new JobParameter();
                jobParameter.setParameter(entry.value());
                jobParameter.setParameterType(map(entry.type()));
                response.getParameters().put(entry.name(), jobParameter);
            }
        }
        return response;
    }

    public static StepExecution map(final org.springframework.batch.core.step.StepExecution stepExecution) {
        final StepExecution response = new StepExecution();
        response.setId(stepExecution.getId());
        response.setVersion(stepExecution.getVersion());
        response.setJobExecutionId(stepExecution.getJobExecutionId());
        response.setStepName(stepExecution.getStepName());
        response.setStatus(map(stepExecution.getStatus()));
        response.setReadCount(Math.toIntExact(stepExecution.getReadCount()));
        response.setWriteCount(Math.toIntExact(stepExecution.getWriteCount()));
        response.setCommitCount(Math.toIntExact(stepExecution.getCommitCount()));
        response.setRollbackCount(Math.toIntExact(stepExecution.getRollbackCount()));
        response.setReadSkipCount(Math.toIntExact(stepExecution.getReadSkipCount()));
        response.setProcessSkipCount(Math.toIntExact(stepExecution.getProcessSkipCount()));
        response.setWriteSkipCount(Math.toIntExact(stepExecution.getWriteSkipCount()));
        response.setStartTime(toDate(stepExecution.getStartTime()));
        response.setEndTime(toDate(stepExecution.getEndTime()));
        response.setLastUpdated(toDate(stepExecution.getLastUpdated()));
        response.setExitStatus(map(stepExecution.getExitStatus()));
        response.setTerminateOnly(stepExecution.isTerminateOnly());
        response.setFilterCount(Math.toIntExact(stepExecution.getFilterCount()));
        response.setFailureExceptions(stepExecution.getFailureExceptions());
        return response;
    }

    public static ExitStatus map(final org.springframework.batch.core.ExitStatus exitStatus) {
        return new ExitStatus(exitStatus.getExitCode(), exitStatus.getExitDescription());
    }


    public static BatchStatus map(final org.springframework.batch.core.BatchStatus batchStatus) {
        final BatchStatus response;
        switch (batchStatus) {
            case COMPLETED:
                response = BatchStatus.COMPLETED;
                break;
            case STARTED:
                response = BatchStatus.STARTED;
                break;
            case STARTING:
                response = BatchStatus.STARTING;
                break;
            case FAILED:
                response = BatchStatus.FAILED;
                break;
            case STOPPED:
                response = BatchStatus.STOPPED;
                break;
            case STOPPING:
                response = BatchStatus.STOPPING;
                break;
            case ABANDONED:
                response = BatchStatus.ABANDONED;
                break;
            case UNKNOWN:
                response = BatchStatus.UNKNOWN;
                break;
            default:
                response = BatchStatus.UNKNOWN;
                break;
        }
        return response;
    }

    public static ParameterType map(final Class<?> parameterType) {
        if (Date.class.equals(parameterType)) {
            return ParameterType.DATE;
        }
        if (String.class.equals(parameterType)) {
            return ParameterType.STRING;
        }
        if (Long.class.equals(parameterType)) {
            return ParameterType.LONG;
        }
        if (Double.class.equals(parameterType)) {
            return ParameterType.DOUBLE;
        }
        throw new SpringBatchLightminApplicationException("Unknown ParameterType: " + parameterType);
    }

    static List<StepExecution> mapStepExecutions(final Collection<org.springframework.batch.core.step.StepExecution>
                                                         stepExecutions) {
        final List<StepExecution> response = new LinkedList<>();
        if (!stepExecutions.isEmpty()) {
            for (final org.springframework.batch.core.step.StepExecution stepExecution : stepExecutions) {
                response.add(map(stepExecution));
            }
        }
        return response;
    }

    private static Date toDate(final java.time.LocalDateTime dateTime) {
        return dateTime == null ? null : Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
