package org.tuxdevelop.spring.batch.lightmin.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.batch.lightmin.model.*;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;
import org.tuxdevelop.spring.batch.lightmin.service.StepService;

import java.util.Collection;
import java.util.LinkedList;

@Controller
@RequestMapping(value = "/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private StepService stepService;

    @RequestMapping(method = RequestMethod.GET)
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

    @RequestMapping(value = "/{jobName}", method = RequestMethod.GET)
    public String getJob(final Model model, @PathVariable("jobName") final String jobName, @RequestParam(
            value = "startIndex", defaultValue = "0") final int startIndex, @RequestParam(value = "pageSize",
            defaultValue = "10") final int pageSize) {
        final PageModel pageModel = new PageModel();
        pageModel.setStartIndex(startIndex);
        pageModel.setPageSize(pageSize);
        final Collection<JobInstanceModel> jobInstanceModels = new LinkedList<JobInstanceModel>();
        final Job job = jobService.getJobByName(jobName);
        if (job != null) {
            final Collection<JobInstance> jobInstances = jobService.getJobInstances(jobName, startIndex, pageSize);
            for (final JobInstance jobInstance : jobInstances) {
                final JobInstanceModel jobInstanceModel = new JobInstanceModel();
                jobInstanceModel.setJobName(jobName);
                jobInstanceModel.setJobInstanceId(jobInstance.getInstanceId());
                enrichJobInstanceModel(jobInstanceModel, jobInstance);
                jobInstanceModels.add(jobInstanceModel);
            }
        }
        model.addAttribute("jobName", jobName);
        model.addAttribute("jobPage", pageModel);
        model.addAttribute("jobInstances", jobInstanceModels);
        return "job";
    }

    @RequestMapping(value = "/executions/{jobInstanceId}", method = RequestMethod.GET)
    public String getJobExecutions(final Model model, @PathVariable("jobInstanceId") final Long jobInstanceId) {
        final JobInstance jobInstance = jobService.getJobInstance(jobInstanceId);
        Collection<JobExecution> jobExecutions = jobService.getJobExecutions(jobInstance);
        final Collection<JobExecutionModel> jobExecutionModels = new LinkedList<JobExecutionModel>();
        for (final JobExecution jobExecution : jobExecutions) {
            final JobExecutionModel jobExecutionModel = new JobExecutionModel();
            jobExecutionModel.setJobExecution(jobExecution);
            jobExecutionModel.setJobInstanceId(jobInstanceId);
            jobExecutionModel.setJobName(jobInstance.getJobName());
            jobExecutionModels.add(jobExecutionModel);
        }
        model.addAttribute("jobName", jobInstance.getJobName());
        model.addAttribute("jobExecutions", jobExecutionModels);
        return "jobExecutions";
    }

    @RequestMapping(value = "/executions/execution/{jobExecutionId}", method = RequestMethod.GET)
    public String getJobExecution(final ModelMap modelMap,
                                  @PathVariable(value = "jobExecutionId") final Long jobExecutionId) {
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

    private void enrichJobExecution(final JobExecutionModel jobExecutionModel,
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

    private void enrichJobInstanceModel(final JobInstanceModel jobInstanceModel, final JobInstance jobInstance) {
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
