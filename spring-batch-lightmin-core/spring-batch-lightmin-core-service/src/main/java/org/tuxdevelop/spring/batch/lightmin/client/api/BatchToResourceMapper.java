package org.tuxdevelop.spring.batch.lightmin.client.api;


import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.*;

@Slf4j
public final class BatchToResourceMapper {

    private BatchToResourceMapper() {
    }

    public static List<JobExecution> mapExecutions(final Collection<org.springframework.batch.core.JobExecution>
                                                           jobExecutions) {
        final List<JobExecution> response = new LinkedList<>();
        for (final org.springframework.batch.core.JobExecution jobExecution : jobExecutions) {
            response.add(map(jobExecution));
        }
        return response;
    }

    public static JobExecution map(final org.springframework.batch.core.JobExecution jobExecution) {
        final JobExecution response = new JobExecution();
        response.setId(jobExecution.getId());
        response.setCreateTime(jobExecution.getCreateTime());
        response.setEndTime(jobExecution.getEndTime());
        response.setExitStatus(map(jobExecution.getExitStatus()));
        response.setFailureExceptions(jobExecution.getFailureExceptions());
        response.setJobConfigurationName(jobExecution.getJobConfigurationName());
        response.setJobInstance(map(jobExecution.getJobInstance()));
        response.setJobParameters(map(jobExecution.getJobParameters()));
        response.setLastUpdated(jobExecution.getLastUpdated());
        response.setStartTime(jobExecution.getStartTime());
        response.setStatus(map(jobExecution.getStatus()));
        response.setStepExecutions(mapStepExecutions(jobExecution.getStepExecutions()));
        response.setVersion(jobExecution.getVersion());
        return response;
    }

    public static List<JobInstance> mapInstances(final Collection<org.springframework.batch.core.JobInstance>
                                                         jobInstances) {
        final List<JobInstance> response = new LinkedList<>();
        for (final org.springframework.batch.core.JobInstance jobInstance : jobInstances) {
            response.add(map(jobInstance));
        }
        return response;
    }

    public static JobInstance map(final org.springframework.batch.core.JobInstance jobInstance) {
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

    public static JobParameters map(final org.springframework.batch.core.JobParameters jobParameters) {
        final JobParameters response = new JobParameters();
        final Map<String, org.springframework.batch.core.JobParameter> parametersMap = jobParameters.getParameters();
        if (!parametersMap.isEmpty()) {
            response.setParameters(new HashMap<>());
            for (final Map.Entry<String, org.springframework.batch.core.JobParameter> entry : parametersMap.entrySet()) {
                final JobParameter jobParameter = new JobParameter();
                jobParameter.setParameter(entry.getValue().getValue());
                jobParameter.setParameterType(map(entry.getValue().getType()));
                response.getParameters().put(entry.getKey(), jobParameter);
            }
        }
        return response;
    }

    public static StepExecution map(final org.springframework.batch.core.StepExecution stepExecution) {
        final StepExecution response = new StepExecution();
        response.setId(stepExecution.getId());
        response.setVersion(stepExecution.getVersion());
        response.setJobExecutionId(stepExecution.getJobExecutionId());
        response.setStepName(stepExecution.getStepName());
        response.setStatus(map(stepExecution.getStatus()));
        response.setReadCount(stepExecution.getReadCount());
        response.setWriteCount(stepExecution.getWriteCount());
        response.setCommitCount(stepExecution.getCommitCount());
        response.setRollbackCount(stepExecution.getRollbackCount());
        response.setReadSkipCount(stepExecution.getReadSkipCount());
        response.setProcessSkipCount(stepExecution.getProcessSkipCount());
        response.setWriteSkipCount(stepExecution.getWriteSkipCount());
        response.setStartTime(stepExecution.getStartTime());
        response.setEndTime(stepExecution.getEndTime());
        response.setLastUpdated(stepExecution.getLastUpdated());
        response.setExitStatus(map(stepExecution.getExitStatus()));
        response.setTerminateOnly(stepExecution.isTerminateOnly());
        response.setFilterCount(stepExecution.getFilterCount());
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

    public static ParameterType map(final org.springframework.batch.core.JobParameter.ParameterType parameterType) {
        final ParameterType response;
        switch (parameterType) {
            case DATE:
                response = ParameterType.DATE;
                break;
            case STRING:
                response = ParameterType.STRING;
                break;
            case LONG:
                response = ParameterType.LONG;
                break;
            case DOUBLE:
                response = ParameterType.DOUBLE;
                break;
            default:
                throw new SpringBatchLightminApplicationException("Unknown ParameterType: " + parameterType);
        }
        return response;
    }

    static List<StepExecution> mapStepExecutions(final Collection<org.springframework.batch.core.StepExecution>
                                                         stepExecutions) {
        final List<StepExecution> response = new LinkedList<>();
        if (!stepExecutions.isEmpty()) {
            for (final org.springframework.batch.core.StepExecution stepExecution : stepExecutions) {
                response.add(map(stepExecution));
            }
        }
        return response;
    }
}
