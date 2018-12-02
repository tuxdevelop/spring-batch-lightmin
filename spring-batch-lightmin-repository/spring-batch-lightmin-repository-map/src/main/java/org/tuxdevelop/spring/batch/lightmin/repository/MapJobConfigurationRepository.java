package org.tuxdevelop.spring.batch.lightmin.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Slf4j
public class MapJobConfigurationRepository implements JobConfigurationRepository {

    private final ConcurrentMap<String, Map<String, Map<Long, JobConfiguration>>> jobConfigurations;
    private final AtomicLong currentJobId = new AtomicLong(1L);

    public MapJobConfigurationRepository() {
        this.jobConfigurations = new ConcurrentHashMap<>();
    }

    @Override
    public JobConfiguration getJobConfiguration(final Long jobConfigurationId, final String applicationName) throws
            NoSuchJobConfigurationException {
        final Map<String, Map<Long, JobConfiguration>> applicationJobConfigurations = this.getJobConfigurationsForApplicationName(applicationName);
        final Set<String> jobNames = applicationJobConfigurations.keySet();
        final Map<Long, JobConfiguration> tempMap = new HashMap<>();
        JobConfiguration jobConfiguration = null;
        for (final String jobName : jobNames) {
            final Map<Long, JobConfiguration> configurationMap = applicationJobConfigurations.get(jobName);
            if (configurationMap != null && !configurationMap.isEmpty()) {
                tempMap.putAll(configurationMap);
            }
        }
        for (final Map.Entry<Long, JobConfiguration> jc : tempMap.entrySet()) {
            if (jc.getKey().equals(jobConfigurationId)) {
                jobConfiguration = jc.getValue();
                break;
            }
        }
        if (jobConfiguration == null) {
            final String message = "No jobConfiguration could be found for id:" + jobConfigurationId;
            log.error(message);
            throw new NoSuchJobConfigurationException(message);
        }
        return jobConfiguration;
    }

    @Override
    public Collection<JobConfiguration> getJobConfigurations(final String jobName, final String applicationName) throws NoSuchJobException, NoSuchJobConfigurationException {
        final Map<String, Map<Long, JobConfiguration>> applicationJobConfigurations = this.getJobConfigurationsForApplicationName(applicationName);
        if (applicationJobConfigurations.containsKey(jobName)) {
            final Collection<JobConfiguration> jcs = applicationJobConfigurations.get(jobName).values();
            return new ArrayList<>(jcs);
        } else {
            final String message = "No jobConfigurations found for jobName: " + jobName;
            log.error(message);
            throw new NoSuchJobException(message);
        }
    }

    @Override
    public synchronized JobConfiguration add(final JobConfiguration jobConfiguration, final String applicationName) {
        final String jobName = jobConfiguration.getJobName();
        if (jobName == null) {
            throw new SpringBatchLightminConfigurationException("jobName must not be null!");
        }
        if (!StringUtils.hasLength(applicationName)) {
            throw new SpringBatchLightminConfigurationException("applicationName must not be null or empty");
        }
        if (!this.jobConfigurations.containsKey(applicationName)) {
            this.jobConfigurations.put(applicationName, new HashMap<>());
        }
        final Map<String, Map<Long, JobConfiguration>> jobConfigurationsMap = this.jobConfigurations.get(applicationName);
        final Long jobConfigurationId = this.getNextJobId();
        jobConfiguration.setJobConfigurationId(jobConfigurationId);
        if (jobConfigurationsMap.containsKey(jobName)) {
            jobConfigurationsMap.get(jobName).put(jobConfigurationId, jobConfiguration);
        } else {
            final Map<Long, JobConfiguration> jobConfigurationMap = new HashMap<>();
            jobConfigurationMap.put(jobConfigurationId, jobConfiguration);
            jobConfigurationsMap.put(jobName, jobConfigurationMap);
        }
        return jobConfiguration;
    }


