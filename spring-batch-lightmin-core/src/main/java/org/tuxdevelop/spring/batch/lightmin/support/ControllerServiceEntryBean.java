package org.tuxdevelop.spring.batch.lightmin.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.service.AdminService;

import java.util.Collection;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Component
public class ControllerServiceEntryBean implements ServiceEntryBean, AdminService {

    private final AdminService adminService;

    @Autowired
    public ControllerServiceEntryBean(final AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public void saveJobConfiguration(final JobConfiguration jobConfiguration) {
        adminService.saveJobConfiguration(jobConfiguration);
    }

    @Override
    public void updateJobConfiguration(final JobConfiguration jobConfiguration) {
        final JobConfiguration fetchedJobConfiguration = adminService.getJobConfigurationById(jobConfiguration
                .getJobConfigurationId());
        jobConfiguration.getJobSchedulerConfiguration().setBeanName(fetchedJobConfiguration
                .getJobSchedulerConfiguration().getBeanName());
        adminService.updateJobConfiguration(jobConfiguration);
    }

    @Override
    public void deleteJobConfiguration(final Long jobConfigurationId) {
        adminService.deleteJobConfiguration(jobConfigurationId);
    }

    @Override
    public Collection<JobConfiguration> getJobConfigurationsByJobName(final String jobName) {
        return adminService.getJobConfigurationsByJobName(jobName);
    }

    @Override
    public Map<String, Collection<JobConfiguration>> getJobConfigurationMap(final Collection<String> jobNames) {
        return adminService.getJobConfigurationMap(jobNames);
    }

    @Override
    public Collection<JobConfiguration> getJobConfigurations(final Collection<String> jobNames) {
        return adminService.getJobConfigurations(jobNames);
    }

    @Override
    public JobConfiguration getJobConfigurationById(final Long jobConfigurationId) {
        return adminService.getJobConfigurationById(jobConfigurationId);
    }

    @Override
    public void stopJobConfigurationScheduler(final Long jobConfigurationId) {
        adminService.stopJobConfigurationScheduler(jobConfigurationId);
    }

    @Override
    public void startJobConfigurationScheduler(final Long jobConfigurationId) {
        adminService.startJobConfigurationScheduler(jobConfigurationId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        assert adminService != null;
    }
}
