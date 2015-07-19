package org.tuxdevelop.spring.batch.lightmin.api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.domain.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.service.AdminService;

import java.util.Collection;

@RestController
public class JobConfigurationRestController extends AbstractRestController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS, produces = PRODUCES, method = RequestMethod.GET)
    public JobConfigurations getJobConfigurations() {
        final Collection<JobConfiguration> jobConfigurationCollection = adminService.getJobConfigurations();
        final JobConfigurations jobConfigurations = new JobConfigurations();
        jobConfigurations.setJobConfigurations(jobConfigurationCollection);
        jobConfigurations.setJobName(ALL_JOBS);
        return jobConfigurations;
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS_JOB_NAME, produces = PRODUCES, method = RequestMethod.GET)
    public JobConfigurations getJobConfigurationsByJobName(@PathVariable("jobName") final String jobName) {
        final Collection<JobConfiguration> jobConfigurationCollection = adminService.getJobConfigurationsByJobName
                (jobName);
        final JobConfigurations jobConfigurations = new JobConfigurations();
        jobConfigurations.setJobConfigurations(jobConfigurationCollection);
        jobConfigurations.setJobName(jobName);
        return jobConfigurations;
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID, produces = PRODUCES, method = RequestMethod.GET)
    public JobConfiguration getJobConfigurationById(@PathVariable("jobConfigurationId") final Long jobConfigurationId) {
        return adminService.getJobConfigurationById(jobConfigurationId);
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS, consumes = CONSUMES, method = RequestMethod.POST)
    public void addJobConfiguration(@RequestBody final JobConfiguration jobConfiguration) {
        adminService.saveJobConfiguration(jobConfiguration);
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS, consumes = CONSUMES, method = RequestMethod.PUT)
    public void updateJobConfiguration(@RequestBody final JobConfiguration jobConfiguration) {
        adminService.updateJobConfiguration(jobConfiguration);
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID, method = RequestMethod.DELETE)
    public void deleteJobConfigurationById(@PathVariable("jobConfigurationId") final Long jobConfigurationId) {
        adminService.deleteJobConfiguration(jobConfigurationId);
    }


}
