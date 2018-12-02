package org.tuxdevelop.spring.batch.lightmin.service;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminCoreConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.1
 * Default implementation of {@link AdminService}
 */
@Slf4j
public class DefaultAdminService implements AdminService {

    private static final String TEMP_BEAN_NAME = "TEMP";
    private final JobConfigurationRepository jobConfigurationRepository;
    private final SchedulerService schedulerService;
    private final ListenerService listenerService;
    private final SpringBatchLightminCoreConfigurationProperties springBatchLightminCoreConfigurationProperties;

    public DefaultAdminService(final JobConfigurationRepository jobConfigurationRepository,
                               final SchedulerService schedulerService,
                               final ListenerService listenerService,
                               final SpringBatchLightminCoreConfigurationProperties springBatchLightminCoreConfigurationProperties) {
        this.jobConfigurationRepository = jobConfigurationRepository;
        this.schedulerService = schedulerService;
        this.listenerService = listenerService;
        this.springBatchLightminCoreConfigurationProperties = springBatchLightminCoreConfigurationProperties;
    }

    @Override
    public void saveJobConfiguration(final JobConfiguration jobConfiguration) {
        jobConfiguration.validateForSave();
        if (jobConfiguration.getJobSchedulerConfiguration() != null) {
            jobConfiguration.getJobSchedulerConfiguration().setBeanName(TEMP_BEAN_NAME + "_SCHEDULER_" + jobConfiguration.getJobConfigurationId());
        } else if (jobConfiguration.getJobListenerConfiguration() != null) {
            jobConfiguration.getJobListenerConfiguration().setBeanName(TEMP_BEAN_NAME + "_LISTENER_" + jobConfiguration
                    .getJobConfigurationId());
        }
        final JobConfiguration addedJobConfiguration = this.jobConfigurationRepository.add(jobConfiguration,
                this.springBatchLightminCoreConfigurationProperties.getApplicationName());
        if (addedJobConfiguration.getJobSchedulerConfiguration() != null) {
            addedJobConfiguration.getJobSchedulerConfiguration().setBeanName(null);
            final String beanName = this.schedulerService.registerSchedulerForJob(addedJobConfiguration);
            addedJobConfiguration.getJobSchedulerConfiguration().setBeanName(beanName);
            try {
                this.jobConfigurationRepository.update(addedJobConfiguration,
                        this.springBatchLightminCoreConfigurationProperties.getApplicationName());
                if (SchedulerStatus.RUNNING.equals(addedJobConfiguration.getJobSchedulerConfiguration().getSchedulerStatus())) {
                    this.schedulerService.schedule(beanName, Boolean.TRUE);
                } else {
                    log.info("Scheduler not started, no scheduling triggered!");
                }
            } catch (final NoSuchJobConfigurationException e) {
                log.error(e.getMessage());
                throw new SpringBatchLightminApplicationException(e, e.getMessage());
            }
        } else if (addedJobConfiguration.getJobListenerConfiguration() != null) {
            final String beanName = this.listenerService.registerListenerForJob(addedJobConfiguration);
            addedJobConfiguration.getJobListenerConfiguration().setBeanName(beanName);
            if (ListenerStatus.ACTIVE.equals(addedJobConfiguration.getJobListenerConfiguration().getListenerStatus())) {
                this.listenerService.activateListener(beanName, Boolean.TRUE);
            }
            try {
                this.jobConfigurationRepository.update(addedJobConfiguration,
                        this.springBatchLightminCoreConfigurationProperties.getApplicationName());
            } catch (final NoSuchJobConfigurationException e) {
                log.error(e.getMessage());
                throw new SpringBatchLightminApplicationException(e, e.getMessage());
            }
        }
    }

    @Override
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

    @Override
    public Collection<JobConfiguration> getJobConfigurations(final Collection<String> jobNames) {
        return this.jobConfigurationRepository.getAllJobConfigurationsByJobNames(jobNames,
                this.springBatchLightminCoreConfigurationProperties.getApplicationName());
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
