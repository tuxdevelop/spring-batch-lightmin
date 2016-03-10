package org.tuxdevelop.spring.batch.lightmin.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.model.JobLauncherModel;
import org.tuxdevelop.spring.batch.lightmin.model.JobNameModel;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;
import org.tuxdevelop.spring.batch.lightmin.util.ParameterParser;

import java.util.Date;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Controller
@RequestMapping()
public class JobLauncherController extends CommonController {

    private final JobLauncher jobLauncher;
    private final JobService jobService;
    private final JobRegistry JobRegistry;

    @Autowired
    public JobLauncherController(final JobLauncher defaultAsyncJobLauncher,
                                 final JobService jobService,
                                 final JobRegistry jobRegistry) {
        this.jobLauncher = defaultAsyncJobLauncher;
        this.jobService = jobService;
        this.JobRegistry = jobRegistry;
    }

    @RequestMapping(value = "/jobLaunchers", method = RequestMethod.GET)
    public void initJobLaunchers(final Model model) {
        model.addAttribute("jobNames", jobService.getJobNames());
        model.addAttribute("jobName", new JobNameModel());
    }

    @RequestMapping(value = "/jobLauncher", method = RequestMethod.GET)
    public void configureJobLauncher(@RequestParam("jobName") final String jobName, final Model model) {
        final JobLauncherModel jobLauncherModel = new JobLauncherModel();
        final JobParameters oldJobParameters = jobService.getLastJobParameters(jobName);
        jobLauncherModel.setJobName(jobName);
        jobLauncherModel.setJobParameters(ParameterParser.parseJobParametersToString(oldJobParameters));
        model.addAttribute("jobLauncherModel", jobLauncherModel);
        model.addAttribute("jobIncrementers", JobIncrementer.values());
    }

    @RequestMapping(value = "/jobLauncher", method = RequestMethod.POST)
    public String launchJob(@ModelAttribute("jobLauncherModel") final JobLauncherModel jobLauncherModel) {
        final String jobName = jobLauncherModel.getJobName();
        try {
            final Job job = JobRegistry.getJob(jobName);
            final JobParameters parsedJobParameters = ParameterParser.parseParametersToJobParameters(jobLauncherModel
                    .getJobParameters());
            final JobParameters jobParameters = attachJobIncremeters(parsedJobParameters, jobLauncherModel
                    .getJobIncrementer());
            jobLauncher.run(job, jobParameters);
        } catch (final Exception e) {
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
        return "redirect:job?jobname=" + jobName;
    }

    JobParameters attachJobIncremeters(final JobParameters jobParameters, final JobIncrementer jobIncrementer) {
        //possibile values NONE, DATE
        //DATE
        final JobParameters result;
        if (JobIncrementer.DATE.equals(jobIncrementer)) {
            result = new JobParametersBuilder(jobParameters).addDate(jobIncrementer.getIncrementerIdentifier(), new
                    Date()).toJobParameters();
        } else {
            result = jobParameters;
        }
        return result;
    }
}
