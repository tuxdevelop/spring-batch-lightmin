package org.tuxdevelop.spring.batch.lightmin.server.fe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class EventsController extends CommonController {

    @GetMapping(value = "/events")
    public RedirectView init(final HttpServletRequest request) {
        //make the job-execution-events page the default one, until an events dashboard will be usefull
        return createRedirectView("journals", request);
    }
}
