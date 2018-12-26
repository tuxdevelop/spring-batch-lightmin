package org.tuxdevelop.spring.batch.lightmin.test.api;

import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;

import java.util.*;

public class ApiTestHelper {


    public static JobConfiguration createJobConfiguration(final JobSchedulerConfiguration jobSchedulerConfiguration) {
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration jobConfiguration =
                createJobConfigurationApi();
        jobConfiguration.setJobSchedulerConfiguration(jobSchedulerConfiguration);
        return jobConfiguration;
    }

    public static JobConfiguration createJobConfiguration(final JobListenerConfiguration jobListenerConfiguration) {
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration jobConfiguration =
                createJobConfigurationApi();
        jobConfiguration.setJobListenerConfiguration(jobListenerConfiguration);
        return jobConfiguration;
    }

    private static JobConfiguration createJobConfigurationApi() {
        final JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setJobName("sampleJob");
        jobConfiguration.setJobIncrementer(JobIncrementer.DATE);
        jobConfiguration.setJobParameters(new JobParameters());
        return jobConfiguration;
    }


    public static JobSchedulerConfiguration createJobSchedulerConfiguration(final String cronExpression,
                                                                            final Long fixedDelay,
                                                                            final Long initialDelay,
                                                                            final JobSchedulerType jobSchedulerType) {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setCronExpression(cronExpression);
        jobSchedulerConfiguration.setFixedDelay(fixedDelay);
        jobSchedulerConfiguration.setInitialDelay(initialDelay);
        jobSchedulerConfiguration.setJobSchedulerType(jobSchedulerType);
        jobSchedulerConfiguration.setTaskExecutorType(TaskExecutorType.SYNCHRONOUS);
        jobSchedulerConfiguration.setSchedulerStatus(SchedulerStatus.INITIALIZED);
        return jobSchedulerConfiguration;
    }

    public static JobListenerConfiguration createJobListenerConfiguration(final String sourceFolder,
                                                                          final String pattern,
                                                                          final JobListenerType jobListenerType) {
        final JobListenerConfiguration jobListenerConfiguration = new JobListenerConfiguration();
        jobListenerConfiguration.setJobListenerType(jobListenerType);
        jobListenerConfiguration.setTaskExecutorType(TaskExecutorType.SYNCHRONOUS);
        jobListenerConfiguration.setSourceFolder(sourceFolder);
        jobListenerConfiguration.setFilePattern(pattern);
        jobListenerConfiguration.setPollerPeriod(1000L);
        jobListenerConfiguration.setListenerStatus(ListenerStatus.STOPPED);
        return jobListenerConfiguration;
    }

