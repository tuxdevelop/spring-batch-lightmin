package org.tuxdevelop.spring.batch.lightmin.server.fe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.application.ApplicationClusterModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.service.LightminClientApplicationFeService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ApplicationsController extends CommonController {

    private final LightminClientApplicationFeService lightminClientApplicationFeService;

    public ApplicationsController(final LightminClientApplicationFeService lightminClientApplicationFeService) {
        this.lightminClientApplicationFeService = lightminClientApplicationFeService;
    }

    @GetMapping(value = "/applications")
    public void init(final Model model) {
        final List<ApplicationClusterModel> applicationClusters =
                this.lightminClientApplicationFeService.getApplicationClusterModels();
        model.addAttribute("applicationClusters", applicationClusters);
    }

    @PostMapping(value = "/applications", params = {"delete-application-instance"})
    public RedirectView deleteInstance(
            @RequestParam(name = "application-instance-id") final String applicationInstanceId,
            final HttpServletRequest request) {

        this.lightminClientApplicationFeService.removeApplicationInstance(applicationInstanceId);

        return this.createRedirectView("applications", request);
    }

}
