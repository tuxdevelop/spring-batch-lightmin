package org.tuxdevelop.spring.batch.lightmin.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.batch.lightmin.model.*;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;
import org.tuxdevelop.spring.batch.lightmin.service.StepService;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Marcel Becker
 * @author Lars Thielmann
 * @since 0.1
 */
@Controller
public class JobController extends CommonController {

    private final JobService jobService;
    private final StepService stepService;

    @Autowired
    public JobController(final JobService jobService, final StepService stepService) {
        this.jobService = jobService;
        this.stepService = stepService;
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    public void initJobs(final Model model) {
        final Collection<JobInfoModel> jobInfoModels = new LinkedList<JobInfoModel>();
        final Collection<String> jobNames = jobService.getJobNames();
        for (final String jobName : jobNames) {
            final JobInfoModel jobInfoModel = new JobInfoModel();
            jobInfoModel.setJobName(jobName);
            final int instanceCount = jobService.getJobInstanceCount(jobName);
            jobInfoModel.setInstanceCount(instanceCount);
            jobInfoModels.add(jobInfoModel);
        }
        model.addAttribute("jobs", jobInfoModels);
    }

    @RequestMapping(value = "job", method = RequestMethod.GET)
    public String getJob(final Model model, @RequestParam("jobname") final String jobName,
                         @RequestParam(value = "startindex", defaultValue = "0") final int startIndex,
                         @RequestParam(value = "pagesize", defaultValue = "10") final int pageSize) {
        final Collection<JobInstanceModel> jobInstanceModels = new LinkedList<JobInstanceModel>();
        final Job job = jobService.getJobByName(jobName);
        Integer totalJobInstances = 0;
        if (job != null) {
            totalJobInstances = jobService.getJobInstanceCount(jobName);
            final Collection<JobInstance> jobInstances = jobService.getJobInstances(jobName, startIndex, pageSize);
            for (final JobInstance jobInstance : jobInstances) {
                final JobInstanceModel jobInstanceModel = new JobInstanceModel();
                jobInstanceModel.setJobName(jobName);
                jobInstanceModel.setJobInstanceId(jobInstance.getInstanceId());
                enrichJobInstanceModel(jobInstanceModel, jobInstance);
                jobInstanceModels.add(jobInstanceModel);
            }
        }

        final PageModel pageModel = new PageModel(startIndex, pageSize, totalJobInstances);
        model.addAttribute("jobName", jobName);
        model.addAttribute("jobPage", pageModel);
        model.addAttribute("jobInstances", jobInstanceModels);
        return "job";
    }

    @RequestMapping(value = "/executions", method = RequestMethod.GET)
    public String getJobExecutions(final Model model, @RequestParam("jobInstanceId") final Long jobInstanceId,
                                   @RequestParam(value = "startindex", defaultValue = "0") final int startIndex,
                                   @RequestParam(value = "pagesize", defaultValue = "10") final int pageSize) {
        final JobInstance jobInstance = jobService.getJobInstance(jobInstanceId);
        final int totalJobExecutions = jobService.getJobExecutionCount(jobInstance);
        final Collection<JobExecution> jobExecutions = jobService.getJobExecutions(jobInstance, startIndex, pageSize);
        final Collection<JobExecutionModel> jobExecutionModels = new LinkedList<JobExecutionModel>();
        for (final JobExecution jobExecution : jobExecutions) {
            final JobExecutionModel jobExecutionModel = new JobExecutionModel();
            jobExecutionModel.setJobExecution(jobExecution);
            jobExecutionModel.setJobInstanceId(jobInstanceId);
            jobExecutionModel.setJobName(jobInstance.getJobName());
            jobExecutionModels.add(jobExecutionModel);
        }
        final PageModel pageModel = new PageModel(startIndex, pageSize, totalJobExecutions);
        model.addAttribute("jobName", jobInstance.getJobName());
        model.addAttribute("jobExecutions", jobExecutionModels);
        model.addAttribute("pageModel", pageModel);
        model.addAttribute("jobInstanceId", jobInstanceId);
        return "jobExecutions";
    }

    @RequestMapping(value = "/execution", method = RequestMethod.GET)
    public String getJobExecution(final ModelMap modelMap,
                                  @RequestParam(value = "jobExecutionId") final Long jobExecutionId) {
        final JobExecution jobExecution = jobService.getJobExecution(jobExecutionId);
        final JobExecutionModel jobExecutionModel = new JobExecutionModel();
        jobService.attachJobInstance(jobExecution);
        jobExecutionModel.setJobExecution(jobExecution);
        jobExecutionModel.setJobInstanceId(jobExecution.getJobInstance().getInstanceId());
        jobExecutionModel.setJobName(jobExecution.getJobInstance().getJobName());
        stepService.attachStepExecutions(jobExecution);
        enrichJobExecution(jobExecutionModel, jobExecution.getStepExecutions());
        modelMap.put("jobExecution", jobExecutionModel);
        return "jobExecution";
    }

    @RequestMapping(value = "/executionRestart", method = RequestMethod.POST)
    public String restartJobExecution(@RequestParam(value = "jobExecutionId") final Long jobExecutionId,
                                      @RequestParam(value = "jobInstanceId") final Long jobInstanceId) {
        jobService.restartJobExecution(jobExecutionId);
        return "redirect:executions?jobInstanceId=" + jobInstanceId;
    }

    @RequestMapping(value = "/executionStop", method = RequestMethod.POST)
    public String stopJobExecution(@RequestParam(value = "jobExecutionId") final Long jobExecutionId,
                                   @RequestParam(value = "jobInstanceId") final Long jobInstanceId) {
        jobService.stopJobExecution(jobExecutionId);
        return "redirect:executions?jobInstanceId=" + jobInstanceId;
    }

    void enrichJobExecution(final JobExecutionModel jobExecutionModel,
                            Collection<StepExecution> stepExecutions) {
        final Collection<StepExecutionModel> stepExecutionModels = new LinkedList<StepExecutionModel>();
        for (final StepExecution stepExecution : stepExecutions) {
            final StepExecutionModel stepExecutionModel = new StepExecutionModel();
            stepExecutionModel.setStepExecution(stepExecution);
            stepExecutionModel.setJobInstanceId(jobExecutionModel.getJobInstanceId());
            stepExecutionModels.add(stepExecutionModel);
        }
        jobExecutionModel.setStepExecutions(stepExecutionModels);
    }

    void enrichJobInstanceModel(final JobInstanceModel jobInstanceModel, final JobInstance jobInstance) {
        final Collection<JobExecutionModel> jobExecutionModels = new LinkedList<JobExecutionModel>();
        final Collection<JobExecution> jobExecutions = jobService.getJobExecutions(jobInstance);
        for (final JobExecution jobExecution : jobExecutions) {
            final JobExecutionModel jobExecutionModel = new JobExecutionModel();
            jobExecutionModel.setJobInstanceId(jobInstance.getInstanceId());
            jobExecutionModel.setJobExecution(jobExecution);
            jobExecutionModel.setJobName(jobInstanceModel.getJobName());
            jobExecutionModels.add(jobExecutionModel);
        }
        jobInstanceModel.setJobExecutions(jobExecutionModels);
    }
}
