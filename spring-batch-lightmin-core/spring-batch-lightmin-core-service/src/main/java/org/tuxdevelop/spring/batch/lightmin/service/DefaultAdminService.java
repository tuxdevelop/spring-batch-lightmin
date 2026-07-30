package org.tuxdevelop.spring.batch.lightmin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminCoreConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.validation.validator.CronExpressionValidator;
import org.tuxdevelop.spring.batch.lightmin.validation.validator.PathValidator;

import jakarta.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Marcel Becker
 * @since 0.1
 * Default implementation of {@link AdminService}
 */
@Slf4j
public class DefaultAdminService implements AdminService {

    private final JobConfigurationRepository jobConfigurationRepository;
    private final SchedulerService schedulerService;
    private final ListenerService listenerService;
    private final SpringBatchLightminCoreConfigurationProperties springBatchLightminCoreConfigurationProperties;

    public DefaultAdminService(final JobConfigurationRepository jobConfigurationRepository,
                               final SchedulerService schedulerService,
                               final ListenerService listenerService,
                               final SpringBatchLightminCoreConfigurationProperties springBatchLightminCoreConfigurationProperties
    ) {
        this.jobConfigurationRepository = jobConfigurationRepository;
        this.schedulerService = schedulerService;
        this.listenerService = listenerService;
        this.springBatchLightminCoreConfigurationProperties = springBatchLightminCoreConfigurationProperties;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, transactionManager = "lightminTransactionManager")
    public void saveJobConfiguration(@Valid final JobConfiguration jobConfiguration) {
        jobConfiguration.validateForSave();
        if (jobConfiguration.getJobSchedulerConfiguration() != null) {
            saveSchedulerConfiguration(jobConfiguration);
        } else if (jobConfiguration.getJobListenerConfiguration() != null) {
            saveListenerConfiguration(jobConfiguration);
        }
    }

