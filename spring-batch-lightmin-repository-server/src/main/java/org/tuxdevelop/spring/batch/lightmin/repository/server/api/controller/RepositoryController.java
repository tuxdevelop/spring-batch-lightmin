package org.tuxdevelop.spring.batch.lightmin.repository.server.api.controller;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.exception.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.Collection;

/**
 * @author Marcel Becker
 * @since 0.4
 */
@RestController
@RequestMapping("api/repository/jobconfigurations")
public class RepositoryController implements JobConfigurationRepository, InitializingBean {


    private final JobConfigurationRepository localJobConfigurationRepository;

    public RepositoryController(final JobConfigurationRepository localJobConfigurationRepository) {
        this.localJobConfigurationRepository = localJobConfigurationRepository;
    }

    @Override
    @GetMapping(value = "/{jobconfigurationid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public JobConfiguration getJobConfiguration(@PathVariable(name = "jobconfigurationid") final Long jobConfigurationId, @RequestParam(name = "applicationname") final String applicationName) throws NoSuchJobConfigurationException {
        return this.localJobConfigurationRepository.getJobConfiguration(jobConfigurationId, applicationName);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<JobConfiguration> getJobConfigurations(@RequestParam(name = "jobname") final String jobName, @RequestParam(name = "applicationname") final String applicationName) throws NoSuchJobException, NoSuchJobConfigurationException {
        return this.localJobConfigurationRepository.getJobConfigurations(jobName, applicationName);
    }

    @Override
    @PostMapping(value = "/{applicationname}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JobConfiguration add(@RequestBody final JobConfiguration jobConfiguration, @PathVariable(name = "applicationname") final String applicationName) {
        validateJobConfigurationBody(jobConfiguration);
        jobConfiguration.validateForSave();
        return this.localJobConfigurationRepository.add(jobConfiguration, applicationName);
    }

    @Override
    @PutMapping(value = "/{applicationname}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JobConfiguration update(@RequestBody final JobConfiguration jobConfiguration, @PathVariable(name = "applicationname") final String applicationName) throws NoSuchJobConfigurationException {
        validateJobConfigurationBody(jobConfiguration);
        jobConfiguration.validateForUpdate();
        return this.localJobConfigurationRepository.update(jobConfiguration, applicationName);
    }

    @Override
    @PostMapping(value = "/delete/{applicationname}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@RequestBody final JobConfiguration jobConfiguration, @PathVariable(name = "applicationname") final String applicationName) throws NoSuchJobConfigurationException {
        validateJobConfigurationBody(jobConfiguration);
        this.localJobConfigurationRepository.delete(jobConfiguration, applicationName);
    }

    @Override
    @GetMapping(value = "/all/{applicationname}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<JobConfiguration> getAllJobConfigurations(@PathVariable(name = "applicationname") final String applicationName) {
        return this.localJobConfigurationRepository.getAllJobConfigurations(applicationName);
    }

    @Override
    @GetMapping(value = "/all/{applicationname}/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<JobConfiguration> getAllJobConfigurationsByJobNames(@RequestParam(name = "jobnames") final Collection<String> jobNames, @PathVariable(name = "applicationname") final String applicationName) {
        return this.localJobConfigurationRepository.getAllJobConfigurationsByJobNames(jobNames, applicationName);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        assert this.localJobConfigurationRepository != null : "localJobConfigurationRepository must not be null org empty!";
    }

    private void validateJobConfigurationBody(final JobConfiguration jobConfiguration) {
        if (jobConfiguration == null) {
            throw new SpringBatchLightminApplicationException("jobConfiguration must not be null");
        }
    }
}
