package org.tuxdevelop.spring.batch.lightmin.server.fe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ApplicationContextModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ModificationTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.ListenerJobConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.MapJobListenerConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.JobListenerModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.ListenerTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.service.JobListenerFeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@Validated
public class JobListenersController extends CommonController {

    private final JobListenerFeService jobListenerFeService;
    private final Validator validator;


    public JobListenersController(final JobListenerFeService jobListenerFeService,
                                  final Validator validator) {
        this.jobListenerFeService = jobListenerFeService;
        this.validator = validator;
    }


    @GetMapping(value = "/job-listeners")
    public void init(final Model model,
                     @RequestParam(name = "application-instance-id") final String applicationInstanceId) {

        this.initListenersModel(model, applicationInstanceId);
    }

    @PostMapping(value = "/job-listeners", params = "start-listener")
    public void startListener(
            final Model model,
            @RequestParam(name = "job-configuration-id") final Long jobConfigurationId,
            @RequestParam(name = "application-instance-id") final String applicationInstanceId) {
        this.jobListenerFeService.startListener(jobConfigurationId, applicationInstanceId);
        this.init(model, applicationInstanceId);
    }

    @PostMapping(value = "/job-listeners", params = "stop-listener")
    public void stopListener(
            final Model model,
            @RequestParam(name = "job-configuration-id") final Long jobConfigurationId,
            @RequestParam(name = "application-instance-id") final String applicationInstanceId) {
        this.jobListenerFeService.stopListener(jobConfigurationId, applicationInstanceId);
        this.init(model, applicationInstanceId);
    }

    @PostMapping(value = "/job-listeners", params = "delete-listener")
    public void deleteListenerConfiguration(
            final Model model,
            @RequestParam(name = "job-configuration-id") final Long jobConfigurationId,
            @RequestParam(name = "application-instance-id") final String applicationInstanceId) {
        this.jobListenerFeService.deleteListenerConfiguration(jobConfigurationId, applicationInstanceId);
        this.init(model, applicationInstanceId);
    }

    @GetMapping(value = "/job-listener", params = {"init-add-listener"})
    public void initListenerAdd(
            final Model model,
            @RequestParam(name = "listener-type") final String listenerType,
            @RequestParam(name = "application-instance-id") final String applicationInstanceId) {

        final ListenerJobConfigurationModel listenerConfiguration;
        final JobListenerModel listenerModel;
        if (model.containsAttribute("listenerConfiguration")) {
            listenerConfiguration = (ListenerJobConfigurationModel) model.asMap().get("listenerConfiguration");
            listenerModel = listenerConfiguration.getConfig();
        } else {
            listenerConfiguration = new ListenerJobConfigurationModel();
            listenerModel = new JobListenerModel();
            listenerModel.setType(listenerType);
            listenerConfiguration.setConfig(listenerModel);
        }
        listenerModel.setTypeRead(new ListenerTypeModel(ListenerTypeModel.JobListenerType.valueOf(listenerType)));

        model.addAttribute("listenerConfiguration", listenerConfiguration);
        model.addAttribute("modificationType", new ModificationTypeModel(ModificationTypeModel.ModificationType.ADD));
        this.initApplicationContextModel(model, applicationInstanceId);
    }

    @GetMapping(value = "/job-listener", params = {"init-edit-listener"})
    public void initSchedulerUpdate(
            final Model model,
            @RequestParam(name = "job-configuration-id") final Long jobConfigurationId,
            @RequestParam(name = "application-instance-id") final String applicationInstanceId) {

        final ListenerJobConfigurationModel listenerConfiguration;

        if (model.containsAttribute("listenerConfiguration")) {
            listenerConfiguration = (ListenerJobConfigurationModel) model.asMap().get("listenerConfiguration");
            final JobListenerModel listenerModel = listenerConfiguration.getConfig();
            final String listenerType = listenerModel.getType();
            listenerModel.setTypeRead(new ListenerTypeModel(ListenerTypeModel.JobListenerType.valueOf(listenerType)));
        } else {
            listenerConfiguration = this.jobListenerFeService.getJobConfigurationModel(jobConfigurationId, applicationInstanceId);
        }

        model.addAttribute("listenerConfiguration", listenerConfiguration);
        model.addAttribute("modificationType", new ModificationTypeModel(ModificationTypeModel.ModificationType.UPDATE));
        this.initApplicationContextModel(model, applicationInstanceId);
    }

    @PostMapping(value = "/job-listener", params = {"add-listener"})
    public RedirectView addScheduler(
            @RequestParam(name = "application-instance-id") final String applicationInstanceId,
            @ModelAttribute("listenerConfiguration") final ListenerJobConfigurationModel listenerConfiguration,
            final BindingResult bindingResult,
            final HttpServletRequest request,
            final RedirectAttributes redirectAttributes) {

        this.validator.validate(listenerConfiguration, bindingResult);

        final RedirectView redirectView;

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("listenerConfiguration", listenerConfiguration);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "listenerConfiguration", bindingResult);
            final String listenerType = listenerConfiguration.getConfig().getType();
            redirectView = this.createRedirectView(
                    "job-listener?init-add-listener=param-error"
                            + "&listener-type=" + listenerType
                            + "&application-instance-id=" + applicationInstanceId,
                    request);
        } else {

            this.jobListenerFeService.addListenerConfiguration(listenerConfiguration, applicationInstanceId);
            redirectView =
                    this.createRedirectView("job-listeners?application-instance-id=" + applicationInstanceId, request);
        }
        return redirectView;
    }

    @PostMapping(value = "/job-listener", params = {"update-listener"})
    public RedirectView updateScheduler(
            @ModelAttribute("listenerConfiguration") final ListenerJobConfigurationModel listenerConfiguration,
            final BindingResult bindingResult,
            @RequestParam(name = "application-instance-id") final String applicationInstanceId,
            final HttpServletRequest request,
            final RedirectAttributes redirectAttributes) {

        this.validator.validate(listenerConfiguration, bindingResult);

        final RedirectView redirectView;

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("listenerConfiguration", listenerConfiguration);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "listenerConfiguration", bindingResult);
            final Long jobConfigurationId = listenerConfiguration.getId();
            redirectView = this.createRedirectView(
                    "job-listener?init-edit-listener=param-error"
                            + "&job-configuration-id=" + jobConfigurationId
                            + "&application-instance-id=" + applicationInstanceId,
                    request);
        } else {
            this.jobListenerFeService.updateListenerConfiguration(listenerConfiguration, applicationInstanceId);
            redirectView =
                    this.createRedirectView("job-listeners?application-instance-id=" + applicationInstanceId, request);
        }

        return redirectView;
    }

    private void initListenersModel(
            final Model model,
            @RequestParam(name = "application-instance-id") final String applicationInstanceId) {
        this.initApplicationContextModel(model, applicationInstanceId);

        final MapJobListenerConfigurationModel jobSchedulerConfigurations =
                this.jobListenerFeService.getMapJobConfigurationModel(applicationInstanceId);

        model.addAttribute("mapJobConfigurations", jobSchedulerConfigurations);
    }

    private void initApplicationContextModel(final Model model, @RequestParam(name = "application-instance-id") final String applicationInstanceId) {
        final ApplicationContextModel applicationContextModel =
                this.jobListenerFeService.getApplicationContextModel(applicationInstanceId);
        model.addAttribute("applicationContextModel", applicationContextModel);
    }

}
