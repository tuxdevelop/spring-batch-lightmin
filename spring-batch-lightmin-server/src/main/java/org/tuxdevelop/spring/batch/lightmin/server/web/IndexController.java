package org.tuxdevelop.spring.batch.lightmin.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @author Marcel Becker
 * @author Lars Thielmann
 * @since 0.1
 */
@Controller
@RequestMapping("/")
public class IndexController extends CommonController {

    private static final String ROOT_SLASH = "/";

    private final String servletPath;
    private final RegistrationBean registrationBean;


    @Autowired
    public IndexController(
            @Value("${server.servlet-path:/}") final String servletPath,
            final RegistrationBean registrationBean) {
        this.servletPath = servletPath;
        this.registrationBean = registrationBean;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String redirectToIndex(final Model model) {
        final Collection<LightminClientApplication> lightminClientApplications = this.registrationBean.getAll();
        model.addAttribute("clientApplications", lightminClientApplications);
        return "index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public RedirectView redirect(final HttpServletRequest request) {
        return this.createRedirectView(this.determinIndex(this.servletPath), request);
    }

    String determinIndex(final String currentServletPath) {
        final String redirectPath;
        if (currentServletPath != null) {
            if (currentServletPath.endsWith(ROOT_SLASH)) {
                redirectPath = currentServletPath;
            } else {
                redirectPath = currentServletPath + ROOT_SLASH;
            }
        } else {
            redirectPath = ROOT_SLASH;
        }
        return redirectPath;
    }
}

