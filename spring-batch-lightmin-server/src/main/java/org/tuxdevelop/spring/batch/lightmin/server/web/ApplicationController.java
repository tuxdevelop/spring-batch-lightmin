package org.tuxdevelop.spring.batch.lightmin.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Controller
@RequestMapping("/application")
public class ApplicationController extends CommonController {

    private final RegistrationBean registrationBean;

    @Autowired
    public ApplicationController(final RegistrationBean registrationBean) {
        this.registrationBean = registrationBean;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void initApplication(@RequestParam("applicationid") final String applicationId, final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        model.addAttribute("clientApplication", lightminClientApplication);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String removeApplication(@RequestParam("applicationid") final String applicationId) {
        registrationBean.deleteRegistration(applicationId);
        return "redirect:index";
    }
}
