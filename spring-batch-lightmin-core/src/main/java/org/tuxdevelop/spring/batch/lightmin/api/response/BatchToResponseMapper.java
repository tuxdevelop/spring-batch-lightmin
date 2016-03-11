package org.tuxdevelop.spring.batch.lightmin.api.response;


import java.util.*;

public final class BatchToResponseMapper {

    private BatchToResponseMapper() {
    }

    public static List<JobExecution> mapExecutions(final Collection<org.springframework.batch.core.JobExecution>
                                                           jobExecutions) {
        final List<JobExecution> response = new LinkedList<JobExecution>();
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
        response.setExecutionContext(jobExecution.getExecutionContext());
        response.setExitStatus(map(jobExecution.getExitStatus()));
        response.setFailureExceptions(jobExecution.getFailureExceptions());
        response.setJobConfigurationName(jobExecution.getJobConfigurationName());
        response.setJobInstance(map(jobExecution.getJobInstance()));
        response.setJobParameters(map(jobExecution.getJobParameters()));
        response.setLastUpdated(jobExecution.getLastUpdated());
        response.setStartTime(jobExecution.getStartTime());
        response.setStatus(jobExecution.getStatus());
        response.setStepExecutions(mapStepExecutions(jobExecution.getStepExecutions()));
        response.setVersion(jobExecution.getVersion());
        return response;
    }

    public static List<JobInstance> mapInstances(final Collection<org.springframework.batch.core.JobInstance>
                                                         jobInstances) {
        final List<JobInstance> response = new LinkedList<JobInstance>();
        for (final org.springframework.batch.core.JobInstance jobInstance : jobInstances) {
            response.add(map(jobInstance));
        }
        return response;
    }

    public static JobInstance map(final org.springframework.batch.core.JobInstance jobInstance) {
        final JobInstance response = new JobInstance();
        response.setVersion(jobInstance.getVersion());
        response.setId(jobInstance.getInstanceId());
        response.setJobName(jobInstance.getJobName());
        return response;
    }

    public static JobParameters map(final org.springframework.batch.core.JobParameters jobParameters) {
        final JobParameters response = new JobParameters();
        final Map<String, org.springframework.batch.core.JobParameter> parametersMap = jobParameters.getParameters();
        if (!parametersMap.isEmpty()) {
            response.setParameters(new HashMap<String, JobParameter>());
            for (final Map.Entry<String, org.springframework.batch.core.JobParameter> entry : parametersMap.entrySet()) {
                final JobParameter jobParameter = new JobParameter();
                jobParameter.setIdentifying(entry.getValue().isIdentifying());
                jobParameter.setParameter(entry.getValue().getValue());
                jobParameter.setParameterType(entry.getValue().getType());
                response.getParameters().put(entry.getKey(), jobParameter);
            }
        }
        return response;
    }

    public static List<StepExecution> mapStepExecutions(final Collection<org.springframework.batch.core.StepExecution>
                                                                stepExecutions) {
        final List<StepExecution> response = new LinkedList<StepExecution>();
        if (!stepExecutions.isEmpty()) {
            for (final org.springframework.batch.core.StepExecution stepExecution : stepExecutions) {
                response.add(map(stepExecution));
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
        response.setStatus(stepExecution.getStatus());
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
        response.setExecutionContext(stepExecution.getExecutionContext());
        response.setExitStatus(map(stepExecution.getExitStatus()));
        response.setTerminateOnly(stepExecution.isTerminateOnly());
        response.setFilterCount(stepExecution.getFilterCount());
        response.setFailureExceptions(stepExecution.getFailureExceptions());
        return response;
    }

    public static ExitStatus map(final org.springframework.batch.core.ExitStatus exitStatus) {
        return new ExitStatus(exitStatus.getExitCode(), exitStatus.getExitDescription());
    }
}
