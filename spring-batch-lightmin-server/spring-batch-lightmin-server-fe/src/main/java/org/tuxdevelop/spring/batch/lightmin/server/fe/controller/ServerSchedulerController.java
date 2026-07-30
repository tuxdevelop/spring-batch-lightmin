package org.tuxdevelop.spring.batch.lightmin.server.fe.controller;

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
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.BooleanModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ContentPageModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ModificationTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler.ServerSchedulerConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler.ServerSchedulerConfigurationStatusModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler.ServerSchedulerConfigurationsModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler.ServerSchedulerInfoPageModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.service.ServerSchedulerFeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ServerSchedulerController extends CommonController {

    private final ServerSchedulerFeService serverSchedulerFeService;
    private final Validator validator;

    public ServerSchedulerController(final ServerSchedulerFeService serverSchedulerFeService,
                                     final Validator validator) {
        this.serverSchedulerFeService = serverSchedulerFeService;
        this.validator = validator;
    }

    @GetMapping(value = "/server-schedulers")
    public RedirectView init(final HttpServletRequest request) {
        return this.createRedirectView("server-scheduler-executions", request);
    }

    //Executions

    @GetMapping(value = "/server-scheduler-executions")
    public void allServerSchedulerExecutions(final Model model,
                                             @RequestParam(name = "start-index", defaultValue = "0") final Integer index,
                                             @RequestParam(name = "page-size", defaultValue = "20") final Integer pageSize,
                                             @RequestParam(name = "state", required = false) final Integer state) {

        this.initExecutionsModel(model, index, pageSize, state);
    }

    @PostMapping(value = "/server-scheduler-executions", params = {"delete-execution"})
    public void deleteExecution(final Model model,
                                @RequestParam(name = "start-index", defaultValue = "0") final Integer index,
                                @RequestParam(name = "page-size", defaultValue = "20") final Integer pageSize,
                                @RequestParam(name = "execution-id") final Long executionId,
                                @RequestParam(name = "state", required = false) final Integer state) {

        this.serverSchedulerFeService.deleteExecution(executionId);
        this.initExecutionsModel(model, index, pageSize, state);
    }

    @PostMapping(value = "/server-scheduler-executions", params = {"stop-execution"})
    public void stopExecution(final Model model,
                              @RequestParam(name = "start-index", defaultValue = "0") final Integer index,
                              @RequestParam(name = "page-size", defaultValue = "20") final Integer pageSize,
                              @RequestParam(name = "execution-id") final Long executionId,
                              @RequestParam(name = "state", required = false) final Integer state) {

        this.serverSchedulerFeService.stopExecution(executionId);
        this.initExecutionsModel(model, index, pageSize, state);

    }

    //Configurations

    @GetMapping(value = "/server-scheduler-configurations")
    public void initServerSchedulerConfigurations(final Model model) {
        this.initSchedulerConfigurationsModel(model);
    }


    @GetMapping(value = "/server-scheduler-configuration", params = {"init-add-configuration"})
    public void initSchedulerConfigurationAdd(
            final Model model,
            @RequestParam(name = "application") final String applicationName) {

        final ServerSchedulerConfigurationModel configuration;

        if (model.containsAttribute("schedulerConfiguration")) {
            configuration = (ServerSchedulerConfigurationModel) model.asMap().get("schedulerConfiguration");
        } else {
            configuration = new ServerSchedulerConfigurationModel();
        }
        configuration.setApplicationName(applicationName);
        final ApplicationContextModel applicationContextModel = this.getApplicationContextModel(applicationName);

        model.addAttribute("schedulerConfiguration", configuration);
        model.addAttribute("modificationType", new ModificationTypeModel(ModificationTypeModel.ModificationType.ADD));
        model.addAttribute("applicationContextModel", applicationContextModel);
        model.addAttribute("schedulerStatus", ServerSchedulerConfigurationStatusModel.ServerSchedulerConfigurationStatusType.values());
        model.addAttribute("booleanSelector", BooleanModel.values());
    }

    @GetMapping(value = "/server-scheduler-configuration", params = {"init-edit-configuration"})
    public void initSchedulerConfigurationEdit(
            final Model model,
            @RequestParam(name = "configuration-id") final long id) {

        final ServerSchedulerConfigurationModel configuration;

        if (model.containsAttribute("schedulerConfiguration")) {
            configuration = (ServerSchedulerConfigurationModel) model.asMap().get("schedulerConfiguration");
        } else {
            configuration = this.serverSchedulerFeService.findById(id);
        }

        final ApplicationContextModel applicationContextModel = this.getApplicationContextModel(configuration.getApplicationName());

        model.addAttribute("schedulerConfiguration", configuration);
        model.addAttribute("modificationType", new ModificationTypeModel(ModificationTypeModel.ModificationType.UPDATE));
        model.addAttribute("applicationContextModel", applicationContextModel);
        model.addAttribute("schedulerStatus", ServerSchedulerConfigurationStatusModel.ServerSchedulerConfigurationStatusType.values());
        model.addAttribute("booleanSelector", BooleanModel.values());
    }

    @PostMapping(value = "/server-scheduler-configuration", params = {"add-scheduler"})
    public RedirectView addSchedulerConfiguration(@ModelAttribute("schedulerConfiguration") final ServerSchedulerConfigurationModel configuration,
                                                  final BindingResult bindingResult,
                                                  final HttpServletRequest request,
                                                  final RedirectAttributes redirectAttributes) {

        this.validator.validate(configuration, bindingResult);

        final RedirectView redirectView;

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("schedulerConfiguration", configuration);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "schedulerConfiguration", bindingResult);
            redirectView = this.createRedirectView(
                    "server-scheduler-configuration?init-add-configuration=param-error"
                            + "&application=" + configuration.getApplicationName(),
                    request);
        } else {
            this.serverSchedulerFeService.saveSchedulerConfiguration(configuration);
            redirectView = this.createRedirectView("server-scheduler-configurations", request);
        }

        return redirectView;
    }

    @PostMapping(value = "/server-scheduler-configuration", params = {"update-scheduler"})
    public RedirectView updateSchedulerConfiguration(@ModelAttribute("schedulerConfiguration") final ServerSchedulerConfigurationModel configuration,
                                                     final BindingResult bindingResult,
                                                     final HttpServletRequest request,
                                                     final RedirectAttributes redirectAttributes) {

        this.validator.validate(configuration, bindingResult);

        final RedirectView redirectView;

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("schedulerConfiguration", configuration);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "schedulerConfiguration", bindingResult);
            redirectView = this.createRedirectView(
                    "server-scheduler-configuration?init-edit-configuration=param-error"
                            + "&configuration-id=" + configuration.getId(),
                    request);
        } else {
            this.serverSchedulerFeService.saveSchedulerConfiguration(configuration);
            redirectView = this.createRedirectView("server-scheduler-configurations", request);
        }

        return redirectView;
    }

    @PostMapping(value = "/server-scheduler-configurations", params = {"disable-scheduler"})
    public void disableSchedulerConfiguration(final Model model,
                                              @RequestParam(name = "configuration-id") final Long configurationId) {
        this.serverSchedulerFeService.disableConfiguration(configurationId);
        this.initSchedulerConfigurationsModel(model);
    }

    @PostMapping(value = "/server-scheduler-configurations", params = {"start-scheduler"})
    public void startSchedulerConfiguration(final Model model,
                                            @RequestParam(name = "configuration-id") final Long configurationId) {
        this.serverSchedulerFeService.startConfiguration(configurationId);
        this.initSchedulerConfigurationsModel(model);
    }


    @PostMapping(value = "/server-scheduler-configurations", params = {"delete-scheduler"})
    public void deleteSchedulerConfiguration(final Model model,
                                             @RequestParam(name = "configuration-id") final Long configurationId) {
        this.serverSchedulerFeService.deleteConfiguration(configurationId);
        this.initSchedulerConfigurationsModel(model);
    }

    @GetMapping(value = "/server-scheduler-configuration-view")
    public void initServerSchedulerConfigurationView(final Model model,
                                                     @RequestParam(name = "id") final Long id) {
        final ServerSchedulerConfigurationModel configuration = this.serverSchedulerFeService.findById(id);
        model.addAttribute("schedulerConfiguration", configuration);
    }


    //Mappers and Helpers

    private void initExecutionsModel(final Model model, final Integer index, final Integer pageSize, final Integer state) {
        final ContentPageModel<ServerSchedulerInfoPageModel> page =
                this.serverSchedulerFeService.getServerSchedulerInfos(state, index, pageSize);

        model.addAttribute("pageModel", page);
    }

    private void initSchedulerConfigurationsModel(final Model model) {
        final ServerSchedulerConfigurationsModel serverSchedulerConfigurations =
                this.serverSchedulerFeService.getServerSchedulerConfigurations();
        model.addAttribute("schedulerConfigurations", serverSchedulerConfigurations);
    }

    private ApplicationContextModel getApplicationContextModel(@RequestParam(name = "application") final String applicationName) {
        final String applicationInstanceId = this.serverSchedulerFeService.getApplicationInstanceIdByName(applicationName);
        return this.serverSchedulerFeService.getApplicationContextModel(applicationInstanceId);
    }


}
