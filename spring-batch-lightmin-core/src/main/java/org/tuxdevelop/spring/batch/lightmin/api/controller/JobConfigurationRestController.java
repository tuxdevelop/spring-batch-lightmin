package org.tuxdevelop.spring.batch.lightmin.api.controller;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.support.ControllerServiceEntryBean;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@RestController
public class JobConfigurationRestController extends AbstractRestController implements InitializingBean {

    private final ControllerServiceEntryBean controllerServiceEntryBean;
    private final JobRegistry jobRegistry;

    @Autowired
    public JobConfigurationRestController(final ControllerServiceEntryBean controllerServiceEntryBean, final JobRegistry jobRegistry) {
        this.controllerServiceEntryBean = controllerServiceEntryBean;
        this.jobRegistry = jobRegistry;
    }


    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS, produces = PRODUCES, method = RequestMethod.GET)
    public ResponseEntity<JobConfigurations> getJobConfigurations() {
        final JobConfigurations jobConfigurations = controllerServiceEntryBean.getJobConfigurations(jobRegistry.getJobNames());
        jobConfigurations.setJobName(ALL_JOBS);
        return ResponseEntity.ok(jobConfigurations);
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS_JOB_NAME, produces = PRODUCES, method = RequestMethod.GET)
    public ResponseEntity<JobConfigurations> getJobConfigurationsByJobName(@PathVariable("jobname") final String jobName) {
        final JobConfigurations jobConfigurations = controllerServiceEntryBean.getJobConfigurationsByJobName(jobName);
        jobConfigurations.setJobName(jobName);
        return ResponseEntity.ok(jobConfigurations);
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID, produces = PRODUCES, method = RequestMethod.GET)
    public ResponseEntity<JobConfiguration> getJobConfigurationById(@PathVariable("jobconfigurationid") final Long jobConfigurationId) {
        final JobConfiguration jobConfiguration = controllerServiceEntryBean.getJobConfigurationById(jobConfigurationId);
        return ResponseEntity.ok(jobConfiguration);
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS, consumes = CONSUMES, method = RequestMethod.POST)
    public ResponseEntity<Void> addJobConfiguration(@RequestBody final JobConfiguration jobConfiguration) {
        controllerServiceEntryBean.saveJobConfiguration(jobConfiguration);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS, consumes = CONSUMES, method = RequestMethod.PUT)
    public ResponseEntity<Void> updateJobConfiguration(@RequestBody final JobConfiguration jobConfiguration) {
        controllerServiceEntryBean.updateJobConfiguration(jobConfiguration);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteJobConfigurationById(@PathVariable("jobconfigurationid") final Long jobConfigurationId) {
        controllerServiceEntryBean.deleteJobConfiguration(jobConfigurationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        assert controllerServiceEntryBean != null;
        assert jobRegistry != null;
    }
}
