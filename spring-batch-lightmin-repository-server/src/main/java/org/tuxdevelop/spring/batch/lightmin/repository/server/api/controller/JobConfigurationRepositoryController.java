package org.tuxdevelop.spring.batch.lightmin.repository.server.api.controller;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;

import java.util.Collection;

/**
 * @author Marcel Becker
 * @since 0.4
 */
@RestController
@RequestMapping("api/repository/jobconfigurations")
public class JobConfigurationRepositoryController implements JobConfigurationRepository, InitializingBean {


    private final JobConfigurationRepository jobConfigurationRepository;

    public JobConfigurationRepositoryController(final JobConfigurationRepository jobConfigurationRepository) {
        this.jobConfigurationRepository = jobConfigurationRepository;
    }

    @Override
    @GetMapping(value = "/{jobconfigurationid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public JobConfiguration getJobConfiguration(@PathVariable(name = "jobconfigurationid") final Long jobConfigurationId, @RequestParam(name = "applicationname") final String applicationName) throws NoSuchJobConfigurationException {
        return this.jobConfigurationRepository.getJobConfiguration(jobConfigurationId, applicationName);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<JobConfiguration> getJobConfigurations(@RequestParam(name = "jobname") final String jobName, @RequestParam(name = "applicationname") final String applicationName) throws NoSuchJobException, NoSuchJobConfigurationException {
        return this.jobConfigurationRepository.getJobConfigurations(jobName, applicationName);
    }

    @Override
    @PostMapping(value = "/{applicationname}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JobConfiguration add(@RequestBody final JobConfiguration jobConfiguration, @PathVariable(name = "applicationname") final String applicationName) {
        this.validateJobConfigurationBody(jobConfiguration);
        jobConfiguration.validateForSave();
        return this.jobConfigurationRepository.add(jobConfiguration, applicationName);
    }

    @Override
    @PutMapping(value = "/{applicationname}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JobConfiguration update(@RequestBody final JobConfiguration jobConfiguration, @PathVariable(name = "applicationname") final String applicationName) throws NoSuchJobConfigurationException {
        this.validateJobConfigurationBody(jobConfiguration);
        jobConfiguration.validateForUpdate();
        return this.jobConfigurationRepository.update(jobConfiguration, applicationName);
    }

    @Override
    @PostMapping(value = "/delete/{applicationname}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@RequestBody final JobConfiguration jobConfiguration, @PathVariable(name = "applicationname") final String applicationName) throws NoSuchJobConfigurationException {
        this.validateJobConfigurationBody(jobConfiguration);
        this.jobConfigurationRepository.delete(jobConfiguration, applicationName);
    }

    @Override
    @GetMapping(value = "/all/{applicationname}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<JobConfiguration> getAllJobConfigurations(@PathVariable(name = "applicationname") final String applicationName) {
        return this.jobConfigurationRepository.getAllJobConfigurations(applicationName);
    }

    @Override
    @GetMapping(value = "/all/{applicationname}/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<JobConfiguration> getAllJobConfigurationsByJobNames(@RequestParam(name = "jobnames") final Collection<String> jobNames, @PathVariable(name = "applicationname") final String applicationName) {
        return this.jobConfigurationRepository.getAllJobConfigurationsByJobNames(jobNames, applicationName);
    }

    @Override
    public void afterPropertiesSet() {
        assert this.jobConfigurationRepository != null : "jobConfigurationRepository must not be null org empty!";
    }

    private void validateJobConfigurationBody(final JobConfiguration jobConfiguration) {
        if (jobConfiguration == null) {
            throw new SpringBatchLightminApplicationException("jobConfiguration must not be null");
        }
    }
}
