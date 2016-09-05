package org.tuxdevelop.spring.batch.lightmin.service;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.1
 * Default implementation of {@link org.tuxdevelop.spring.batch.lightmin.service.AdminService}
 */
@Slf4j
public class DefaultAdminService implements AdminService {

    private static final String TEMP_BEAN_NAME = "TEMP";
    private final JobConfigurationRepository jobConfigurationRepository;
    private final SchedulerService schedulerService;
    private final ListenerService listenerService;

    public DefaultAdminService(final JobConfigurationRepository jobConfigurationRepository,
                               final SchedulerService schedulerService, final ListenerService listenerService) {
        this.jobConfigurationRepository = jobConfigurationRepository;
        this.schedulerService = schedulerService;
        this.listenerService = listenerService;
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
        final JobConfiguration addedJobConfiguration = jobConfigurationRepository.add(jobConfiguration);
        if (addedJobConfiguration.getJobSchedulerConfiguration() != null) {
            addedJobConfiguration.getJobSchedulerConfiguration().setBeanName(null);
            final String beanName = schedulerService.registerSchedulerForJob(addedJobConfiguration);
            addedJobConfiguration.getJobSchedulerConfiguration().setBeanName(beanName);
            try {
                jobConfigurationRepository.update(addedJobConfiguration);
                if (SchedulerStatus.RUNNING.equals(addedJobConfiguration.getJobSchedulerConfiguration().getSchedulerStatus())) {
                    schedulerService.schedule(beanName, Boolean.TRUE);
                } else {
                    log.info("Scheduler not started, no scheduling triggered!");
                }
            } catch (final NoSuchJobConfigurationException e) {
                log.error(e.getMessage());
                throw new SpringBatchLightminApplicationException(e, e.getMessage());
            }
        } else if (addedJobConfiguration.getJobListenerConfiguration() != null) {
            final String beanName = listenerService.registerListenerForJob(addedJobConfiguration);
            addedJobConfiguration.getJobListenerConfiguration().setBeanName(beanName);
            if (ListenerStatus.ACTIVE.equals(addedJobConfiguration.getJobListenerConfiguration().getListenerStatus())) {
                listenerService.activateListener(beanName, Boolean.TRUE);
            }
            try {
                jobConfigurationRepository.update(addedJobConfiguration);
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
            final JobConfiguration existingJobConfiguration = jobConfigurationRepository.getJobConfiguration(jobConfiguration.getJobConfigurationId());
            if (existingJobConfiguration.getJobSchedulerConfiguration() != null) {
                final String existingBeanName = existingJobConfiguration.getJobSchedulerConfiguration().getBeanName();
                jobConfiguration.getJobSchedulerConfiguration().setBeanName(existingBeanName);
                jobConfigurationRepository.update(jobConfiguration);
                schedulerService.refreshSchedulerForJob(jobConfiguration);
                if (SchedulerStatus.RUNNING.equals(jobConfiguration.getJobSchedulerConfiguration().getSchedulerStatus())) {
                    schedulerService.schedule(jobConfiguration.getJobSchedulerConfiguration().getBeanName(), Boolean.TRUE);
                } else {
                    log.info("Scheduler not started, no scheduling triggered!");
                }
            } else if (existingJobConfiguration.getJobListenerConfiguration() != null) {
                jobConfigurationRepository.update(jobConfiguration);
                listenerService.refreshListenerForJob(jobConfiguration);
            }
        } catch (final NoSuchJobConfigurationException e) {
            log.error(e.getMessage());
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }

    }

    @Override
    public void deleteJobConfiguration(final Long jobConfigurationId) {
        try {
            final JobConfiguration jobConfiguration = jobConfigurationRepository.getJobConfiguration(jobConfigurationId);
            final JobSchedulerConfiguration jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
            final JobListenerConfiguration jobListenerConfiguration = jobConfiguration.getJobListenerConfiguration();
            if (jobSchedulerConfiguration != null) {
                final String beanName = jobSchedulerConfiguration.getBeanName();
                schedulerService.terminate(beanName);
                schedulerService.unregisterSchedulerForJob(beanName);
                jobConfigurationRepository.delete(jobConfiguration);
            }
            if (jobListenerConfiguration != null) {
                final String beanName = jobListenerConfiguration.getBeanName();
                listenerService.terminateListener(beanName);
                jobConfigurationRepository.delete(jobConfiguration);
            }
        } catch (final NoSuchJobConfigurationException e) {
            log.error(e.getMessage());
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    public Collection<JobConfiguration> getJobConfigurationsByJobName(final String jobName) {
        try {
            return jobConfigurationRepository.getJobConfigurations(jobName);
        } catch (final NoSuchJobException e) {
            final String message = "No Job with name: " + jobName + " is registered";
            log.error(message);
            throw new SpringBatchLightminApplicationException(e, message);
        }
    }

    @Override
    public Map<String, Collection<JobConfiguration>> getJobConfigurationMap(final Collection<String> jobNames) {
        final Map<String, Collection<JobConfiguration>> jobConfigurationMap = new HashMap<>();
        final Collection<JobConfiguration> jobConfigurations = jobConfigurationRepository.getAllJobConfigurationsByJobNames(jobNames);
        attachSchedulerStatus(jobConfigurations);
        log.info("Fetched " + jobConfigurations.size() + " JobConfigurations");
        for (final JobConfiguration jobConfiguration : jobConfigurations) {
            final String jobName = jobConfiguration.getJobName();
            if (!jobConfigurationMap.containsKey(jobName)) {
                jobConfigurationMap.put(jobName, new HashSet<JobConfiguration>());
            }
            log.info("add " + jobConfiguration + " to result set");
            jobConfigurationMap.get(jobName).add(jobConfiguration);
        }
        return jobConfigurationMap;
    }

    @Override
    public Collection<JobConfiguration> getJobConfigurations(final Collection<String> jobNames) {
        return jobConfigurationRepository.getAllJobConfigurationsByJobNames(jobNames);
    }

    @Override
    public JobConfiguration getJobConfigurationById(final Long jobConfigurationId) {
        try {
            final JobConfiguration jobConfiguration = jobConfigurationRepository.getJobConfiguration(jobConfigurationId);
            attachSchedulerStatus(jobConfiguration);
            return jobConfiguration;
        } catch (final NoSuchJobConfigurationException e) {
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    public void stopJobConfiguration(final Long jobConfigurationId) {
        try {
            final JobConfiguration jobConfiguration = jobConfigurationRepository.getJobConfiguration(jobConfigurationId);
            final JobSchedulerConfiguration jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
            final JobListenerConfiguration jobListenerConfiguration = jobConfiguration.getJobListenerConfiguration();
            if (jobSchedulerConfiguration != null) {
                final String beanName = jobSchedulerConfiguration.getBeanName();
                schedulerService.terminate(beanName);
                jobConfiguration.getJobSchedulerConfiguration().setSchedulerStatus(SchedulerStatus.STOPPED);
                jobConfigurationRepository.update(jobConfiguration);
            } else if (jobListenerConfiguration != null) {
                final String beanName = jobListenerConfiguration.getBeanName();
                listenerService.terminateListener(beanName);
                jobConfiguration.getJobListenerConfiguration().setListenerStatus(ListenerStatus.STOPPED);
                jobConfigurationRepository.update(jobConfiguration);
            }
        } catch (final NoSuchJobConfigurationException e) {
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    public void startJobConfiguration(final Long jobConfigurationId) {
        try {
            final JobConfiguration jobConfiguration = jobConfigurationRepository.getJobConfiguration(jobConfigurationId);
            final JobSchedulerConfiguration jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
            final JobListenerConfiguration jobListenerConfiguration = jobConfiguration.getJobListenerConfiguration();
            if (jobSchedulerConfiguration != null) {
                final String beanName = jobSchedulerConfiguration.getBeanName();
                schedulerService.schedule(beanName, Boolean.FALSE);
                jobConfiguration.getJobSchedulerConfiguration().setSchedulerStatus(SchedulerStatus.RUNNING);
                jobConfigurationRepository.update(jobConfiguration);
            } else if (jobListenerConfiguration != null) {
                final String beanName = jobListenerConfiguration.getBeanName();
                listenerService.activateListener(beanName, Boolean.FALSE);
                jobConfiguration.getJobListenerConfiguration().setListenerStatus(ListenerStatus.ACTIVE);
                jobConfigurationRepository.update(jobConfiguration);
            }
        } catch (final NoSuchJobConfigurationException e) {
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() {
        assert jobConfigurationRepository != null;
        assert schedulerService != null;
    }

    private void attachSchedulerStatus(final Collection<JobConfiguration> jobConfigurations) {
        for (final JobConfiguration jobConfiguration : jobConfigurations) {
            attachSchedulerStatus(jobConfiguration);
        }
    }

    private void attachSchedulerStatus(final JobConfiguration jobConfiguration) {
        if (jobConfiguration.getJobSchedulerConfiguration() != null) {
            final String schedulerName = jobConfiguration.getJobSchedulerConfiguration().getBeanName();
            final SchedulerStatus schedulerStatus = schedulerService.getSchedulerStatus(schedulerName);
            jobConfiguration.getJobSchedulerConfiguration().setSchedulerStatus(schedulerStatus);
        }
    }
}
