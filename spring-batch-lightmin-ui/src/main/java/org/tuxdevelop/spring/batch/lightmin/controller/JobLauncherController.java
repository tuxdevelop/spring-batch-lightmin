package org.tuxdevelop.spring.batch.lightmin.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.model.JobLauncherModel;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;
import org.tuxdevelop.spring.batch.lightmin.util.ParameterParser;

@Controller
@RequestMapping("/jobLaunchers")
public class JobLauncherController {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobService jobService;

	@Autowired
	private JobRegistry JobRegistry;

	@RequestMapping(method = RequestMethod.GET)
	public void initJobLaunchers(final Model model) {
		model.addAttribute("jobNames", jobService.getJobNames());
		model.addAttribute("jobLauncherModel", new JobLauncherModel());
	}

	@RequestMapping(method = RequestMethod.POST)
	public String launchJob(@ModelAttribute("jobLauncherModel") final JobLauncherModel jobLauncherModel) {
		final String jobName = jobLauncherModel.getJobName();
		try {
			final Job job = JobRegistry.getJob(jobName);
			final JobParameters jobParameters = ParameterParser.parseParametersToJobParameters(jobLauncherModel
					.getJobParameters());
			jobLauncher.run(job, jobParameters);
		} catch (final Exception e) {
			throw new SpringBatchLightminApplicationException(e, e.getMessage());
		}
		return "redirect:jobLaunchers";
	}
}
