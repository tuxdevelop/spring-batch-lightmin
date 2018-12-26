package org.tuxdevelop.spring.batch.lightmin.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.tuxdevelop.spring.batch.lightmin.domain.TaskExecutorType;

import java.util.Date;
import java.util.Map;

@Slf4j
final class ServiceUtil {

    private ServiceUtil() {
    }

    static JobLauncher createJobLauncher(final TaskExecutorType taskExecutorType,
                                         final JobRepository jobRepository) {
        final SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        if (TaskExecutorType.ASYNCHRONOUS.equals(taskExecutorType)) {
            final SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
            jobLauncher.setTaskExecutor(taskExecutor);
        } else if (TaskExecutorType.SYNCHRONOUS.equals(taskExecutorType)) {
            final TaskExecutor taskExecutor = new SyncTaskExecutor();
            jobLauncher.setTaskExecutor(taskExecutor);
        }
        return jobLauncher;
    }

    static JobParameters mapToJobParameters(final Map<String, Object> parameters) {
        final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        if (parameters != null) {
            for (final Map.Entry<String, Object> parameter : parameters.entrySet()) {
                final String parameterName = parameter.getKey();
                final Object parameterValue = parameter.getValue();
                attachJobParameter(jobParametersBuilder, parameterName, parameterValue);
            }
        }
        return jobParametersBuilder.toJobParameters();
    }

    private static void attachJobParameter(final JobParametersBuilder jobParametersBuilder, final String parameterName,
                                           final Object parameterValue) {
        if (parameterValue instanceof Long) {
            jobParametersBuilder.addLong(parameterName, (Long) parameterValue);
        } else if (parameterValue instanceof Integer) {
            jobParametersBuilder.addLong(parameterName, Long.valueOf((Integer) parameterValue));
        } else if (parameterValue instanceof Date) {
            jobParametersBuilder.addDate(parameterName, (Date) parameterValue);
        } else if (parameterValue instanceof String) {
            jobParametersBuilder.addString(parameterName, (String) parameterValue);
        } else if (parameterValue instanceof Double) {
            jobParametersBuilder.addDouble(parameterName, (Double) parameterValue);
        } else {
            log.error("Could not add Parameter. Cause: Unsupported Parameter Type:" + parameterValue.getClass() + " !");
        }
    }

}
