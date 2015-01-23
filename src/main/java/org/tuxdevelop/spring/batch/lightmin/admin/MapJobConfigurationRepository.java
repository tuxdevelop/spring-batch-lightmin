package org.tuxdevelop.spring.batch.lightmin.admin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapJobConfigurationRepository implements JobConfigurationRepository{

	private Map<String, Set<JobConfiguration>> jobConfigurations;

	public MapJobConfigurationRepository() {
		jobConfigurations = new HashMap<String, Set<JobConfiguration>>();
	}

	@Override
	public void add(final JobConfiguration jobConfiguration) {
		final String jobName = jobConfiguration.getJobName();
		if (jobName == null) {
			throw new IllegalArgumentException("jobName must not be null!");
		}
		if (jobConfigurations.containsKey(jobName)) {
			jobConfigurations.get(jobName).add(jobConfiguration);
		} else {
			final Set<JobConfiguration> jobConfigurationSet = new HashSet<JobConfiguration>();
			jobConfigurationSet.add(jobConfiguration);
			jobConfigurations.put(jobName, jobConfigurationSet);
		}
	}

	@Override
	public void delete(final JobConfiguration jobConfiguration) {
		final String jobName = jobConfiguration.getJobName();
		final Long jobConfigurationId = jobConfiguration.getJobConfigurationId();
		if (jobName == null) {
			throw new IllegalArgumentException("jobName must not be null!");
		}
		JobConfiguration jobConfigurationToDelete = null;
		if (jobConfigurations.containsKey(jobName)) {
			final Set<JobConfiguration> jobConfigurationSet = jobConfigurations.get(jobName);
			for (final JobConfiguration jc : jobConfigurationSet) {
				if (jc.getJobConfigurationId().equals(jobConfigurationId)) {
					jobConfigurationToDelete = jc;
					break;
				}
			}
			if (jobConfigurationToDelete != null) {
				jobConfigurationSet.remove(jobConfigurationToDelete);
			} else {
				log.info("No JobConfiguration found for id: " + jobConfigurationId + ". Nothing to delete");
			}
		} else {
			log.info("No configuration found for job: " + jobName + ". Nothing to delete");
		}

	}

}
