package org.tuxdevelop.spring.batch.lightmin.controller;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.TaskExecutorType;
import org.tuxdevelop.spring.batch.lightmin.model.JobConfigurationAddModel;
import org.tuxdevelop.spring.batch.lightmin.model.JobConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.service.AdminService;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;
import org.tuxdevelop.spring.batch.lightmin.util.ParameterParser;

@Controller
public class JobConfigurationController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JobService jobService;

    @RequestMapping(value = "/jobConfigurations", method = RequestMethod.GET)
    public void getJobConfigurations(final Model model) {
        final Map<String, Collection<JobConfiguration>> jobConfigurationMap = adminService.getJobConfigurationMap();
        final Collection<JobConfigurationModel> jobConfigurationModels = new LinkedList<JobConfigurationModel>();
        for (final Map.Entry<String, Collection<JobConfiguration>> entry : jobConfigurationMap.entrySet()) {
            final JobConfigurationModel jobConfigurationModel = new JobConfigurationModel();
            jobConfigurationModel.setJobName(entry.getKey());
            jobConfigurationModel.setJobConfigurations(entry.getValue());
            jobConfigurationModels.add(jobConfigurationModel);
        }
        model.addAttribute("jobConfigurationModels", jobConfigurationModels);
    }

    @RequestMapping(value = "/jobConfigurations/{jobConfigurationId}", method = RequestMethod.GET)
    public void getJobConfiguration(final Model model, @PathVariable("jobConfigurationId") final Long jobConfigurationId) {
        final JobConfiguration jobConfiguration = adminService.getJobConfigurationById(jobConfigurationId);
        model.addAttribute("jobConfiguration", jobConfiguration);
    }

    @RequestMapping(value = "/jobConfigurationAdd", method = RequestMethod.GET)
    public void initAddJobConfiguration(final Model model) {
        final JobConfigurationAddModel jobConfigurationAddModel = new JobConfigurationAddModel();
        model.addAttribute("jobConfigurationAddModel", jobConfigurationAddModel);
        model.addAttribute("jobNames", jobService.getJobNames());
        model.addAttribute("jobSchedulerTypes", JobSchedulerType.values());
        model.addAttribute("taskExecutorTypes", TaskExecutorType.values());
    }

    @RequestMapping(value = "/jobConfigurationEdit/{jobConfigurationId}", method = RequestMethod.GET)
    public String initEditJobConfiguration(@PathVariable("jobConfigurationId") final Long jobConfigurationId,
                                           final Model model) {
        final JobConfiguration jobConfiguration = adminService.getJobConfigurationById(jobConfigurationId);
        final JobConfigurationAddModel jobConfigurationAddModel = new JobConfigurationAddModel();
        jobConfigurationAddModel.setJobName(jobConfiguration.getJobName());
        jobConfigurationAddModel.setJobParameters(ParameterParser.parseParameterMapToString(jobConfiguration.getJobParameters()));
        jobConfigurationAddModel.setJobSchedulerType(jobConfiguration.getJobSchedulerConfiguration()
                .getJobSchedulerType());
        jobConfigurationAddModel.setTaskExecutorType(jobConfiguration.getJobSchedulerConfiguration()
                .getTaskExecutorType());
        jobConfigurationAddModel.setCronExpression(jobConfiguration.getJobSchedulerConfiguration().getCronExpression());
        jobConfigurationAddModel.setFixedDelay(jobConfiguration.getJobSchedulerConfiguration().getFixedDelay());
        jobConfigurationAddModel.setInitialDelay(jobConfiguration.getJobSchedulerConfiguration().getInitialDelay());
        jobConfigurationAddModel.setJobConfigurationId(jobConfigurationId);
        model.addAttribute("jobConfigurationAddModel", jobConfigurationAddModel);
        model.addAttribute("jobSchedulerTypes", JobSchedulerType.values());
        model.addAttribute("taskExecutorTypes", TaskExecutorType.values());
        return "jobConfigurationEdit";
    }

    @RequestMapping(value = "/jobConfigurationAdd", method = RequestMethod.POST)
    public String addJobConfiguration(
            @ModelAttribute("jobConfigurationAddModel") final JobConfigurationAddModel jobConfigurationAddModel) {
        final JobConfiguration jobConfiguration = mapModelToJobConfiguration(jobConfigurationAddModel);
        adminService.saveJobConfiguration(jobConfiguration);
        return "redirect:/jobConfigurations";
    }

    @RequestMapping(value = "/jobConfigurationEdit", method = RequestMethod.POST)
    public String updateJobConfiguration(
            @ModelAttribute("jobConfigurationAddModel") final JobConfigurationAddModel jobConfigurationAddModel) {
        final JobConfiguration currentJobConfiguration = adminService.getJobConfigurationById(jobConfigurationAddModel
                .getJobConfigurationId());
        final JobConfiguration jobConfiguration = mapModelToJobConfiguration(jobConfigurationAddModel);
        jobConfiguration.getJobSchedulerConfiguration().setBeanName(
                currentJobConfiguration.getJobSchedulerConfiguration().getBeanName());
        adminService.updateJobConfiguration(jobConfiguration);
        return "redirect:/jobConfigurations";
    }

    @RequestMapping(value = "/jobConfigurations/delete/{jobConfigurationId}", method = RequestMethod.POST)
    public String deleteJobConfiguration(@PathVariable("jobConfigurationId") final Long jobConfigurationId) {
        adminService.deleteJobConfiguration(jobConfigurationId);
        return "redirect:/jobConfigurations";
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
        final Map<String, Object> parameters = ParameterParser.parseParameters(jobConfigurationAddModel
                .getJobParameters());
        final JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setJobName(jobConfigurationAddModel.getJobName());
        jobConfiguration.setJobSchedulerConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobParameters(parameters);
        jobConfiguration.setJobIncrementer(JobIncrementer.DATE);
        jobConfiguration.setJobConfigurationId(jobConfigurationAddModel.getJobConfigurationId());
        return jobConfiguration;
    }

}
