package org.tuxdevelop.spring.batch.lightmin.server.fe.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ApplicationContextModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.launcher.JobLauncherModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.service.JobLauncherFeService;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class JobLauncherController extends CommonController {

    private final JobLauncherFeService jobLauncherFeService;
    private final Validator validator;

    public JobLauncherController(final JobLauncherFeService jobLauncherFeService, final Validator validator) {
        this.jobLauncherFeService = jobLauncherFeService;
        this.validator = validator;
    }

    @RequestMapping(value = "/job-launcher")
    public void init(final Model model,
                     @RequestParam(name = "job-name") final String jobName,
                     @RequestParam(name = "application-instance-id") final String applicationInstanceId) {

        final JobLauncherModel jobLauncherModel;

        if (model.containsAttribute("jobLauncher")) {
            jobLauncherModel = (JobLauncherModel) model.asMap().get("jobLauncher");
        } else {
            jobLauncherModel = this.jobLauncherFeService.getJobLauncherModel(jobName, applicationInstanceId);
        }

        final ApplicationContextModel applicationContextModel =
                this.jobLauncherFeService.getApplicationContextModel(applicationInstanceId);
        applicationContextModel.setJobName(jobName);

        model.addAttribute("jobLauncher", jobLauncherModel);
        model.addAttribute("applicationContextModel", applicationContextModel);
    }

    @PostMapping(value = "/job-launcher")
    public RedirectView launchJob(@ModelAttribute("jobLauncher") final JobLauncherModel jobLauncherModel,
                                  final BindingResult bindingResult,
                                  final HttpServletRequest request,
                                  final RedirectAttributes redirectAttributes) {

        this.validator.validate(jobLauncherModel, bindingResult);

        final RedirectView redirectView;

        final String jobName = jobLauncherModel.getJobName();
        final String applicationInstanceId = jobLauncherModel.getApplicationInstanceId();

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("jobLauncher", jobLauncherModel);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "jobLauncher", bindingResult);
            redirectView = this.createRedirectView(
                    "job-launcher?job-name=" + jobName
                            + "&application-instance-id=" + applicationInstanceId,
                    request);
        } else {
            this.jobLauncherFeService.launchJob(jobLauncherModel);
            redirectView = this.createRedirectView(
                    "batch-job-executions?job-name=" + jobName + "&application-instance-id=" + applicationInstanceId,
                    request);
        }


        return redirectView;
    }
}
