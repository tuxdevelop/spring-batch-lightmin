package org.tuxdevelop.spring.batch.lightmin.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tuxdevelop.spring.batch.lightmin.api.resource.AdminToResourceMapper;
import org.tuxdevelop.spring.batch.lightmin.api.resource.ResourceToAdminMapper;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.service.AdminService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Component
public class ControllerServiceEntryBean implements ServiceEntry {

    private final AdminService adminService;

    @Autowired
    public ControllerServiceEntryBean(final AdminService adminService) {
        this.adminService = adminService;
    }


    @Override
    public void saveJobConfiguration(final JobConfiguration jobConfiguration) {
        adminService.saveJobConfiguration(ResourceToAdminMapper.map(jobConfiguration));
    }

    @Override
    public void updateJobConfiguration(final JobConfiguration jobConfiguration) {
        adminService.updateJobConfiguration(ResourceToAdminMapper.map(jobConfiguration));
    }

    @Override
    public void deleteJobConfiguration(final Long jobConfigurationId) {
        adminService.deleteJobConfiguration(jobConfigurationId);
    }

    @Override
    public JobConfigurations getJobConfigurationsByJobName(final String jobName) {
        final Collection<org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration> jobConfigurations = adminService.getJobConfigurationsByJobName(jobName);
        return AdminToResourceMapper.map(jobConfigurations);
    }

    @Override
    public Map<String, JobConfigurations> getJobConfigurationMap(final Collection<String> jobNames) {
        final Map<String, Collection<org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration>> jobConfigurationMap = adminService.getJobConfigurationMap(jobNames);
        final Map<String, JobConfigurations> response = new HashMap<>();
        for (final Map.Entry<String, Collection<org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration>> entry : jobConfigurationMap.entrySet()) {
            final JobConfigurations jobConfigurations = AdminToResourceMapper.map(entry.getValue());
            response.put(entry.getKey(), jobConfigurations);
        }
        return response;
    }

    @Override
    public JobConfigurations getJobConfigurations(final Collection<String> jobNames) {
        return AdminToResourceMapper.map(adminService.getJobConfigurations(jobNames));
    }

    @Override
    public JobConfiguration getJobConfigurationById(final Long jobConfigurationId) {
        return AdminToResourceMapper.map(adminService.getJobConfigurationById(jobConfigurationId));
    }

    @Override
    public void stopJobConfigurationScheduler(final Long jobConfigurationId) {
        adminService.stopJobConfigurationScheduler(jobConfigurationId);
    }

    @Override
    public void startJobConfigurationScheduler(final Long jobConfigurationId) {
        adminService.startJobConfigurationScheduler(jobConfigurationId);
    }
}
