package org.tuxdevelop.spring.batch.lightmin.service;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.execption.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.execption.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.execption.SpringBatchLightminApplicationException;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
public class AdminServiceBean implements AdminService {

    private JobConfigurationRepository jobConfigurationRepository;
    private SchedulerService schedulerService;

    public AdminServiceBean(final JobConfigurationRepository jobConfigurationRepository,
                            final SchedulerService schedulerService) {
        this.jobConfigurationRepository = jobConfigurationRepository;
        this.schedulerService = schedulerService;
    }

    @Override
    public void saveJobConfiguration(final JobConfiguration jobConfiguration) {
        jobConfiguration.validateForSave();
        final JobConfiguration addedJobConfiguration = jobConfigurationRepository.add(jobConfiguration);
        final String beanName = schedulerService.registerSchedulerForJob(jobConfiguration);
        addedJobConfiguration.getJobSchedulerConfiguration().setBeanName(beanName);
        try {
            jobConfigurationRepository.update(addedJobConfiguration);
        } catch (NoSuchJobConfigurationException e) {
            log.error(e.getMessage());
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    public void updateJobConfiguration(final JobConfiguration jobConfiguration) {
        jobConfiguration.validateForUpdate();
        try {
            jobConfigurationRepository.update(jobConfiguration);
            schedulerService.refreshSchedulerForJob(jobConfiguration);
        } catch (NoSuchJobConfigurationException e) {
            log.error(e.getMessage());
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    public void deleteJobConfiguration(final Long jobConfigurationId) {
        try {
            final JobConfiguration jobConfiguration =
                    jobConfigurationRepository.getJobConfiguration(jobConfigurationId);
            final JobSchedulerConfiguration jobSchedulerConfiguration = jobConfiguration.getJobSchedulerConfiguration();
            if (jobSchedulerConfiguration != null) {
                final String beanName = jobSchedulerConfiguration.getBeanName();
                schedulerService.unregisterSchedulerForJob(beanName);
            } else {
                log.error("Could not unregister JobScheduler, job scheduler configuration for job configuration with " +
                        "id " + jobConfigurationId + " is null!");
            }
        } catch (NoSuchJobConfigurationException e) {
            log.error(e.getMessage());
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    public Collection<JobConfiguration> getJobConfigurationsByJobName(final String jobName) {
        try {
            return jobConfigurationRepository.getJobConfigurations(jobName);
        } catch (NoSuchJobException e) {
            final String message = "No Job with name: " + jobName + "is registered";
            log.error(message);
            throw new SpringBatchLightminApplicationException(e,message);
        }
    }

    @Override
    public Map<String, Collection<JobConfiguration>> getJobConfigurationMap() {
        final Map<String, Collection<JobConfiguration>> jobConfigurationMap = new HashMap<String,
                Collection<JobConfiguration>>();
        final Collection<JobConfiguration> jobConfigurations = jobConfigurationRepository.getAllJobConfigurations();
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
    public Collection<JobConfiguration> getJobConfigurations() {
        return jobConfigurationRepository.getAllJobConfigurations();
    }

    @Override
    public JobConfiguration getJobConfigurationById(final Long jobConfigurationId) {
        try {
            return jobConfigurationRepository.getJobConfiguration(jobConfigurationId);
        } catch (NoSuchJobConfigurationException e) {
            throw new SpringBatchLightminApplicationException(e,e.getMessage());
        }
    }

    @PostConstruct
    public void publishRegisteredJobs() {
        final Collection<JobConfiguration> jobConfigurations = jobConfigurationRepository.getAllJobConfigurations();
        final String repositoryMessage = "Using " + jobConfigurationRepository.getClass().getCanonicalName() + " as " +
                "JobConfigurationRepository!";
        log.debug(repositoryMessage);
        if (jobConfigurations != null && jobConfigurations.isEmpty()) {
            for (JobConfiguration jobConfiguration : jobConfigurations) {
                schedulerService.registerSchedulerForJob(jobConfiguration);
            }
        }
        final String message = "jobConfigurations restored: " + (jobConfigurations != null ? jobConfigurations.size
                () : "null");
        log.debug(message);
    }

    @Override
    public void afterPropertiesSet(){
        assert (jobConfigurationRepository != null);
        assert (schedulerService != null);
    }


}
