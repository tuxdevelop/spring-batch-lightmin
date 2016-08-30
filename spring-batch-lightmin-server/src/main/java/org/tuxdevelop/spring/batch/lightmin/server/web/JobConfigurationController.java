package org.tuxdevelop.spring.batch.lightmin.server.web;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientInformation;
import org.tuxdevelop.spring.batch.lightmin.model.JobConfigurationAddModel;
import org.tuxdevelop.spring.batch.lightmin.model.JobConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.admin.AdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.util.ParameterParser;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Controller
public class JobConfigurationController extends CommonController {

    private final AdminServerService adminServerService;
    private final RegistrationBean registrationBean;

    @Autowired
    public JobConfigurationController(final AdminServerService adminServerService,
            final RegistrationBean registrationBean) {
        this.adminServerService = adminServerService;
        this.registrationBean = registrationBean;
    }

    @RequestMapping(value = "/jobConfigurations", method = RequestMethod.GET)
    public void getJobConfigurations(@RequestParam("applicationid") final String applicationId,
            final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final Collection<JobConfigurationModel> jobConfigurationModels = getJobConfigurationModels(
                lightminClientApplication);
        model.addAttribute("jobConfigurationModels", jobConfigurationModels);
        model.addAttribute("clientApplication", lightminClientApplication);
    }

    @RequestMapping(value = "/jobConfigurationAdd", method = RequestMethod.GET)
    public void initAddJobConfiguration(@RequestParam("applicationid") final String applicationId,
            final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final LightminClientInformation lightminClientInformation = lightminClientApplication
                .getLightminClientInformation();
        final JobConfigurationAddModel jobConfigurationAddModel = new JobConfigurationAddModel();
        model.addAttribute("jobConfigurationAddModel", jobConfigurationAddModel);
        model.addAttribute("jobNames", lightminClientInformation.getRegisteredJobs());
        model.addAttribute("jobSchedulerTypes", lightminClientInformation.getSupportedSchedulerTypes());
        model.addAttribute("taskExecutorTypes", lightminClientInformation.getSupportedTaskExecutorTypes());
        model.addAttribute("schedulerStatusValues", lightminClientInformation.getSupportedSchedulerStatuses());
        model.addAttribute("jobIncrementerTypes", lightminClientInformation.getSupportedJobIncrementers());
        model.addAttribute("clientApplication", lightminClientApplication);
    }

    @RequestMapping(value = "/jobConfigurationEdit", method = RequestMethod.GET)
    public String initEditJobConfiguration(@RequestParam("jobConfigurationId") final Long jobConfigurationId,
            @RequestParam("applicationid") final String applicationId,
            final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final LightminClientInformation lightminClientInformation = lightminClientApplication
                .getLightminClientInformation();
        final JobConfiguration jobConfiguration = adminServerService.getJobConfiguration(jobConfigurationId,
                lightminClientApplication);
        final JobConfigurationAddModel jobConfigurationAddModel = new JobConfigurationAddModel();
        jobConfigurationAddModel.setJobName(jobConfiguration.getJobName());
        jobConfigurationAddModel
                .setJobParameters(ParameterParser.parseParametersToString(jobConfiguration.getJobParameters()));
        jobConfigurationAddModel.setJobSchedulerType(jobConfiguration.getJobSchedulerConfiguration()
                .getJobSchedulerType());
        jobConfigurationAddModel.setTaskExecutorType(jobConfiguration.getJobSchedulerConfiguration()
                .getTaskExecutorType());
        jobConfigurationAddModel.setCronExpression(jobConfiguration.getJobSchedulerConfiguration().getCronExpression());
        jobConfigurationAddModel.setFixedDelay(jobConfiguration.getJobSchedulerConfiguration().getFixedDelay());
        jobConfigurationAddModel.setInitialDelay(jobConfiguration.getJobSchedulerConfiguration().getInitialDelay());
        jobConfigurationAddModel.setJobConfigurationId(jobConfigurationId);
        jobConfigurationAddModel
                .setSchedulerStatus(jobConfiguration.getJobSchedulerConfiguration().getSchedulerStatus());
        jobConfigurationAddModel.setJobIncrementer(jobConfiguration.getJobIncrementer());
        model.addAttribute("jobConfigurationAddModel", jobConfigurationAddModel);
        model.addAttribute("jobSchedulerTypes", lightminClientInformation.getSupportedSchedulerTypes());
        model.addAttribute("taskExecutorTypes", lightminClientInformation.getSupportedTaskExecutorTypes());
        model.addAttribute("jobIncrementerTypes", lightminClientInformation.getSupportedJobIncrementers());
        model.addAttribute("clientApplication", lightminClientApplication);
        return "jobConfigurationEdit";
    }

    @RequestMapping(value = "/jobConfigurationAdd", method = RequestMethod.POST)
    public String addJobConfiguration(
            @ModelAttribute("jobConfigurationAddModel") final JobConfigurationAddModel jobConfigurationAddModel) {
        final LightminClientApplication lightminClientApplication = registrationBean
                .get(jobConfigurationAddModel.getApplicationId());
        final JobConfiguration jobConfiguration = mapModelToJobConfiguration(jobConfigurationAddModel);
        adminServerService.saveJobConfiguration(jobConfiguration, lightminClientApplication);
        return "redirect:jobConfigurations";
    }

