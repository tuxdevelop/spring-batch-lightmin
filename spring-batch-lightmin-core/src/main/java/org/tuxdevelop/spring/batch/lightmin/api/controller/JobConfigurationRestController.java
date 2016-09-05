package org.tuxdevelop.spring.batch.lightmin.api.controller;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.support.ServiceEntry;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@ResponseBody
@RequestMapping("/")
public class JobConfigurationRestController extends AbstractRestController implements InitializingBean {

    private final ServiceEntry serviceEntry;
    private final JobRegistry jobRegistry;

    public JobConfigurationRestController(final ServiceEntry serviceEntry, final JobRegistry jobRegistry) {
        this.serviceEntry = serviceEntry;
        this.jobRegistry = jobRegistry;
    }


    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS, produces = PRODUCES, method = RequestMethod.GET)
    public ResponseEntity<JobConfigurations> getJobConfigurations() {
        final JobConfigurations jobConfigurations = serviceEntry.getJobConfigurations(jobRegistry.getJobNames());
        return ResponseEntity.ok(jobConfigurations);
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS_JOB_NAME, produces = PRODUCES, method = RequestMethod.GET)
    public ResponseEntity<JobConfigurations> getJobConfigurationsByJobName(@PathVariable("jobname") final String jobName) {
        final JobConfigurations jobConfigurations = serviceEntry.getJobConfigurationsByJobName(jobName);
        return ResponseEntity.ok(jobConfigurations);
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID, produces = PRODUCES, method = RequestMethod.GET)
    public ResponseEntity<JobConfiguration> getJobConfigurationById(@PathVariable("jobconfigurationid") final Long jobConfigurationId) {
        final JobConfiguration jobConfiguration = serviceEntry.getJobConfigurationById(jobConfigurationId);
        return ResponseEntity.ok(jobConfiguration);
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS, consumes = CONSUMES, method = RequestMethod.POST)
    public ResponseEntity<Void> addJobConfiguration(@RequestBody final JobConfiguration jobConfiguration) {
        serviceEntry.saveJobConfiguration(jobConfiguration);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS, consumes = CONSUMES, method = RequestMethod.PUT)
    public ResponseEntity<Void> updateJobConfiguration(@RequestBody final JobConfiguration jobConfiguration) {
        serviceEntry.updateJobConfiguration(jobConfiguration);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteJobConfigurationById(@PathVariable("jobconfigurationid") final Long jobConfigurationId) {
        serviceEntry.deleteJobConfiguration(jobConfigurationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATION_START, method = RequestMethod.GET)
    public ResponseEntity<Void> startJobConfiguration(@PathVariable("jobconfigurationid") final Long jobConfigurationId) {
        serviceEntry.startJobConfiguration(jobConfigurationId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATION_STOP, method = RequestMethod.GET)
    public ResponseEntity<Void> stopJobConfiguration(@PathVariable("jobconfigurationid") final Long jobConfigurationId) {
        serviceEntry.stopJobConfiguration(jobConfigurationId);
        return ResponseEntity.ok().build();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        assert serviceEntry != null;
        assert jobRegistry != null;
    }
}
