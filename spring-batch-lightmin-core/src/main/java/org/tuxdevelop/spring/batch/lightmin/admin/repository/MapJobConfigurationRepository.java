package org.tuxdevelop.spring.batch.lightmin.admin.repository;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.*;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Slf4j
public class MapJobConfigurationRepository implements JobConfigurationRepository {

    private Map<String, Set<JobConfiguration>> jobConfigurations;
    private Long currentJobId;

    public MapJobConfigurationRepository() {
        jobConfigurations = new HashMap<String, Set<JobConfiguration>>();
        currentJobId = 1L;
    }

    @Override
    public JobConfiguration getJobConfiguration(final Long jobConfigurationId) throws NoSuchJobConfigurationException {
        final Set<String> jobNames = jobConfigurations.keySet();
        final Set<JobConfiguration> jobConfigurationSet = new HashSet<JobConfiguration>();
        JobConfiguration jobConfiguration = null;
        for (String jobName : jobNames) {
            final Set<JobConfiguration> configurationMap = jobConfigurations.get(jobName);
            if (configurationMap != null && !configurationMap.isEmpty()) {
                jobConfigurationSet.addAll(configurationMap);
            }
        }
        for (final JobConfiguration jc : jobConfigurationSet) {
            if (jc.getJobConfigurationId().equals(jobConfigurationId)) {
                jobConfiguration = jc;
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
            return jobConfigurations.get(jobName);
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
            throw new IllegalArgumentException("jobName must not be null!");
        }
        final Long jobConfigurationId = getNextJobId();
        jobConfiguration.setJobConfigurationId(jobConfigurationId);
        if (jobConfigurations.containsKey(jobName)) {
            jobConfigurations.get(jobName).add(jobConfiguration);
        } else {
            final Set<JobConfiguration> jobConfigurationSet = new HashSet<JobConfiguration>();
            jobConfigurationSet.add(jobConfiguration);
            jobConfigurations.put(jobName, jobConfigurationSet);
        }
        return jobConfiguration;
    }

    @Override
    public synchronized JobConfiguration update(final JobConfiguration jobConfiguration)
            throws NoSuchJobConfigurationException {
        JobConfiguration jobConfigurationToUpdate = getJobConfiguration(jobConfiguration.getJobConfigurationId());
        if (jobConfigurationToUpdate == null) {
            final String message = "No JobConfiguration found for id: " + jobConfiguration.getJobConfigurationId();
            log.error(message);
            throw new NoSuchJobConfigurationException(message);
        } else {
            jobConfigurationToUpdate = jobConfiguration;
        }
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
            final Set<JobConfiguration> jobConfigurationSet = jobConfigurations.get(jobName);
            jobConfigurationToDelete = getJobConfiguration(jobConfigurationId);
            if (jobConfigurationToDelete != null) {
                jobConfigurationSet.remove(jobConfigurationToDelete);
            } else {
                final String message = "No JobConfiguration found for id: " + jobConfigurationId + ". Nothing to " +
                        "delete";
                log.error(message);
                throw new NoSuchJobConfigurationException(message);
            }
        } else {
            final String message = "No configuration found for job: " + jobName + ". Nothing to delete";
            log.error(message);
            throw new NoSuchJobConfigurationException(message);
        }

    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurations() {
        final Collection<JobConfiguration> jobConfigurationCollection = new LinkedList<JobConfiguration>();
        for (Map.Entry<String, Set<JobConfiguration>> entry : jobConfigurations.entrySet()) {
            jobConfigurationCollection.addAll(entry.getValue());
        }
        return jobConfigurationCollection;
    }

    @Override
    public Collection<JobConfiguration> getAllJobConfigurationsByJobNames(Collection<String> jobNames) {
        final Collection<JobConfiguration> jobConfigurationCollection = new LinkedList<JobConfiguration>();
        for (final String jobName : jobNames) {
            if (jobConfigurations.containsKey(jobName)) {
                jobConfigurationCollection.addAll(jobConfigurations.get(jobName));
            } else {
                log.debug("No Configuration found for Job with name: " + jobName);
            }
        }

        return jobConfigurationCollection;
    }

    private synchronized Long getNextJobId() {
        final Long nextJobId = Long.valueOf(currentJobId);
        currentJobId++;
        return nextJobId;
    }

}