    @RequestMapping(value = "/jobConfigurationEdit", method = RequestMethod.POST)
    public String updateJobConfiguration(
            @ModelAttribute("jobConfigurationAddModel") final JobConfigurationAddModel jobConfigurationAddModel) {
        final LightminClientApplication lightminClientApplication = registrationBean
                .get(jobConfigurationAddModel.getApplicationId());
        final JobConfiguration jobConfiguration = mapModelToJobConfiguration(jobConfigurationAddModel);
        adminServerService.updateJobConfiguration(jobConfiguration, lightminClientApplication);
        return "redirect:jobConfigurations";
    }

    @RequestMapping(value = "/jobConfigurations", method = RequestMethod.POST)
    public String deleteJobConfiguration(@RequestParam("jobConfigurationId") final long jobConfigurationId,
            @RequestParam("applicationid") final String applicationId,
            final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        adminServerService.deleteJobConfiguration(jobConfigurationId, lightminClientApplication);
        final Collection<JobConfigurationModel> jobConfigurationModels = getJobConfigurationModels(
                lightminClientApplication);
        model.addAttribute("jobConfigurationModels", jobConfigurationModels);
        model.addAttribute("clientApplication", lightminClientApplication);
        return "redirect:jobConfigurations";
    }

    @RequestMapping(value = "/jobConfigurationSchedulerStart", method = RequestMethod.POST)
    public String startJobConfigurationScheduler(@RequestParam("jobConfigurationId") final long jobConfigurationId,
            @RequestParam("applicationid") final String applicationId,
            final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        adminServerService.startJobConfigurationScheduler(jobConfigurationId, lightminClientApplication);
        final Collection<JobConfigurationModel> jobConfigurationModels = getJobConfigurationModels(
                lightminClientApplication);
        model.addAttribute("jobConfigurationModels", jobConfigurationModels);
        model.addAttribute("clientApplication", lightminClientApplication);
        return "redirect:jobConfigurations";
    }

    @RequestMapping(value = "/jobConfigurationSchedulerStop", method = RequestMethod.POST)
    public String stopJobConfigurationScheduler(@RequestParam("jobConfigurationId") final long jobConfigurationId,
            @RequestParam("applicationid") final String applicationId,
            final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        adminServerService.stopJobConfigurationScheduler(jobConfigurationId, lightminClientApplication);
        final Collection<JobConfigurationModel> jobConfigurationModels = getJobConfigurationModels(
                lightminClientApplication);
        model.addAttribute("jobConfigurationModels", jobConfigurationModels);
        model.addAttribute("clientApplication", lightminClientApplication);
        return "redirect:jobConfigurations";
    }

    private Collection<JobConfigurationModel> getJobConfigurationModels(
            final LightminClientApplication lightminClientApplication) {
        final Map<String, JobConfigurations> jobConfigurationMap = adminServerService
                .getJobConfigurationsMap(lightminClientApplication);
        final Collection<JobConfigurationModel> jobConfigurationModels = new LinkedList<>();
        for (final Map.Entry<String, JobConfigurations> entry : jobConfigurationMap.entrySet()) {
            final JobConfigurationModel jobConfigurationModel = new JobConfigurationModel();
            jobConfigurationModel.setJobName(entry.getKey());
            jobConfigurationModel.setJobConfigurations(entry.getValue().getJobConfigurations());
            jobConfigurationModels.add(jobConfigurationModel);
        }
        return jobConfigurationModels;
    }

    private JobConfiguration mapModelToJobConfiguration(final JobConfigurationAddModel jobConfigurationAddModel) {
        final JobSchedulerConfiguration jobSchedulerConfiguration = new JobSchedulerConfiguration();
        jobSchedulerConfiguration.setJobSchedulerType(jobConfigurationAddModel.getJobSchedulerType());
        if (JobSchedulerType.CRON.equals(jobConfigurationAddModel.getJobSchedulerType())) {
            jobSchedulerConfiguration.setCronExpression(jobConfigurationAddModel.getCronExpression());
        } else {
            jobSchedulerConfiguration.setFixedDelay(jobConfigurationAddModel.getFixedDelay());
            jobSchedulerConfiguration.setInitialDelay(jobConfigurationAddModel.getInitialDelay());
        }
        jobSchedulerConfiguration.setTaskExecutorType(jobConfigurationAddModel.getTaskExecutorType());
        jobSchedulerConfiguration.setSchedulerStatus(jobConfigurationAddModel.getSchedulerStatus());
        final JobParameters jobParameters = ParameterParser
                .parseParametersStringToJobParameters(jobConfigurationAddModel.getJobParameters());
        final JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setJobName(jobConfigurationAddModel.getJobName());
        jobConfiguration.setJobSchedulerConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobParameters(jobParameters);
        jobConfiguration.setJobIncrementer(jobConfigurationAddModel.getJobIncrementer());
        jobConfiguration.setJobConfigurationId(jobConfigurationAddModel.getJobConfigurationId());
        return jobConfiguration;
    }
}
