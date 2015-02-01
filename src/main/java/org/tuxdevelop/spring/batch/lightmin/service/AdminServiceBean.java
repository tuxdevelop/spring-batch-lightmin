package org.tuxdevelop.spring.batch.lightmin.service;

import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.JobConfigurationRepository;

import java.util.Collection;

public class AdminServiceBean implements AdminService {

    private JobConfigurationRepository jobConfigurationRepository;
    private SchedulerService schedulerService;

    public AdminServiceBean(final JobConfigurationRepository jobConfigurationRepository, final SchedulerService schedulerService) {
        this.jobConfigurationRepository = jobConfigurationRepository;
        this.schedulerService = schedulerService;
    }

    @Override
    public void registerSchedulerForJob(final JobConfiguration jobConfiguration) {
        schedulerService.registerSchedulerForJob(jobConfiguration);
    }

    @Override
    public void saveJobConfiguration(final JobConfiguration jobConfiguration) {
        jobConfigurationRepository.add(jobConfiguration);
    }

    @Override
    public Collection<JobConfiguration> getJobConfigurationsByJobName(final String jobName) {
        //TODO implement me
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //TODO implement me
    }


}
