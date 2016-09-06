package org.tuxdevelop.spring.batch.lightmin.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientInformation;
import org.tuxdevelop.spring.batch.lightmin.model.JobConfigurationAddModel;
import org.tuxdevelop.spring.batch.lightmin.model.JobConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.model.JobListenerTypeModel;
import org.tuxdevelop.spring.batch.lightmin.model.JobSchedulerTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.admin.AdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.util.ParameterParser;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "/jobSchedulerConfigurations", method = RequestMethod.GET)
    public void getJobSchedulerConfigurations(@RequestParam("applicationid") final String applicationId,
                                              final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final Collection<JobConfigurationModel> jobConfigurationModels = getJobConfigurationSchedulerModels(lightminClientApplication);
        model.addAttribute("jobConfigurationModels", jobConfigurationModels);
        model.addAttribute("clientApplication", lightminClientApplication);
    }

    @RequestMapping(value = "/jobListenerConfigurations", method = RequestMethod.GET)
    public void getJobListenerConfigurations(@RequestParam("applicationid") final String applicationId,
                                             final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final Collection<JobConfigurationModel> jobConfigurationModels = getJobConfigurationListenerModels(lightminClientApplication);
        model.addAttribute("jobConfigurationModels", jobConfigurationModels);
        model.addAttribute("clientApplication", lightminClientApplication);
    }

    @RequestMapping(value = "/jobSchedulerConfigurationAdd", method = RequestMethod.GET)
    public void initAddJobSchedulerConfigurationType(@RequestParam("applicationid") final String applicationId,
                                                     final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final List<JobSchedulerType> jobSchedulerTypes = lightminClientApplication.getLightminClientInformation().getSupportedSchedulerTypes();
        model.addAttribute("jobSchedulerTypes", jobSchedulerTypes);
        model.addAttribute("clientApplication", lightminClientApplication);
        model.addAttribute("jobSchedulerType", new JobSchedulerTypeModel());
    }

    @RequestMapping(value = "/jobListenerConfigurationAdd", method = RequestMethod.GET)
    public void initAddJobListenerConfigurationType(@RequestParam("applicationid") final String applicationId,
                                                    final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final List<JobListenerType> jobListenerTypes = lightminClientApplication.getLightminClientInformation().getSupportedJobListenerTypes();
        model.addAttribute("jobListenerTypes", jobListenerTypes);
        model.addAttribute("clientApplication", lightminClientApplication);
        model.addAttribute("jobListenerType", new JobListenerTypeModel());
    }

    @RequestMapping(value = "/jobSchedulerAdd", method = RequestMethod.GET)
    public void initAddJobSchedulerConfiguration(@RequestParam("id") final String applicationId,
                                                 @RequestParam("jobSchedulerType") final JobSchedulerType jobSchedulerType,
                                                 final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final JobConfigurationAddModel jobConfigurationAddModel = new JobConfigurationAddModel();
        jobConfigurationAddModel.setApplicationId(lightminClientApplication.getId());
        jobConfigurationAddModel.setJobSchedulerType(jobSchedulerType);
        initJobConfigurationAddModel(model,
                lightminClientApplication,
                jobConfigurationAddModel,
                null,
                jobSchedulerType);
    }

    @RequestMapping(value = "/jobListenerAdd", method = RequestMethod.GET)
    public void initAddJobListenerConfiguration(@RequestParam("id") final String applicationId,
                                                @RequestParam("jobListenerType") final JobListenerType jobListenerType,
                                                final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final JobConfigurationAddModel jobConfigurationAddModel = new JobConfigurationAddModel();
        jobConfigurationAddModel.setApplicationId(lightminClientApplication.getId());
        jobConfigurationAddModel.setJobListenerType(jobListenerType);
        initJobConfigurationAddModel(model,
                lightminClientApplication,
                jobConfigurationAddModel,
                jobListenerType,
                null
        );
    }


    @RequestMapping(value = "/jobSchedulerEdit", method = RequestMethod.GET)
    public String initEditJobSchedulerConfiguration(@RequestParam("jobConfigurationId") final Long jobConfigurationId,
                                                    @RequestParam("applicationid") final String applicationId,
                                                    final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final LightminClientInformation lightminClientInformation = lightminClientApplication
                .getLightminClientInformation();
        final JobConfiguration jobConfiguration = adminServerService.getJobConfiguration(jobConfigurationId,
                lightminClientApplication);
        final JobConfigurationAddModel jobConfigurationAddModel = new JobConfigurationAddModel();
        jobConfigurationAddModel.setJobName(jobConfiguration.getJobName());
        jobConfigurationAddModel.setJobParameters(ParameterParser.parseParametersToString(jobConfiguration.getJobParameters()));
        jobConfigurationAddModel.setJobSchedulerType(jobConfiguration.getJobSchedulerConfiguration().getJobSchedulerType());
        jobConfigurationAddModel.setTaskExecutorType(jobConfiguration.getJobSchedulerConfiguration().getTaskExecutorType());
        jobConfigurationAddModel.setCronExpression(jobConfiguration.getJobSchedulerConfiguration().getCronExpression());
        jobConfigurationAddModel.setFixedDelay(jobConfiguration.getJobSchedulerConfiguration().getFixedDelay());
        jobConfigurationAddModel.setInitialDelay(jobConfiguration.getJobSchedulerConfiguration().getInitialDelay());
        jobConfigurationAddModel.setJobConfigurationId(jobConfigurationId);
        jobConfigurationAddModel.setSchedulerStatus(jobConfiguration.getJobSchedulerConfiguration().getSchedulerStatus());
        jobConfigurationAddModel.setJobIncrementer(jobConfiguration.getJobIncrementer());
        jobConfigurationAddModel.setApplicationId(lightminClientApplication.getId());
        model.addAttribute("jobConfigurationAddModel", jobConfigurationAddModel);
        model.addAttribute("jobSchedulerTypes", lightminClientInformation.getSupportedSchedulerTypes());
        model.addAttribute("taskExecutorTypes", lightminClientInformation.getSupportedTaskExecutorTypes());
        model.addAttribute("jobIncrementerTypes", lightminClientInformation.getSupportedJobIncrementers());
        model.addAttribute("clientApplication", lightminClientApplication);
        return "jobSchedulerEdit";
    }

    @RequestMapping(value = "/jobListenerEdit", method = RequestMethod.GET)
    public String initEditJobListenerConfiguration(@RequestParam("jobConfigurationId") final Long jobConfigurationId,
                                                   @RequestParam("applicationid") final String applicationId,
                                                   final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final LightminClientInformation lightminClientInformation = lightminClientApplication.getLightminClientInformation();
        final JobConfiguration jobConfiguration = adminServerService.getJobConfiguration(jobConfigurationId, lightminClientApplication);
        final JobConfigurationAddModel jobConfigurationAddModel = new JobConfigurationAddModel();
        jobConfigurationAddModel.setJobName(jobConfiguration.getJobName());
        jobConfigurationAddModel.setJobParameters(ParameterParser.parseParametersToString(jobConfiguration.getJobParameters()));
        jobConfigurationAddModel.setJobListenerType(jobConfiguration.getJobListenerConfiguration().getJobListenerType());
        jobConfigurationAddModel.setTaskExecutorType(jobConfiguration.getJobListenerConfiguration().getTaskExecutorType());
        jobConfigurationAddModel.setSourceFolder(jobConfiguration.getJobListenerConfiguration().getSourceFolder());
        jobConfigurationAddModel.setFilePattern(jobConfiguration.getJobListenerConfiguration().getFilePattern());
        jobConfigurationAddModel.setPollerPeriod(jobConfiguration.getJobListenerConfiguration().getPollerPeriod());
        jobConfigurationAddModel.setJobConfigurationId(jobConfigurationId);
        jobConfigurationAddModel.setListenerStatus(jobConfiguration.getJobListenerConfiguration().getListenerStatus());
        jobConfigurationAddModel.setJobIncrementer(jobConfiguration.getJobIncrementer());
        jobConfigurationAddModel.setApplicationId(lightminClientApplication.getId());
        model.addAttribute("jobConfigurationAddModel", jobConfigurationAddModel);
        model.addAttribute("jobListenerTypes", lightminClientInformation.getSupportedJobListenerTypes());
        model.addAttribute("taskExecutorTypes", lightminClientInformation.getSupportedTaskExecutorTypes());
        model.addAttribute("jobIncrementerTypes", lightminClientInformation.getSupportedJobIncrementers());
        model.addAttribute("clientApplication", lightminClientApplication);
        return "jobListenerEdit";
    }

    @RequestMapping(value = "/jobConfigurationAdd", method = RequestMethod.POST)
    public String addJobConfiguration(
            @ModelAttribute("jobConfigurationAddModel") final JobConfigurationAddModel jobConfigurationAddModel) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(jobConfigurationAddModel.getApplicationId());
        final JobConfiguration jobConfiguration = mapModelToJobConfiguration(jobConfigurationAddModel);
        final String redirect = determineAddModelRedirect(jobConfigurationAddModel);
        adminServerService.saveJobConfiguration(jobConfiguration, lightminClientApplication);
        return "redirect:" + redirect + "?applicationid=" + jobConfigurationAddModel.getApplicationId();
    }

    @RequestMapping(value = "/jobConfigurationEdit", method = RequestMethod.POST)
    public String updateJobConfiguration(
            @ModelAttribute("jobConfigurationAddModel") final JobConfigurationAddModel jobConfigurationAddModel) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(jobConfigurationAddModel.getApplicationId());
        final JobConfiguration jobConfiguration = mapModelToJobConfiguration(jobConfigurationAddModel);
        final String redirect = determineAddModelRedirect(jobConfigurationAddModel);
        adminServerService.updateJobConfiguration(jobConfiguration, lightminClientApplication);
        return "redirect:" + redirect + "?applicationid=" + jobConfigurationAddModel.getApplicationId();
    }

    @RequestMapping(value = "/jobConfigurations", method = RequestMethod.POST)
    public String deleteJobConfiguration(@RequestParam("jobConfigurationId") final long jobConfigurationId,
                                         @RequestParam("applicationid") final String applicationId,
                                         final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        adminServerService.deleteJobConfiguration(jobConfigurationId, lightminClientApplication);
        final Collection<JobConfigurationModel> jobConfigurationModels = getJobConfigurationSchedulerModels(
                lightminClientApplication);
        model.addAttribute("jobConfigurationModels", jobConfigurationModels);
        model.addAttribute("clientApplication", lightminClientApplication);
        return "redirect:jobSchedulerConfigurations?applicationid=" + applicationId;
    }

    @RequestMapping(value = "/jobConfigurationStart", method = RequestMethod.POST)
    public String startJobConfigurationScheduler(@RequestParam("jobConfigurationId") final long jobConfigurationId,
                                                 @RequestParam("applicationid") final String applicationId,
                                                 @RequestParam("redirect") final String redirect,
                                                 final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        adminServerService.startJobConfigurationScheduler(jobConfigurationId, lightminClientApplication);
        final Collection<JobConfigurationModel> jobConfigurationModels = getJobConfigurationSchedulerModels(
                lightminClientApplication);
        model.addAttribute("jobConfigurationModels", jobConfigurationModels);
        model.addAttribute("clientApplication", lightminClientApplication);
        return "redirect:" + redirect + "?applicationid=" + applicationId;
    }

    @RequestMapping(value = "/jobConfigurationStop", method = RequestMethod.POST)
    public String stopJobConfigurationScheduler(@RequestParam("jobConfigurationId") final long jobConfigurationId,
                                                @RequestParam("applicationid") final String applicationId,
                                                @RequestParam("redirect") final String redirect,
                                                final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        adminServerService.stopJobConfigurationScheduler(jobConfigurationId, lightminClientApplication);
        final Collection<JobConfigurationModel> jobConfigurationModels = getJobConfigurationSchedulerModels(
                lightminClientApplication);
        model.addAttribute("jobConfigurationModels", jobConfigurationModels);
        model.addAttribute("clientApplication", lightminClientApplication);
        return "redirect:" + redirect + "?applicationid=" + applicationId;
    }

    private Collection<JobConfigurationModel> getJobConfigurationSchedulerModels(
            final LightminClientApplication lightminClientApplication) {
        final Map<String, JobConfigurations> jobConfigurationMap = adminServerService.getJobConfigurationsMap(lightminClientApplication);
        final Collection<JobConfigurationModel> jobConfigurationModels = new LinkedList<>();
        for (final Map.Entry<String, JobConfigurations> entry : jobConfigurationMap.entrySet()) {
            final JobConfigurations tempJobConfigurations = entry.getValue();
            final Collection<JobConfiguration> tempCollection = new LinkedList<>();
            for (final JobConfiguration tempJobConfiguration : tempJobConfigurations.getJobConfigurations()) {
                if (tempJobConfiguration.getJobSchedulerConfiguration() != null) {
                    tempCollection.add(tempJobConfiguration);
                }
            }
            if (!tempCollection.isEmpty()) {
                final JobConfigurationModel jobConfigurationModel = new JobConfigurationModel();
                jobConfigurationModel.setJobName(entry.getKey());
                jobConfigurationModel.setJobConfigurations(tempCollection);
                jobConfigurationModels.add(jobConfigurationModel);
            }
        }
        return jobConfigurationModels;
    }

    private Collection<JobConfigurationModel> getJobConfigurationListenerModels(
            final LightminClientApplication lightminClientApplication) {
        final Map<String, JobConfigurations> jobConfigurationMap = adminServerService
                .getJobConfigurationsMap(lightminClientApplication);
        final Collection<JobConfigurationModel> jobConfigurationModels = new LinkedList<>();
        for (final Map.Entry<String, JobConfigurations> entry : jobConfigurationMap.entrySet()) {
            final JobConfigurations tempJobConfigurations = entry.getValue();
            final Collection<JobConfiguration> tempCollection = new LinkedList<>();
            for (final JobConfiguration tempJobConfiguration : tempJobConfigurations.getJobConfigurations()) {
                if (tempJobConfiguration.getJobListenerConfiguration() != null) {
                    tempCollection.add(tempJobConfiguration);
                }
            }
            if (!tempCollection.isEmpty()) {
                final JobConfigurationModel jobConfigurationModel = new JobConfigurationModel();
                jobConfigurationModel.setJobName(entry.getKey());
                jobConfigurationModel.setJobConfigurations(tempCollection);
                jobConfigurationModels.add(jobConfigurationModel);
            }
        }
        return jobConfigurationModels;
    }

    private JobConfiguration mapModelToJobConfiguration(final JobConfigurationAddModel jobConfigurationAddModel) {
        //Scheduler
        final JobSchedulerConfiguration jobSchedulerConfiguration;
        if (jobConfigurationAddModel.getJobSchedulerType() != null) {
            jobSchedulerConfiguration = new JobSchedulerConfiguration();
            jobSchedulerConfiguration.setJobSchedulerType(jobConfigurationAddModel.getJobSchedulerType());
            if (JobSchedulerType.CRON.equals(jobConfigurationAddModel.getJobSchedulerType())) {
                jobSchedulerConfiguration.setCronExpression(jobConfigurationAddModel.getCronExpression());
            } else {
                jobSchedulerConfiguration.setFixedDelay(jobConfigurationAddModel.getFixedDelay());
                jobSchedulerConfiguration.setInitialDelay(jobConfigurationAddModel.getInitialDelay());
            }
            jobSchedulerConfiguration.setTaskExecutorType(jobConfigurationAddModel.getTaskExecutorType());
            jobSchedulerConfiguration.setSchedulerStatus(jobConfigurationAddModel.getSchedulerStatus());
        } else {
            jobSchedulerConfiguration = null;
        }
        //Listener
        final JobListenerConfiguration jobListenerConfiguration;
        if (jobConfigurationAddModel.getJobListenerType() != null) {
            jobListenerConfiguration = new JobListenerConfiguration();
            jobListenerConfiguration.setJobListenerType(jobConfigurationAddModel.getJobListenerType());
            jobListenerConfiguration.setListenerStatus(jobConfigurationAddModel.getListenerStatus());
            jobListenerConfiguration.setPollerPeriod(jobConfigurationAddModel.getPollerPeriod());
            jobListenerConfiguration.setSourceFolder(jobConfigurationAddModel.getSourceFolder());
            jobListenerConfiguration.setFilePattern(jobConfigurationAddModel.getFilePattern());
            jobListenerConfiguration.setTaskExecutorType(jobConfigurationAddModel.getTaskExecutorType());
        } else {
            jobListenerConfiguration = null;
        }
        //JobConfiguration
        final JobConfiguration jobConfiguration = new JobConfiguration();
        final JobParameters jobParameters = ParameterParser
                .parseParametersStringToJobParameters(jobConfigurationAddModel.getJobParameters());
        jobConfiguration.setJobName(jobConfigurationAddModel.getJobName());
        jobConfiguration.setJobSchedulerConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobListenerConfiguration(jobListenerConfiguration);
        jobConfiguration.setJobParameters(jobParameters);
        jobConfiguration.setJobIncrementer(jobConfigurationAddModel.getJobIncrementer());
        jobConfiguration.setJobConfigurationId(jobConfigurationAddModel.getJobConfigurationId());
        return jobConfiguration;
    }

    private void initJobConfigurationAddModel(final Model model,
                                              final LightminClientApplication lightminClientApplication,
                                              final JobConfigurationAddModel jobConfigurationAddModel,
                                              final JobListenerType jobListenerType,
                                              final JobSchedulerType jobSchedulerType) {
        model.addAttribute("jobConfigurationAddModel", jobConfigurationAddModel);
        model.addAttribute("jobNames", lightminClientApplication.getLightminClientInformation().getRegisteredJobs());
        model.addAttribute("jobSchedulerTypes", lightminClientApplication.getLightminClientInformation().getSupportedSchedulerTypes());
        if (jobSchedulerType != null) {
            model.addAttribute("jobSchedulerType", jobSchedulerType.name());
        }
        model.addAttribute("taskExecutorTypes", lightminClientApplication.getLightminClientInformation().getSupportedTaskExecutorTypes());
        model.addAttribute("schedulerStatusValues", lightminClientApplication.getLightminClientInformation().getSupportedSchedulerStatuses());
        model.addAttribute("jobIncrementerTypes", lightminClientApplication.getLightminClientInformation().getSupportedJobIncrementers());
        if (jobListenerType != null) {
            model.addAttribute("jobListenerType", jobListenerType.name());
        }
        model.addAttribute("jobListenerTypes", lightminClientApplication.getLightminClientInformation().getSupportedJobListenerTypes());
        model.addAttribute("listenerStatusValues", lightminClientApplication.getLightminClientInformation().getSupportedListenerStatuses());
        model.addAttribute("clientApplication", lightminClientApplication);
    }

    private String determineAddModelRedirect(final JobConfigurationAddModel jobConfigurationAddModel) {
        final String redirect;
        //Dirty Hack Start
        if (jobConfigurationAddModel.getJobSchedulerType() != null) {
            redirect = "jobSchedulerConfigurations";
        } else if (jobConfigurationAddModel.getJobListenerType() != null) {
            redirect = "jobListenerConfigurations";
        } else {
            redirect = "";
        }
        //Dirty Hack End
        return redirect;
    }
}
