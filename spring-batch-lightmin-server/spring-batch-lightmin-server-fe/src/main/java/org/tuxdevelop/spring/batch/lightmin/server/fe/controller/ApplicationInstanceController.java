package org.tuxdevelop.spring.batch.lightmin.server.fe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.application.ApplicationInstanceModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ApplicationContextModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.service.LightminClientApplicationFeService;

@Controller
public class ApplicationInstanceController extends CommonController {

    private final LightminClientApplicationFeService applicationFeService;

    public ApplicationInstanceController(final LightminClientApplicationFeService applicationFeService) {
        this.applicationFeService = applicationFeService;
    }

    @GetMapping(value = "/application-instance")
    public String initParam(final Model model, @RequestParam(name = "application-instance-id") final String applicationInstanceId) {
        final ApplicationInstanceModel applicationInstanceModel = this.applicationFeService.get(applicationInstanceId);
        final ApplicationContextModel applicationContextModel =
                this.applicationFeService.getApplicationContextModel(applicationInstanceId);
        model.addAttribute("applicationInstance", applicationInstanceModel);
        model.addAttribute("applicationContextModel", applicationContextModel);
        return "application-instance";
    }

}
