package org.tuxdevelop.spring.batch.lightmin.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.TaskExecutorType;
import org.tuxdevelop.spring.batch.lightmin.admin.scheduler.CronScheduler;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.admin.scheduler.PeriodScheduler;
import org.tuxdevelop.spring.batch.lightmin.execption.SpringBatchLightminConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.util.BeanRegistrar;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class SchedulerServiceBean implements SchedulerService {

    private BeanRegistrar beanRegistrar;
    private JobRepository jobRepository;
    private JobRegistry jobRegistry;

    public SchedulerServiceBean(final BeanRegistrar beanRegistrar, final JobRepository jobRepository,
                                final JobRegistry jobRegistry) {
        this.beanRegistrar = beanRegistrar;
        this.jobRepository = jobRepository;
        this.jobRegistry = jobRegistry;
    }

    @Override
    public String registerSchedulerForJob(final JobConfiguration jobConfiguration) {
        final JobSchedulerType schedulerType = jobConfiguration.getJobSchedulerConfiguration().getJobSchedulerType();
        final String beanName;
        switch (schedulerType) {
            case CRON:
                beanName =registerCronScheduler(jobConfiguration);
                break;
            case PERIOD:
                beanName =registerPeriodScheduler(jobConfiguration);
                break;
            default:
                throw new SpringBatchLightminConfigurationException("Unknown Scheduler Type: " + schedulerType);
        }
        return beanName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        assert (beanRegistrar != null);
        assert (jobRepository != null);
        assert (jobRegistry != null);
    }

    //TODO simplify | unify registration
    private String registerCronScheduler(final JobConfiguration jobConfiguration) {
        try {
            final Set<Object> constructorValues = new HashSet<Object>();
            final JobLauncher jobLauncher = this.createJobLauncher(jobConfiguration.getJobSchedulerConfiguration().getTaskExecutorType());
            final Job job = jobRegistry.getJob(jobConfiguration.getJobName());
            final JobParameters jobParameters = mapToJobParameters(jobConfiguration.getJobParameters());
            final JobSchedulerConfiguration jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
            final String beanName;
            if(jobSchedulerConfiguration.getBeanName() == null || jobSchedulerConfiguration.getBeanName().isEmpty()) {
                beanName= generateSchedulerBeanName(jobConfiguration.getJobName(), jobConfiguration
                                .getJobConfigurationId(),
                        jobConfiguration.getJobSchedulerConfiguration().getJobSchedulerType());
            }else{
               beanName = jobSchedulerConfiguration.getBeanName();
            }
            constructorValues.add(jobLauncher);
            constructorValues.add(job);
            constructorValues.add(jobConfiguration.getJobIncrementer());
            constructorValues.add(jobParameters);
            beanRegistrar.registerBean(CronScheduler.class, beanName, constructorValues, null, null, null, null);
            return beanName;
        } catch (Exception e) {
            throw new SpringBatchLightminConfigurationException(e, e.getMessage());
        }
    }

    private String registerPeriodScheduler(final JobConfiguration jobConfiguration) {
        try {
            final Set<Object> constructorValues = new HashSet<Object>();
            final JobLauncher jobLauncher = this.createJobLauncher(jobConfiguration.getJobSchedulerConfiguration().getTaskExecutorType());
            final Job job = jobRegistry.getJob(jobConfiguration.getJobName());
            final JobParameters jobParameters = mapToJobParameters(jobConfiguration.getJobParameters());
            final JobSchedulerConfiguration jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
            final String beanName;
            if(jobSchedulerConfiguration.getBeanName() == null || jobSchedulerConfiguration.getBeanName().isEmpty()) {
                beanName= generateSchedulerBeanName(jobConfiguration.getJobName(), jobConfiguration
                                .getJobConfigurationId(),
                        jobConfiguration.getJobSchedulerConfiguration().getJobSchedulerType());
            }else{
                beanName = jobSchedulerConfiguration.getBeanName();
            }
            constructorValues.add(jobLauncher);
            constructorValues.add(job);
            constructorValues.add(jobConfiguration.getJobIncrementer());
            constructorValues.add(jobParameters);
            beanRegistrar.registerBean(PeriodScheduler.class, beanName, constructorValues, null, null, null, null);
            return beanName;
        } catch (Exception e) {
            throw new SpringBatchLightminConfigurationException(e, e.getMessage());
        }
    }

    private JobLauncher createJobLauncher(final TaskExecutorType taskExecutorType) {
        final SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(this.jobRepository);
        if(TaskExecutorType.ASYNCHRONOUS.equals(taskExecutorType)){
            final AsyncTaskExecutor taskExecutor = new ConcurrentTaskExecutor();
            jobLauncher.setTaskExecutor(taskExecutor);
        }
        return jobLauncher;
    }

    private JobParameters mapToJobParameters(final Map<String, Object> parameters) {
        final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        if (parameters != null) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                final String parameterName = parameter.getKey();
                final Object parameterValue = parameter.getValue();
                attachJobParameter(jobParametersBuilder,parameterName,parameterValue);
            }
        }
        return jobParametersBuilder.toJobParameters();
    }

    private void attachJobParameter(final JobParametersBuilder jobParametersBuilder, final String parameterName, final
    Object parameterValue) {
        if (parameterValue instanceof Long) {
            jobParametersBuilder.addLong(parameterName, (Long) parameterValue);
        } else if (parameterValue instanceof Date) {
            jobParametersBuilder.addDate(parameterName, (Date) parameterValue);
        } else if (parameterValue instanceof String) {
            jobParametersBuilder.addString(parameterName, (String) parameterValue);
        } else {
            log.error("Could not add Parameter. Cause: Unsupported Parameter Type:" + parameterValue.getClass() + " !");
        }
    }

    private String generateSchedulerBeanName(final String jobName, final Long id,
                                             final JobSchedulerType jobSchedulerType) {
        final String beanName = jobName + jobSchedulerType.name() + id;
        return beanName;
    }


}