    private void saveSchedulerConfiguration(final JobConfiguration addedJobConfiguration) {
        final String beanName = this.schedulerService.registerSchedulerForJob(addedJobConfiguration);
        addedJobConfiguration.getJobSchedulerConfiguration().setBeanName(beanName);
        try {
            this.jobConfigurationRepository.add(addedJobConfiguration,
                    this.springBatchLightminCoreConfigurationProperties.getApplicationName());
            if (SchedulerStatus.RUNNING.equals(addedJobConfiguration.getJobSchedulerConfiguration().getSchedulerStatus())) {
                try {
                    this.schedulerService.schedule(beanName, Boolean.TRUE);
                } catch (Exception e) {
                    this.schedulerService.unregisterSchedulerForJob(beanName);
                    throw new SpringBatchLightminConfigurationException(e, "The Application could not register bean with name " + beanName + ".\n Please check configuration of the Scheduler.");
                }
            } else {
                log.info("Scheduler not started, no scheduling triggered!");
            }
        } catch (final Exception e) {
            log.error(e.getMessage());
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    private void saveListenerConfiguration(final JobConfiguration addedJobConfiguration) {
        final String beanName = this.listenerService.registerListenerForJob(addedJobConfiguration);
        addedJobConfiguration.getJobListenerConfiguration().setBeanName(beanName);
        if (ListenerStatus.ACTIVE.equals(addedJobConfiguration.getJobListenerConfiguration().getListenerStatus())) {
            try {
                this.listenerService.activateListener(beanName, Boolean.TRUE);
            } catch (Exception e) {
                this.listenerService.unregisterListenerForJob(beanName);
                throw new SpringBatchLightminConfigurationException(e, "The Application could not register bean with name " + beanName + ".\n Please check configuration of the Listener.");
            }
        }
        try {
            this.jobConfigurationRepository.add(addedJobConfiguration,
                    this.springBatchLightminCoreConfigurationProperties.getApplicationName());
        } catch (final Exception e) {
            log.error(e.getMessage());
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, transactionManager = "lightminTransactionManager")
    public void updateJobConfiguration(final JobConfiguration jobConfiguration) {
        jobConfiguration.validateForUpdate();
        try {
            final JobConfiguration existingJobConfiguration = this.jobConfigurationRepository.getJobConfiguration(jobConfiguration.getJobConfigurationId(),
                    this.springBatchLightminCoreConfigurationProperties.getApplicationName());
            if (existingJobConfiguration.getJobSchedulerConfiguration() != null) {
                final String existingBeanName = existingJobConfiguration.getJobSchedulerConfiguration().getBeanName();
                jobConfiguration.getJobSchedulerConfiguration().setBeanName(existingBeanName);
                this.jobConfigurationRepository.update(jobConfiguration,
                        this.springBatchLightminCoreConfigurationProperties.getApplicationName());
                this.schedulerService.refreshSchedulerForJob(jobConfiguration);
                if (SchedulerStatus.RUNNING.equals(jobConfiguration.getJobSchedulerConfiguration().getSchedulerStatus())) {
                    this.schedulerService.schedule(jobConfiguration.getJobSchedulerConfiguration().getBeanName(), Boolean.TRUE);
                } else {
                    log.info("Scheduler not started, no scheduling triggered!");
                }
            } else if (existingJobConfiguration.getJobListenerConfiguration() != null) {
                final String existingBeanName = existingJobConfiguration.getJobListenerConfiguration().getBeanName();
                jobConfiguration.getJobListenerConfiguration().setBeanName(existingBeanName);
                this.jobConfigurationRepository.update(jobConfiguration,
                        this.springBatchLightminCoreConfigurationProperties.getApplicationName());
                this.listenerService.refreshListenerForJob(jobConfiguration);
            }
        } catch (final NoSuchJobConfigurationException e) {
            log.error(e.getMessage());
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, transactionManager = "lightminTransactionManager")
    public void deleteJobConfiguration(final Long jobConfigurationId) {
        try {
            final JobConfiguration jobConfiguration = this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId,
                    this.springBatchLightminCoreConfigurationProperties.getApplicationName());
            final JobSchedulerConfiguration jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
            final JobListenerConfiguration jobListenerConfiguration = jobConfiguration.getJobListenerConfiguration();
            if (jobSchedulerConfiguration != null) {
                final String beanName = jobSchedulerConfiguration.getBeanName();
                this.schedulerService.terminate(beanName);
                this.schedulerService.unregisterSchedulerForJob(beanName);
                this.jobConfigurationRepository.delete(jobConfiguration,
                        this.springBatchLightminCoreConfigurationProperties.getApplicationName());
            }
            if (jobListenerConfiguration != null) {
                final String beanName = jobListenerConfiguration.getBeanName();
                this.listenerService.terminateListener(beanName);
                this.jobConfigurationRepository.delete(jobConfiguration,
                        this.springBatchLightminCoreConfigurationProperties.getApplicationName());
            }
        } catch (final NoSuchJobConfigurationException e) {
            log.error(e.getMessage());
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    public Collection<JobConfiguration> getJobConfigurationsByJobName(final String jobName) {
        try {
            return this.jobConfigurationRepository.getJobConfigurations(jobName,
                    this.springBatchLightminCoreConfigurationProperties.getApplicationName());
        } catch (final NoSuchJobException | NoSuchJobConfigurationException e) {
            final String message = "No Job with name: " + jobName + " is registered";
            log.error(message);
            throw new SpringBatchLightminApplicationException(e, message);
        }
    }

    @Override
    public Map<String, Collection<JobConfiguration>> getJobConfigurationMap(final Collection<String> jobNames) {
        final Map<String, Collection<JobConfiguration>> jobConfigurationMap = new HashMap<>();
        final Collection<JobConfiguration> jobConfigurations = this.jobConfigurationRepository.getAllJobConfigurationsByJobNames(jobNames,
                this.springBatchLightminCoreConfigurationProperties.getApplicationName());
        this.attachSchedulerStatus(jobConfigurations);
        log.info("Fetched " + jobConfigurations.size() + " JobConfigurations");
        for (final JobConfiguration jobConfiguration : jobConfigurations) {
            final String jobName = jobConfiguration.getJobName();
            if (!jobConfigurationMap.containsKey(jobName)) {
                jobConfigurationMap.put(jobName, new HashSet<>());
            }
            log.info("add " + jobConfiguration + " to result set");
            jobConfigurationMap.get(jobName).add(jobConfiguration);
        }
        return jobConfigurationMap;
    }

    /**
     * Returns all Jobconfiguration, which:
     * 1.) Are period
     * 2.) Have a valid Cron expression (Non-Null, 6 config params, expression valid)
     *
     * @param jobNames names of the {@link org.springframework.batch.core.Job}s, to get the configurations for
     * @return
     */
    @Override
    public Collection<JobConfiguration> getJobConfigurations(final Collection<String> jobNames) {
        Collection<JobConfiguration> allJobConfigurationsByJobNames = this.jobConfigurationRepository.getAllJobConfigurationsByJobNames(jobNames,
                this.springBatchLightminCoreConfigurationProperties.getApplicationName());
        allJobConfigurationsByJobNames = allJobConfigurationsByJobNames.stream().filter(DefaultAdminService::validateJobConfiguration).collect(Collectors.toCollection(LinkedList::new));
        return allJobConfigurationsByJobNames;
    }

    private static boolean validateJobConfiguration(JobConfiguration jobConfiguration) {
        if (jobConfiguration.getJobListenerConfiguration() != null) {
            PathValidator validator = new PathValidator();
            String sourceFolder = jobConfiguration.getJobListenerConfiguration().getSourceFolder();
            return validator.isValid(sourceFolder, null);
        } else {
            if (jobConfiguration.getJobSchedulerConfiguration().getJobSchedulerType().equals(org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType.CRON)) {
                CronExpressionValidator cronExpressionValidator = new CronExpressionValidator();
                String cronExpression = jobConfiguration.getJobSchedulerConfiguration().getCronExpression();
                return cronExpressionValidator.isValid(cronExpression, null);
            } else return true;
        }
    }

    @Override
    public JobConfiguration getJobConfigurationById(final Long jobConfigurationId) {
        try {
            final JobConfiguration jobConfiguration = this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId,
                    this.springBatchLightminCoreConfigurationProperties.getApplicationName());
            this.attachSchedulerStatus(jobConfiguration);
            return jobConfiguration;
        } catch (final NoSuchJobConfigurationException e) {
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, transactionManager = "lightminTransactionManager")
    public void stopJobConfiguration(final Long jobConfigurationId) {
        try {
            final JobConfiguration jobConfiguration = this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId,
                    this.springBatchLightminCoreConfigurationProperties.getApplicationName());
            final JobSchedulerConfiguration jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
            final JobListenerConfiguration jobListenerConfiguration = jobConfiguration.getJobListenerConfiguration();
            if (jobSchedulerConfiguration != null) {
                final String beanName = jobSchedulerConfiguration.getBeanName();
                this.schedulerService.terminate(beanName);
                jobConfiguration.getJobSchedulerConfiguration().setSchedulerStatus(SchedulerStatus.STOPPED);
                this.jobConfigurationRepository.update(jobConfiguration,
                        this.springBatchLightminCoreConfigurationProperties.getApplicationName());
            } else if (jobListenerConfiguration != null) {
                final String beanName = jobListenerConfiguration.getBeanName();
                this.listenerService.terminateListener(beanName);
                jobConfiguration.getJobListenerConfiguration().setListenerStatus(ListenerStatus.STOPPED);
                this.jobConfigurationRepository.update(jobConfiguration,
                        this.springBatchLightminCoreConfigurationProperties.getApplicationName());
            }
        } catch (final NoSuchJobConfigurationException e) {
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, transactionManager = "lightminTransactionManager")
    public void startJobConfiguration(final Long jobConfigurationId) {
        try {
            final JobConfiguration jobConfiguration = this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId,
                    this.springBatchLightminCoreConfigurationProperties.getApplicationName());
            final JobSchedulerConfiguration jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
            final JobListenerConfiguration jobListenerConfiguration = jobConfiguration.getJobListenerConfiguration();
            if (jobSchedulerConfiguration != null) {
                final String beanName = jobSchedulerConfiguration.getBeanName();
                this.schedulerService.schedule(beanName, Boolean.FALSE);
                jobConfiguration.getJobSchedulerConfiguration().setSchedulerStatus(SchedulerStatus.RUNNING);
                this.jobConfigurationRepository.update(jobConfiguration,
                        this.springBatchLightminCoreConfigurationProperties.getApplicationName());
            } else if (jobListenerConfiguration != null) {
                final String beanName = jobListenerConfiguration.getBeanName();
                this.listenerService.activateListener(beanName, Boolean.FALSE);
                jobConfiguration.getJobListenerConfiguration().setListenerStatus(ListenerStatus.ACTIVE);
                this.jobConfigurationRepository.update(jobConfiguration,
                        this.springBatchLightminCoreConfigurationProperties.getApplicationName());
            }
        } catch (final NoSuchJobConfigurationException e) {
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() {
        assert this.jobConfigurationRepository != null;
        assert this.schedulerService != null;
    }

    private void attachSchedulerStatus(final Collection<JobConfiguration> jobConfigurations) {
        for (final JobConfiguration jobConfiguration : jobConfigurations) {
            this.attachSchedulerStatus(jobConfiguration);
        }
    }

    private void attachSchedulerStatus(final JobConfiguration jobConfiguration) {
        if (jobConfiguration.getJobSchedulerConfiguration() != null) {
            final String schedulerName = jobConfiguration.getJobSchedulerConfiguration().getBeanName();
            final SchedulerStatus schedulerStatus = this.schedulerService.getSchedulerStatus(schedulerName);
            jobConfiguration.getJobSchedulerConfiguration().setSchedulerStatus(schedulerStatus);
        }
    }
}
