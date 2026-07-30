package org.tuxdevelop.spring.batch.lightmin.server.fe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends CommonController {

    @GetMapping(value = "/")
    public RedirectView initIndex(final HttpServletRequest request) {
        return createRedirectView("index", request);
    }

    @GetMapping(value = "/index")
    public RedirectView redirectApplications(final HttpServletRequest request) {
        return createRedirectView("applications", request);
    }

}
