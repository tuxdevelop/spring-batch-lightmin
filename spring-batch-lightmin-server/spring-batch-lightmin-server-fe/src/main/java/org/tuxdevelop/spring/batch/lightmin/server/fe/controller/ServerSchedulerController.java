package org.tuxdevelop.spring.batch.lightmin.server.fe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ApplicationContextModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ContentPageModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ModificationTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler.ServerSchedulerConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler.ServerSchedulerConfigurationStatusModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler.ServerSchedulerConfigurationsModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler.ServerSchedulerInfoPageModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.service.ServerSchedulerFeService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ServerSchedulerController extends CommonController {

    private final ServerSchedulerFeService serverSchedulerFeService;

    public ServerSchedulerController(final ServerSchedulerFeService serverSchedulerFeService) {
        this.serverSchedulerFeService = serverSchedulerFeService;
    }

    @GetMapping(value = "/server-schedulers")
    public void init() {

    }

    @GetMapping(value = "/server-scheduler-executions")
    public void allServerSchedulerExecutions(final Model model,
                                             @RequestParam(name = "start-index", defaultValue = "0") final Integer index,
                                             @RequestParam(name = "page-size", defaultValue = "20") final Integer pageSize,
                                             @RequestParam(name = "state", required = false) final Integer state) {

        initExecutionsModel(model, index, pageSize, state);
    }

    @PostMapping(value = "/server-scheduler-executions", params = {"delete-execution"})
    public void deleteExecution(final Model model,
                                @RequestParam(name = "start-index", defaultValue = "0") final Integer index,
                                @RequestParam(name = "page-size", defaultValue = "20") final Integer pageSize,
                                @RequestParam(name = "execution-id") final Long executionId,
                                @RequestParam(name = "state", required = false) final Integer state) {

        initExecutionsModel(model, index, pageSize, state);
    }

    @PostMapping(value = "/server-scheduler-executions", params = {"stop-execution"})
    public void stopExecution(final Model model,
                              @RequestParam(name = "start-index", defaultValue = "0") final Integer index,
                              @RequestParam(name = "page-size", defaultValue = "20") final Integer pageSize,
                              @RequestParam(name = "execution-id") final Long executionId,
                              @RequestParam(name = "state", required = false) final Integer state) {

        initExecutionsModel(model, index, pageSize, state);

    }

    @GetMapping(value = "/server-scheduler-configurations")
    public void initServerSchedulerConfigurations(final Model model) {
        final ServerSchedulerConfigurationsModel serverSchedulerConfigurations =
                this.serverSchedulerFeService.getServerSchedulerConfigurations();
        model.addAttribute("schedulerConfigurations", serverSchedulerConfigurations);
    }

    @GetMapping(value = "/server-scheduler-configuration", params = {"init-add-configuration"})
    public void initSchedulerConfigurationAdd(
            final Model model,
            @RequestParam(name = "application") final String applicationName) {

        final ServerSchedulerConfigurationModel configuration = new ServerSchedulerConfigurationModel();
        configuration.setApplicationName(applicationName);
        final String applicationInstanceId = this.serverSchedulerFeService.getApplicationInstanceIdByName(applicationName);
        final ApplicationContextModel applicationContextModel = this.serverSchedulerFeService.getApplicationContextModel(applicationInstanceId);

        model.addAttribute("schedulerConfiguration", configuration);
        model.addAttribute("modificationType", new ModificationTypeModel(ModificationTypeModel.ModificationType.ADD));
        model.addAttribute("applicationContextModel", applicationContextModel);
        model.addAttribute("schedulerStatus", ServerSchedulerConfigurationStatusModel.ServerSchedulerConfigurationStatusType.values());
    }

    @PostMapping(value = "/server-scheduler-configuration", params = {"add-scheduler"})
    public RedirectView addSchedulerConfiguration(@ModelAttribute("schedulerConfiguration") final ServerSchedulerConfigurationModel configuration,
                                                  final BindingResult bindingResult,
                                                  final HttpServletRequest request,
                                                  final RedirectAttributes redirectAttributes) {

        this.serverSchedulerFeService.saveSchedulerConfiguration(configuration);

        //TODO: handle binding results
        return this.createRedirectView("server-scheduler-configurations", request);
    }

    private void initExecutionsModel(final Model model, final Integer index, final Integer pageSize, final Integer state) {
        final ContentPageModel<ServerSchedulerInfoPageModel> page =
                this.serverSchedulerFeService.getServerSchedulerInfos(state, index, pageSize);

        model.addAttribute("pageModel", page);
    }


}
