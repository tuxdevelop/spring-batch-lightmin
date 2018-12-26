package org.tuxdevelop.spring.batch.lightmin.client.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobLaunch;
import org.tuxdevelop.spring.batch.lightmin.service.ServiceEntry;

/**
 * @author Marcel Becker
 * @version 0.3
 */
@ResponseBody
@RequestMapping("/")
public class JobLauncherRestController extends AbstractRestController {

    private final ServiceEntry serviceEntry;

    public JobLauncherRestController(final ServiceEntry serviceEntry) {
        this.serviceEntry = serviceEntry;
    }

    /**
     * Lauches a {@link org.springframework.batch.core.Job} with the given values of the {@link JobLaunch} parameter
     *
     * @param jobLaunch the launch information for the Job
     * @return HTTP Status Code 201
     */
    @RequestMapping(value = JobLauncherRestControllerAPI.JOB_LAUNCH, consumes = CONSUMES, method = RequestMethod.POST)
    public ResponseEntity<Void> launchJob(@RequestBody final JobLaunch jobLaunch) {
        this.serviceEntry.launchJob(jobLaunch);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
