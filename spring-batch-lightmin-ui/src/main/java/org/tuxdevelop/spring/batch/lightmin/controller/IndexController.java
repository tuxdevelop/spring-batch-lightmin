package org.tuxdevelop.spring.batch.lightmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tuxdevelop.spring.batch.lightmin.model.RestApiModel;

import java.net.InetAddress;

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
    private final InetAddress hostName;

    @Autowired
    public IndexController(final InetAddress inetAddress,
                           @Value("${server.servlet-path:/}") final String servletPath) {
        this.hostName = inetAddress;
        this.servletPath = servletPath;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String redirectToIndex(final Model model) {
        final RestApiModel restApiModel = new RestApiModel();
        model.addAttribute("restApi", restApiModel);
        model.addAttribute("applicationName", (hostName.getCanonicalHostName() + servletPath));
        return "index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String redirect() {
        return "redirect:" + determinIndex(servletPath);
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

