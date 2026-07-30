package org.tuxdevelop.spring.batch.lightmin.server.fe.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ApplicationContextModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ModificationTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.MapJobSchedulerConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.SchedulerJobConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.JobSchedulerModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.SchedulerTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.service.JobSchedulerFeService;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class JobSchedulersController extends CommonController {

    private final JobSchedulerFeService jobSchedulerFeService;
    private final Validator validator;

    public JobSchedulersController(final JobSchedulerFeService jobSchedulerFeService, final Validator validator) {
        this.jobSchedulerFeService = jobSchedulerFeService;
        this.validator = validator;
    }

    @GetMapping(value = "/job-schedulers")
    public void init(final Model model,
                     @RequestParam(name = "application-instance-id") final String applicationInstanceId) {

        this.initSchedulersModel(model, applicationInstanceId);
    }

    @PostMapping(value = "/job-schedulers", params = "start-scheduler")
    public void startScheduler(
            final Model model,
            @RequestParam(name = "job-configuration-id") final Long jobConfigurationId,
            @RequestParam(name = "application-instance-id") final String applicationInstanceId) {
        this.jobSchedulerFeService.startScheduler(jobConfigurationId, applicationInstanceId);
        this.init(model, applicationInstanceId);
    }

    @PostMapping(value = "/job-schedulers", params = "stop-scheduler")
    public void stopScheduler(
            final Model model,
            @RequestParam(name = "job-configuration-id") final Long jobConfigurationId,
            @RequestParam(name = "application-instance-id") final String applicationInstanceId) {
        this.jobSchedulerFeService.stopScheduler(jobConfigurationId, applicationInstanceId);
        this.init(model, applicationInstanceId);
    }

    @PostMapping(value = "/job-schedulers", params = "delete-scheduler")
    public void deleteSchedulerConfiguration(
            final Model model,
            @RequestParam(name = "job-configuration-id") final Long jobConfigurationId,
            @RequestParam(name = "application-instance-id") final String applicationInstanceId) {
        this.jobSchedulerFeService.deleteSchedulerConfiguration(jobConfigurationId, applicationInstanceId);
        this.init(model, applicationInstanceId);
    }

    @GetMapping(value = "/job-scheduler", params = {"init-add-scheduler"})
    public void initSchedulerAdd(
            final Model model,
            @RequestParam(name = "scheduler-type") final String schedulerType,
            @RequestParam(name = "application-instance-id") final String applicationInstanceId) {


        final SchedulerJobConfigurationModel schedulerConfiguration;
        final JobSchedulerModel jobSchedulerModel;

        if (model.containsAttribute("schedulerConfiguration")) {
            schedulerConfiguration = (SchedulerJobConfigurationModel) model.asMap().get("schedulerConfiguration");
            jobSchedulerModel = schedulerConfiguration.getConfig();
        } else {
            schedulerConfiguration = new SchedulerJobConfigurationModel();
            jobSchedulerModel = new JobSchedulerModel();
            jobSchedulerModel.setType(schedulerType);
            schedulerConfiguration.setConfig(jobSchedulerModel);
        }
        jobSchedulerModel.setTypeRead(new SchedulerTypeModel(SchedulerTypeModel.JobSchedulerType.valueOf(schedulerType)));

        model.addAttribute("schedulerConfiguration", schedulerConfiguration);
        model.addAttribute("modificationType", new ModificationTypeModel(ModificationTypeModel.ModificationType.ADD));
        this.initApplicationContextModel(model, applicationInstanceId);
    }

    @GetMapping(value = "/job-scheduler", params = {"init-edit-scheduler"})
    public void initSchedulerUpdate(
            final Model model,
            @RequestParam(name = "job-configuration-id") final Long jobConfigurationId,
            @RequestParam(name = "application-instance-id") final String applicationInstanceId) {

        final SchedulerJobConfigurationModel schedulerConfiguration;

        if (model.containsAttribute("schedulerConfiguration")) {
            schedulerConfiguration = (SchedulerJobConfigurationModel) model.asMap().get("schedulerConfiguration");
            final JobSchedulerModel schedulerModel = schedulerConfiguration.getConfig();
            final String schedulerType = schedulerModel.getType();
            schedulerModel.setTypeRead(new SchedulerTypeModel(SchedulerTypeModel.JobSchedulerType.valueOf(schedulerType)));
        } else {
            schedulerConfiguration = this.jobSchedulerFeService.getJobConfigurationModel(jobConfigurationId, applicationInstanceId);
        }

        model.addAttribute("schedulerConfiguration", schedulerConfiguration);
        model.addAttribute("modificationType", new ModificationTypeModel(ModificationTypeModel.ModificationType.UPDATE));
        this.initApplicationContextModel(model, applicationInstanceId);
    }


    @PostMapping(value = "/job-scheduler", params = {"add-scheduler"})
    public RedirectView addScheduler(
            @ModelAttribute("schedulerConfiguration") final SchedulerJobConfigurationModel schedulerConfiguration,
            final BindingResult bindingResult,
            @RequestParam(name = "application-instance-id") final String applicationInstanceId,
            final HttpServletRequest request,
            final RedirectAttributes redirectAttributes) {

        this.validator.validate(schedulerConfiguration, bindingResult);

        final RedirectView redirectView;

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("schedulerConfiguration", schedulerConfiguration);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "schedulerConfiguration", bindingResult);
            final String schedulerType = schedulerConfiguration.getConfig().getType();
            redirectView = this.createRedirectView(
                    "job-scheduler?init-add-scheduler=param-error"
                            + "&scheduler-type=" + schedulerType
                            + "&application-instance-id=" + applicationInstanceId,
                    request);
        } else {
            this.jobSchedulerFeService.addSchedulerConfiguration(schedulerConfiguration, applicationInstanceId);
            redirectView = this.createRedirectView("job-schedulers?application-instance-id=" + applicationInstanceId, request);
        }

        return redirectView;
    }

    @PostMapping(value = "/job-scheduler", params = {"update-scheduler"})
    public RedirectView updateScheduler(
            @ModelAttribute("schedulerConfiguration") final SchedulerJobConfigurationModel schedulerConfiguration,
            final BindingResult bindingResult,
            @RequestParam(name = "application-instance-id") final String applicationInstanceId,
            final HttpServletRequest request,
            final RedirectAttributes redirectAttributes) {
        this.validator.validate(schedulerConfiguration, bindingResult);

        final RedirectView redirectView;

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("schedulerConfiguration", schedulerConfiguration);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "schedulerConfiguration", bindingResult);
            final Long jobConfigurationId = schedulerConfiguration.getId();
            redirectView = this.createRedirectView(
                    "job-scheduler?init-edit-scheduler=param-error"
                            + "&job-configuration-id=" + jobConfigurationId
                            + "&application-instance-id=" + applicationInstanceId,
                    request);
        } else {
            this.jobSchedulerFeService.updateSchedulerConfiguration(schedulerConfiguration, applicationInstanceId);
            redirectView =
                    this.createRedirectView("job-schedulers?application-instance-id=" + applicationInstanceId, request);
        }

        return redirectView;
    }


    private void initSchedulersModel(
            final Model model,
            @RequestParam(name = "application-instance-id") final String applicationInstanceId) {
        this.initApplicationContextModel(model, applicationInstanceId);

        final MapJobSchedulerConfigurationModel jobSchedulerConfigurations =
                this.jobSchedulerFeService.getMapJobConfigurationModel(applicationInstanceId);

        model.addAttribute("mapJobConfigurations", jobSchedulerConfigurations);
    }

    private void initApplicationContextModel(final Model model, @RequestParam(name = "application-instance-id") final String applicationInstanceId) {
        final ApplicationContextModel applicationContextModel =
                this.jobSchedulerFeService.getApplicationContextModel(applicationInstanceId);
        model.addAttribute("applicationContextModel", applicationContextModel);
    }
}