    @Override
    public synchronized JobConfiguration update(final JobConfiguration jobConfiguration, final String applicationName)
            throws NoSuchJobConfigurationException {
        if (this.jobConfigurations.containsKey(applicationName)) {
            final Map<String, Map<Long, JobConfiguration>> applicationJobConfigurations = this.jobConfigurations.get(applicationName);
            this.getJobConfiguration(jobConfiguration.getJobConfigurationId(), applicationName);
            if (applicationJobConfigurations.containsKey(jobConfiguration.getJobName())) {
                applicationJobConfigurations.get(jobConfiguration.getJobName()).put(jobConfiguration.getJobConfigurationId(),
                        jobConfiguration);
            } else {
                this.add(jobConfiguration, applicationName);
            }
        } else {
            this.add(jobConfiguration, applicationName);
        }
        return jobConfiguration;
    }

    @Override
    public synchronized void delete(final JobConfiguration jobConfiguration, final String applicationName) throws NoSuchJobConfigurationException {
        final Map<String, Map<Long, JobConfiguration>> applicationJobConfigurations = this.jobConfigurations.get(applicationName);
        final String jobName = jobConfiguration.getJobName();
        final Long jobConfigurationId = jobConfiguration.getJobConfigurationId();
        if (jobName == null) {
            throw new SpringBatchLightminApplicationException("jobName must not be null!");
        }
        final JobConfiguration jobConfigurationToDelete;
        if (applicationJobConfigurations.containsKey(jobName)) {
            final Map<Long, JobConfiguration> jobConfigurationMap = applicationJobConfigurations.get(jobName);
            jobConfigurationToDelete = this.getJobConfiguration(jobConfigurationId, applicationName);
            jobConfigurationMap.remove(jobConfigurationToDelete.getJobConfigurationId());
            log.debug("Removed JobConfiguration with id: " + jobConfiguration.getJobConfigurationId());
        } else {
            final String message = "No configuration found for job: " + jobName + ". Nothing to delete";
            log.error(message);
            throw new NoSuchJobConfigurationException(message);
        }

    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurations(final String applicationName) {
        final Map<String, Map<Long, JobConfiguration>> applicationJobConfigurations = this.jobConfigurations.get(applicationName);
        final Collection<JobConfiguration> jobConfigurationCollection = new LinkedList<>();
        if (applicationJobConfigurations != null) {
            for (final Map.Entry<String, Map<Long, JobConfiguration>> entry : applicationJobConfigurations.entrySet()) {
                jobConfigurationCollection.addAll(entry.getValue().values());
            }
        }
        return jobConfigurationCollection;
    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurationsByJobNames(final Collection<String> jobNames, final String applicationName) {
        final Map<String, Map<Long, JobConfiguration>> applicationJobConfigurations = this.jobConfigurations.get(applicationName);
        final Collection<JobConfiguration> jobConfigurationCollection = new LinkedList<>();
        if (applicationJobConfigurations != null) {
            for (final String jobName : jobNames) {
                if (applicationJobConfigurations.containsKey(jobName)) {
                    jobConfigurationCollection.addAll(applicationJobConfigurations.get(jobName).values());
                } else {
                    log.debug("No Configuration found for Job with name: " + jobName);
                }
            }
        }
        return jobConfigurationCollection;
    }

    private synchronized Long getNextJobId() {
        return this.currentJobId.getAndIncrement();
    }

    private Map<String, Map<Long, JobConfiguration>> getJobConfigurationsForApplicationName(final String applicationName) throws NoSuchJobConfigurationException {
        final Map<String, Map<Long, JobConfiguration>> jobConfigurationMap;
        if (StringUtils.hasLength(applicationName)) {
            if (this.jobConfigurations.containsKey(applicationName)) {
                jobConfigurationMap = this.jobConfigurations.get(applicationName);
            } else {
                throw new NoSuchJobConfigurationException("Could not determine SpringBatchLightminApplication with " +
                        "name: " + applicationName);
            }
        } else {
            throw new SpringBatchLightminApplicationException("applicationName must not be null or empty");
        }
        return jobConfigurationMap;
    }

}