    public static JobConfigurations createJobListenerConfigurations(final int count) {
        final Collection<JobConfiguration> jobListenerConfigurations = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final JobListenerConfiguration jobListenerConfiguration =
                    createJobListenerConfiguration("/", "*.txt", JobListenerType.LOCAL_FOLDER_LISTENER);
            final JobConfiguration jobConfiguration = createJobConfiguration(jobListenerConfiguration);
            jobListenerConfigurations.add(jobConfiguration);
        }
        return createJobConfigurations(jobListenerConfigurations);
    }

    public static JobConfigurations createJobSchedulerConfigurations(final int count) {
        final Collection<JobConfiguration> jobSchedulerConfigurations = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final JobSchedulerConfiguration jobSchedulerConfiguration =
                    createJobSchedulerConfiguration(null, 1L, 1L, JobSchedulerType.PERIOD);
            final JobConfiguration jobConfiguration = createJobConfiguration(jobSchedulerConfiguration);
            jobSchedulerConfigurations.add(jobConfiguration);
        }
        return createJobConfigurations(jobSchedulerConfigurations);
    }

    public static JobConfigurations createJobConfigurations(final Collection<JobConfiguration> jobConfigurationCollection) {
        final JobConfigurations jobConfigurations = new JobConfigurations();
        jobConfigurations.setJobConfigurations(jobConfigurationCollection);
        return jobConfigurations;
    }

    public static Map<String, JobConfigurations> addJobConfigurations(
            final Map<String, JobConfigurations> jobConfigurationsMap,
            final String applicationName,
            final JobConfigurations jobConfigurations) {
        jobConfigurationsMap.put(applicationName, jobConfigurations);
        return jobConfigurationsMap;

    }

    public static JobInstancePage createJobInstancePage(final int pageSize) {
        final JobInstancePage jobInstancePage = new JobInstancePage();
        jobInstancePage.setJobInstances(createJobInstances(pageSize));
        jobInstancePage.setJobName("test_job");
        jobInstancePage.setPageSize(pageSize);
        jobInstancePage.setStartIndex(0);
        jobInstancePage.setTotalJobInstanceCount(pageSize);
        return jobInstancePage;
    }

    public static List<JobInstance> createJobInstances(final int count) {
        final List<JobInstance> jobInstances = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            final JobInstance jobInstance = createJobInstance((long) i, "test_job");
            jobInstances.add(jobInstance);
        }
        return jobInstances;
    }

    public static JobExecutionPage createJobExecutionPage(final int pageSize) {

        final List<JobExecution> jobExecutions = createJobExecutions(pageSize);
        final JobExecutionPage jobExecutionPage = new JobExecutionPage();
        jobExecutionPage.setJobExecutions(jobExecutions);
        jobExecutionPage.setPageSize(pageSize);
        jobExecutionPage.setStartIndex(0);
        jobExecutionPage.setJobName("test_job");
        jobExecutionPage.setTotalJobExecutionCount(pageSize);
        jobExecutionPage.setJobInstanceId(1L);
        return jobExecutionPage;
    }

    public static List<JobExecution> createJobExecutions(final int count) {
        final List<JobExecution> jobExecutions = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            final JobExecution jobExecution = createJobExecution((long) i);
            jobExecutions.add(jobExecution);
        }
        return jobExecutions;
    }

    public static JobExecution createJobExecution(final Long jobExecutionId) {

        final JobExecution jobExecution = new JobExecution();
        jobExecution.setCreateTime(new Date());
        jobExecution.setEndTime(new Date());
        jobExecution.setExitStatus(new ExitStatus("COMPLETED"));
        jobExecution.setId(jobExecutionId);
        jobExecution.setJobParameters(new JobParameters());
        jobExecution.setLastUpdated(new Date());
        jobExecution.setStepExecutions(createStepExecutions(jobExecutionId));
        jobExecution.setJobInstance(createJobInstance(jobExecutionId, "test_job"));
        jobExecution.setStatus(BatchStatus.COMPLETED);
        return jobExecution;

    }

    public static JobInstance createJobInstance(final Long jobInstanceId, final String jobName) {
        final JobInstance jobInstance = new JobInstance();
        jobInstance.setId(jobInstanceId);
        jobInstance.setJobName(jobName);
        return jobInstance;
    }

    public static List<StepExecution> createStepExecutions(final Long jobExecutionId) {
        final List<StepExecution> stepExecutions = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            final StepExecution stepExecution = createStepExecution((long) i, jobExecutionId);
            stepExecutions.add(stepExecution);
        }
        return stepExecutions;
    }


    public static StepExecution createStepExecution(final Long stepExecutionId, final Long jobExecutionId) {
        final StepExecution stepExecution = new StepExecution();
        stepExecution.setStartTime(new Date());
        stepExecution.setEndTime(new Date());
        stepExecution.setExitStatus(new ExitStatus("COMPLETED"));
        stepExecution.setId(stepExecutionId);
        stepExecution.setCommitCount(1);
        stepExecution.setFilterCount(1);
        stepExecution.setJobExecutionId(jobExecutionId);
        stepExecution.setLastUpdated(new Date());
        stepExecution.setProcessSkipCount(0);
        stepExecution.setReadCount(1);
        stepExecution.setRollbackCount(0);
        stepExecution.setReadSkipCount(0);
        stepExecution.setStepName("test_step");
        stepExecution.setStatus(BatchStatus.COMPLETED);
        stepExecution.setWriteCount(1);
        stepExecution.setWriteSkipCount(0);
        stepExecution.setVersion(1);
        return stepExecution;
    }

    public static JobInfo createJobInfo(final String jobName, final int count) {
        final JobInfo jobInfo = new JobInfo();
        jobInfo.setJobInstanceCount(count);
        jobInfo.setJobName(jobName);
        return jobInfo;
    }

    public static JobParameters createJobParameters(final Map<String, JobParameter> parameters) {
        final JobParameters jobParameters = new JobParameters();
        jobParameters.setParameters(parameters);
        return jobParameters;
    }

    public static Map<String, JobParameter> addJobParameter(final Map<String, JobParameter> parameters,
                                                            final String name,
                                                            final JobParameter jobParameter) {
        parameters.put(name, jobParameter);
        return parameters;
    }


    public static JobParameter createJobParameter(final Object value, final ParameterType parameterType) {
        final JobParameter jobParameter = new JobParameter();
        jobParameter.setParameter(value);
        jobParameter.setParameterType(parameterType);
        return jobParameter;
    }

}
