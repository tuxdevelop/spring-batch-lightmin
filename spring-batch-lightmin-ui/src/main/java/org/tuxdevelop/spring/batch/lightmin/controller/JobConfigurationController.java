package org.tuxdevelop.spring.batch.lightmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.model.JobConfigurationAddModel;
import org.tuxdevelop.spring.batch.lightmin.model.JobConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.service.AdminService;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;
import org.tuxdevelop.spring.batch.lightmin.util.ParameterParser;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Controller
public class JobConfigurationController extends CommonController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JobService jobService;

    @RequestMapping(value = "/jobConfigurations", method = RequestMethod.GET)
    public void getJobConfigurations(final Model model) {
        final Collection<JobConfigurationModel> jobConfigurationModels = getJobConfigurationModels();
        model.addAttribute("jobConfigurationModels", jobConfigurationModels);
    }

    @RequestMapping(value = "/jobConfigurationAdd", method = RequestMethod.GET)
    public void initAddJobConfiguration(final Model model) {
        final JobConfigurationAddModel jobConfigurationAddModel = new JobConfigurationAddModel();
        model.addAttribute("jobConfigurationAddModel", jobConfigurationAddModel);
        model.addAttribute("jobNames", jobService.getJobNames());
        model.addAttribute("jobSchedulerTypes", JobSchedulerType.values());
        model.addAttribute("taskExecutorTypes", TaskExecutorType.values());
        model.addAttribute("schedulerStatusValues", getSelectableSchedulerStatus());
        model.addAttribute("jobIncrementerTypes", JobIncrementer.values());
    }

    @RequestMapping(value = "/jobConfigurationEdit", method = RequestMethod.GET)
    public String initEditJobConfiguration(@RequestParam("jobConfigurationId") final Long jobConfigurationId,
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
        jobConfigurationAddModel.setSchedulerStatus(jobConfiguration.getJobSchedulerConfiguration().getSchedulerStatus());
        jobConfigurationAddModel.setJobIncrementer(jobConfiguration.getJobIncrementer());
        model.addAttribute("jobConfigurationAddModel", jobConfigurationAddModel);
        model.addAttribute("jobSchedulerTypes", JobSchedulerType.values());
        model.addAttribute("taskExecutorTypes", TaskExecutorType.values());
        model.addAttribute("jobIncrementerTypes", JobIncrementer.values());
        return "jobConfigurationEdit";
    }

    @RequestMapping(value = "/jobConfigurationAdd", method = RequestMethod.POST)
    public String addJobConfiguration(
            @ModelAttribute("jobConfigurationAddModel") final JobConfigurationAddModel jobConfigurationAddModel) {
        final JobConfiguration jobConfiguration = mapModelToJobConfiguration(jobConfigurationAddModel);
        adminService.saveJobConfiguration(jobConfiguration);
        return "redirect:jobConfigurations";
    }

    @RequestMapping(value = "/jobConfigurationEdit", method = RequestMethod.POST)
    public String updateJobConfiguration(
            @ModelAttribute("jobConfigurationAddModel") final JobConfigurationAddModel jobConfigurationAddModel) {
        final JobConfiguration currentJobConfiguration = adminService.getJobConfigurationById
                (jobConfigurationAddModel.getJobConfigurationId());
        final JobConfiguration jobConfiguration = mapModelToJobConfiguration(jobConfigurationAddModel);
        jobConfiguration.getJobSchedulerConfiguration().setBeanName(
                currentJobConfiguration.getJobSchedulerConfiguration().getBeanName());
        adminService.updateJobConfiguration(jobConfiguration);
        return "redirect:jobConfigurations";
    }

    @RequestMapping(value = "/jobConfigurations", method = RequestMethod.POST)
    public String deleteJobConfiguration(@RequestParam("jobConfigurationId") final long jobConfigurationId,
                                         final Model model) {
        adminService.deleteJobConfiguration(jobConfigurationId);
        final Collection<JobConfigurationModel> jobConfigurationModels = getJobConfigurationModels();
        model.addAttribute("jobConfigurationModels", jobConfigurationModels);
        return "redirect:jobConfigurations";
    }

    @RequestMapping(value = "/jobConfigurationSchedulerStart", method = RequestMethod.POST)
    public String startJobConfigurationScheduler(@RequestParam("jobConfigurationId") final long jobConfigurationId, final
    Model model) {
        adminService.startJobConfigurationScheduler(jobConfigurationId);
        final Collection<JobConfigurationModel> jobConfigurationModels = getJobConfigurationModels();
        model.addAttribute("jobConfigurationModels", jobConfigurationModels);
        return "redirect:jobConfigurations";
    }

    @RequestMapping(value = "/jobConfigurationSchedulerStop", method = RequestMethod.POST)
    public String stopJobConfigurationScheduler(@RequestParam("jobConfigurationId") final long jobConfigurationId, final
    Model model) {
        adminService.stopJobConfigurationScheduler(jobConfigurationId);
        final Collection<JobConfigurationModel> jobConfigurationModels = getJobConfigurationModels();
        model.addAttribute("jobConfigurationModels", jobConfigurationModels);
        return "redirect:jobConfigurations";
    }

    Collection<JobConfigurationModel> getJobConfigurationModels() {
        final Map<String, Collection<JobConfiguration>> jobConfigurationMap = adminService.getJobConfigurationMap
                (jobService.getJobNames());
        final Collection<JobConfigurationModel> jobConfigurationModels = new LinkedList<JobConfigurationModel>();
        for (final Map.Entry<String, Collection<JobConfiguration>> entry : jobConfigurationMap.entrySet()) {
            final JobConfigurationModel jobConfigurationModel = new JobConfigurationModel();
            jobConfigurationModel.setJobName(entry.getKey());
            jobConfigurationModel.setJobConfigurations(entry.getValue());
            jobConfigurationModels.add(jobConfigurationModel);
        }
        return jobConfigurationModels;
    }

    JobConfiguration mapModelToJobConfiguration(final JobConfigurationAddModel jobConfigurationAddModel) {
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
        final Map<String, Object> parameters = ParameterParser.parseParameters(jobConfigurationAddModel
                .getJobParameters());
        final JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setJobName(jobConfigurationAddModel.getJobName());
        jobConfiguration.setJobSchedulerConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobParameters(parameters);
        jobConfiguration.setJobIncrementer(jobConfigurationAddModel.getJobIncrementer());
        jobConfiguration.setJobConfigurationId(jobConfigurationAddModel.getJobConfigurationId());
        return jobConfiguration;
    }


    Collection<SchedulerStatus> getSelectableSchedulerStatus() {
        final Collection<SchedulerStatus> schedulerStatuses = new LinkedList<SchedulerStatus>();
        schedulerStatuses.add(SchedulerStatus.INITIALIZED);
        schedulerStatuses.add(SchedulerStatus.RUNNING);
        schedulerStatuses.add(SchedulerStatus.STOPPED);
        return schedulerStatuses;
    }
}
