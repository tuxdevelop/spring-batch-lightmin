package org.tuxdevelop.spring.batch.lightmin.controller;

import java.util.Collection;
import java.util.LinkedList;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.batch.lightmin.model.JobExecutionModel;
import org.tuxdevelop.spring.batch.lightmin.model.JobInfoModel;
import org.tuxdevelop.spring.batch.lightmin.model.JobInstanceModel;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;

@Controller
@RequestMapping(value = "/jobs")
public class JobController {

	@Autowired
	private JobService jobService;

	@RequestMapping(method = RequestMethod.GET)
	public void getJobs(final Model model) {
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
		model.addAttribute("jobExecutions", jobExecutionModels);
		return "jobExecutions";
	}

	@RequestMapping(value = "/execution/{jobExecutionId}", method = RequestMethod.GET)
	public String getJobExecution(final ModelMap modelMap,
			@PathVariable(value = "jobExecutionId") final Long jobExecutionId) {
		final JobExecution jobExecution = jobService.getJobExecution(jobExecutionId);
		final JobExecutionModel jobExecutionModel = new JobExecutionModel();
		jobExecutionModel.setJobExecution(jobExecution);
		jobExecutionModel.setJobInstanceId(jobExecution.getJobInstance().getId());
		modelMap.put("jobExecution", jobExecutionModel);
		return "jobExecution";
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
