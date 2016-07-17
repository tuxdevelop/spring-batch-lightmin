package org.tuxdevelop.spring.batch.lightmin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.admin.scheduler.CronScheduler;
import org.tuxdevelop.spring.batch.lightmin.admin.scheduler.PeriodScheduler;
import org.tuxdevelop.spring.batch.lightmin.admin.scheduler.Scheduler;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.util.BeanRegistrar;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class DefaultSchedulerService implements SchedulerService {

    private ApplicationContext applicationContext;

    private final BeanRegistrar beanRegistrar;
    private final JobRepository jobRepository;
    private final JobRegistry jobRegistry;

    public DefaultSchedulerService(final BeanRegistrar beanRegistrar, final JobRepository jobRepository,
                                   final JobRegistry jobRegistry) {
        this.beanRegistrar = beanRegistrar;
        this.jobRepository = jobRepository;
        this.jobRegistry = jobRegistry;
    }

    @Autowired
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public String registerSchedulerForJob(final JobConfiguration jobConfiguration) {
        final JobSchedulerType schedulerType = jobConfiguration.getJobSchedulerConfiguration().getJobSchedulerType();
        final String beanName;
        switch (schedulerType) {
            case CRON:
                beanName = registerScheduler(jobConfiguration, CronScheduler.class);
                break;
            case PERIOD:
                beanName = registerScheduler(jobConfiguration, PeriodScheduler.class);
                break;
            default:
                throw new SpringBatchLightminConfigurationException("Unknown Scheduler Type: " + schedulerType);
        }
        return beanName;
    }

    @Override
    public void unregisterSchedulerForJob(final String beanName) {
        beanRegistrar.unregisterBean(beanName);
    }

    @Override
    public void refreshSchedulerForJob(final JobConfiguration jobConfiguration) {
        terminate(jobConfiguration.getJobSchedulerConfiguration().getBeanName());
        unregisterSchedulerForJob(jobConfiguration.getJobSchedulerConfiguration().getBeanName());
        registerSchedulerForJob(jobConfiguration);
    }

    @Override
    public void schedule(final String beanName, final Boolean forceScheduling) {
        if (applicationContext.containsBean(beanName)) {
            final Scheduler scheduler = applicationContext.getBean(beanName, Scheduler.class);
            if (scheduler.getSchedulerStatus().equals(SchedulerStatus.RUNNING)
                    && Boolean.FALSE.equals(forceScheduling)) {
                log.info("Scheduler: " + beanName + " already running");
            } else {
                scheduler.schedule();
            }
        } else {
            throw new SpringBatchLightminConfigurationException("Could not schedule bean with name: " + beanName);
        }
    }

    @Override
    public void terminate(final String beanName) {
        if (applicationContext.containsBean(beanName)) {
            final Scheduler scheduler = applicationContext.getBean(beanName, Scheduler.class);
            if (scheduler.getSchedulerStatus().equals(SchedulerStatus.STOPPED)) {
                log.info("Scheduler: " + beanName + " already terminated");
            } else {
                scheduler.terminate();
            }
        } else {
            throw new SpringBatchLightminConfigurationException("Could not terminate bean with name: " + beanName);
        }
    }

    @Override
    public SchedulerStatus getSchedulerStatus(final String beanName) {
        final SchedulerStatus status;
        if (applicationContext.containsBean(beanName)) {
            final Scheduler scheduler = applicationContext.getBean(beanName, Scheduler.class);
            status = scheduler.getSchedulerStatus();
        } else {
            throw new SpringBatchLightminConfigurationException("Could not get status for bean with name: " + beanName);
        }
        return status;
    }

    @Override
    public void afterPropertiesSet() {
        assert beanRegistrar != null;
        assert jobRepository != null;
        assert jobRegistry != null;
    }

    String registerScheduler(final JobConfiguration jobConfiguration, final Class<?> schedulerClass) {
        try {
            final Set<Object> constructorValues = new HashSet<Object>();
            final JobLauncher jobLauncher = createJobLauncher(jobConfiguration.getJobSchedulerConfiguration()
                    .getTaskExecutorType());
            final Job job = jobRegistry.getJob(jobConfiguration.getJobName());
            final JobParameters jobParameters = mapToJobParameters(jobConfiguration.getJobParameters());
            final JobSchedulerConfiguration jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
            final String beanName;
            if (jobSchedulerConfiguration.getBeanName() == null || jobSchedulerConfiguration.getBeanName().isEmpty()) {
                beanName = generateSchedulerBeanName(jobConfiguration.getJobName(),
                        jobConfiguration.getJobConfigurationId(), jobConfiguration.getJobSchedulerConfiguration()
                                .getJobSchedulerType());
            } else {
                beanName = jobSchedulerConfiguration.getBeanName();
            }
            final SchedulerConstructorWrapper schedulerConstructorWrapper = new SchedulerConstructorWrapper();
            schedulerConstructorWrapper.setJobParameters(jobParameters);
            schedulerConstructorWrapper.setJob(job);
            schedulerConstructorWrapper.setJobLauncher(jobLauncher);
            schedulerConstructorWrapper.setJobIncrementer(jobConfiguration.getJobIncrementer());
            schedulerConstructorWrapper.setJobConfiguration(jobConfiguration);
            constructorValues.add(schedulerConstructorWrapper);
            beanRegistrar.registerBean(schedulerClass, beanName, constructorValues, null, null, null, null);
            return beanName;
        } catch (final Exception e) {
            throw new SpringBatchLightminConfigurationException(e, e.getMessage());
        }
    }

    private JobLauncher createJobLauncher(final TaskExecutorType taskExecutorType) {
        final SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        if (TaskExecutorType.ASYNCHRONOUS.equals(taskExecutorType)) {
            final AsyncTaskExecutor taskExecutor = new ConcurrentTaskExecutor();
            jobLauncher.setTaskExecutor(taskExecutor);
        } else if (TaskExecutorType.SYNCHRONOUS.equals(taskExecutorType)) {
            final TaskExecutor taskExecutor = new SyncTaskExecutor();
            jobLauncher.setTaskExecutor(taskExecutor);
        }
        return jobLauncher;
    }

    private JobParameters mapToJobParameters(final Map<String, Object> parameters) {
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

    private void attachJobParameter(final JobParametersBuilder jobParametersBuilder, final String parameterName,
                                    final Object parameterValue) {
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
        return jobName + jobSchedulerType.name() + id;
    }

}
