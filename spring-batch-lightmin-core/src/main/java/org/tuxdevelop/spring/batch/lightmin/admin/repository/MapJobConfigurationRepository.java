package org.tuxdevelop.spring.batch.lightmin.admin.repository;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
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

    private final ConcurrentMap<String, Map<Long, JobConfiguration>> jobConfigurations;
    private final AtomicLong currentJobId = new AtomicLong(1L);

    public MapJobConfigurationRepository() {
        jobConfigurations = new ConcurrentHashMap<String, Map<Long, JobConfiguration>>();

    }

    @Override
    public JobConfiguration getJobConfiguration(final Long jobConfigurationId) throws NoSuchJobConfigurationException {
        final Set<String> jobNames = jobConfigurations.keySet();
        final Map<Long, JobConfiguration> tempMap = new HashMap<Long, JobConfiguration>();
        JobConfiguration jobConfiguration = null;
        for (final String jobName : jobNames) {
            final Map<Long, JobConfiguration> configurationMap = jobConfigurations.get(jobName);
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
    public Collection<JobConfiguration> getJobConfigurations(final String jobName) throws NoSuchJobException {
        if (jobConfigurations.containsKey(jobName)) {
            return jobConfigurations.get(jobName).values();
        } else {
            final String message = "No jobConfigurations found for jobName: " + jobName;
            log.error(message);
            throw new NoSuchJobException(message);
        }
    }

    @Override
    public synchronized JobConfiguration add(final JobConfiguration jobConfiguration) {
        final String jobName = jobConfiguration.getJobName();
        if (jobName == null) {
            throw new SpringBatchLightminConfigurationException("jobName must not be null!");
        }
        final Long jobConfigurationId = getNextJobId();
        jobConfiguration.setJobConfigurationId(jobConfigurationId);
        if (jobConfigurations.containsKey(jobName)) {
            jobConfigurations.get(jobName).put(jobConfigurationId, jobConfiguration);
        } else {
            final Map<Long, JobConfiguration> jobConfigurationMap = new HashMap<Long, JobConfiguration>();
            jobConfigurationMap.put(jobConfigurationId, jobConfiguration);
            jobConfigurations.put(jobName, jobConfigurationMap);
        }
        return jobConfiguration;
    }

    @Override
    public synchronized JobConfiguration update(final JobConfiguration jobConfiguration)
            throws NoSuchJobConfigurationException {
        JobConfiguration jobConfigurationToUpdate = getJobConfiguration(jobConfiguration.getJobConfigurationId());
        jobConfigurationToUpdate = jobConfiguration;
        return jobConfigurationToUpdate;
    }

    @Override
    public synchronized void delete(final JobConfiguration jobConfiguration) throws
            NoSuchJobConfigurationException {
        final String jobName = jobConfiguration.getJobName();
        final Long jobConfigurationId = jobConfiguration.getJobConfigurationId();
        if (jobName == null) {
            throw new SpringBatchLightminApplicationException("jobName must not be null!");
        }
        final JobConfiguration jobConfigurationToDelete;
        if (jobConfigurations.containsKey(jobName)) {
            final Map<Long, JobConfiguration> jobConfigurationMap = jobConfigurations.get(jobName);
            jobConfigurationToDelete = getJobConfiguration(jobConfigurationId);
            jobConfigurationMap.remove(jobConfigurationToDelete.getJobConfigurationId());
            log.debug("Removed JobConfiguration with id: " + jobConfiguration.getJobConfigurationId());
        } else {
            final String message = "No configuration found for job: " + jobName + ". Nothing to delete";
            log.error(message);
            throw new NoSuchJobConfigurationException(message);
        }

    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurations() {
        final Collection<JobConfiguration> jobConfigurationCollection = new LinkedList<JobConfiguration>();
        for (final Map.Entry<String, Map<Long, JobConfiguration>> entry : jobConfigurations.entrySet()) {
            jobConfigurationCollection.addAll(entry.getValue().values());
        }
        return jobConfigurationCollection;
    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurationsByJobNames(final Collection<String> jobNames) {
        final Collection<JobConfiguration> jobConfigurationCollection = new LinkedList<JobConfiguration>();
        for (final String jobName : jobNames) {
            if (jobConfigurations.containsKey(jobName)) {
                jobConfigurationCollection.addAll(jobConfigurations.get(jobName).values());
            } else {
                log.debug("No Configuration found for Job with name: " + jobName);
            }
        }

        return jobConfigurationCollection;
    }

    private synchronized Long getNextJobId() {
        final Long nextJobId = currentJobId.getAndIncrement();
        return nextJobId;
    }

}
