package org.tuxdevelop.spring.batch.lightmin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.tuxdevelop.spring.batch.lightmin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.scheduler.CronScheduler;
import org.tuxdevelop.spring.batch.lightmin.scheduler.PeriodScheduler;
import org.tuxdevelop.spring.batch.lightmin.scheduler.Scheduler;
import org.tuxdevelop.spring.batch.lightmin.util.BeanRegistrar;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Default implementation of {@link SchedulerService}
 *
 * @author Marcel Becker
 * @since 0.1
 */
@Slf4j
public class DefaultSchedulerService implements SchedulerService {

    private static final String EXECUTOR_SUFFIX = "_executor";
    private final BeanRegistrar beanRegistrar;
    private final JobRepository jobRepository;
    private final JobRegistry jobRegistry;
    private ApplicationContext applicationContext;

    public DefaultSchedulerService(final BeanRegistrar beanRegistrar,
                                   final JobRepository jobRepository,
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
                beanName = this.registerScheduler(jobConfiguration, CronScheduler.class);
                break;
            case PERIOD:
                beanName = this.registerScheduler(jobConfiguration, PeriodScheduler.class);
                break;
            default:
                throw new SpringBatchLightminConfigurationException("Unknown Scheduler Type: " + schedulerType);
        }
        return beanName;
    }

    @Override
    public void unregisterSchedulerForJob(final String beanName) {
        //Unregister Scheduler with corresponding ThreadPoolTaskScheduler
        this.beanRegistrar.unregisterBean(beanName);
        this.beanRegistrar.unregisterBean(beanName + EXECUTOR_SUFFIX);
    }

    @Override
    public void refreshSchedulerForJob(final JobConfiguration jobConfiguration) {
        this.terminate(jobConfiguration.getJobSchedulerConfiguration().getBeanName());
        this.unregisterSchedulerForJob(jobConfiguration.getJobSchedulerConfiguration().getBeanName());
        this.registerSchedulerForJob(jobConfiguration);
    }

    @Override
    public void schedule(final String beanName, final Boolean forceScheduling) {
        if (this.applicationContext.containsBean(beanName)) {
            final Scheduler scheduler = this.applicationContext.getBean(beanName, Scheduler.class);
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
        if (this.applicationContext.containsBean(beanName)) {
            final Scheduler scheduler = this.applicationContext.getBean(beanName, Scheduler.class);
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
        if (this.applicationContext.containsBean(beanName)) {
            final Scheduler scheduler = this.applicationContext.getBean(beanName, Scheduler.class);
            status = scheduler.getSchedulerStatus();
        } else {
            throw new SpringBatchLightminConfigurationException("Could not get status for bean with name: " + beanName);
        }
        return status;
    }

    @Override
    public void afterPropertiesSet() {
        assert this.beanRegistrar != null;
        assert this.jobRepository != null;
        assert this.jobRegistry != null;
    }

    @Override
    public JobLauncher createLobLauncher(final TaskExecutorType taskExecutorType, final JobRepository jobRepository) {
        return ServiceUtil.createJobLauncher(taskExecutorType, this.jobRepository);
    }

    private String registerScheduler(final JobConfiguration jobConfiguration, final Class<?> schedulerClass) {
        try {
            final Set<Object> constructorValues = new HashSet<>();
            final JobLauncher jobLauncher = this.createLobLauncher(
                    jobConfiguration.getJobSchedulerConfiguration().getTaskExecutorType(),
                    this.jobRepository);
            final Job job = this.jobRegistry.getJob(jobConfiguration.getJobName());
            final JobParameters jobParameters = ServiceUtil.mapToJobParameters(jobConfiguration.getJobParameters());
            final JobSchedulerConfiguration jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
            final String beanName;
            if (jobSchedulerConfiguration.getBeanName() == null || jobSchedulerConfiguration.getBeanName().isEmpty()) {
                beanName = this.generateSchedulerBeanName(jobConfiguration.getJobName(),
                        jobConfiguration.getJobSchedulerConfiguration().getJobSchedulerType());
            } else {
                beanName = jobSchedulerConfiguration.getBeanName();
            }
            final ThreadPoolTaskScheduler threadPoolTaskScheduler = this.registerThreadPoolTaskScheduler(beanName);
            final SchedulerConstructorWrapper schedulerConstructorWrapper = new SchedulerConstructorWrapper();
            schedulerConstructorWrapper.setJobParameters(jobParameters);
            schedulerConstructorWrapper.setJob(job);
            schedulerConstructorWrapper.setJobLauncher(jobLauncher);
            schedulerConstructorWrapper.setJobIncrementer(jobConfiguration.getJobIncrementer());
            schedulerConstructorWrapper.setJobConfiguration(jobConfiguration);
            schedulerConstructorWrapper.setThreadPoolTaskScheduler(threadPoolTaskScheduler);
            constructorValues.add(schedulerConstructorWrapper);
            this.beanRegistrar.registerBean(
                    schedulerClass,
                    beanName,
                    constructorValues,
                    null,
                    null,
                    null,
                    null);
            return beanName;
        } catch (final Exception e) {
            throw new SpringBatchLightminConfigurationException(e, e.getMessage());
        }
    }

    private String generateSchedulerBeanName(final String jobName,
                                             final JobSchedulerType jobSchedulerType) {
        return jobName + "-" + jobSchedulerType.name() + "-" + UUID.randomUUID();
    }

    private ThreadPoolTaskScheduler registerThreadPoolTaskScheduler(final String jobConfigurationBeanName) {
        final String beanName = jobConfigurationBeanName + EXECUTOR_SUFFIX;
        this.beanRegistrar.registerBean(
                ThreadPoolTaskScheduler.class,
                beanName,
                null,
                null,
                null,
                null,
                null
        );
        return this.applicationContext.getBean(beanName, ThreadPoolTaskScheduler.class);
    }
}
