package org.tuxdevelop.spring.batch.lightmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.tuxdevelop.spring.batch.lightmin.model.RestApiModel;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

@Controller
@RequestMapping("/")
public class IndexController {

    @Value("${server.servlet-path}")
    private String servletPath;

    @Autowired
    private InetAddress hostName;

    @RequestMapping(method = RequestMethod.GET)
    public String redirectToIndex(final Model model) {
        final RestApiModel restApiModel = new RestApiModel();
        model.addAttribute("restApi", restApiModel);
        model.addAttribute("applicationName", (hostName.getCanonicalHostName() + servletPath));
        return "index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public void initIndex(final Model model) {
        final RestApiModel restApiModel = new RestApiModel();
        model.addAttribute("applicationName", (hostName.getCanonicalHostName() + servletPath));
        model.addAttribute("restApi", restApiModel);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(final HttpServletRequest request, Exception ex) {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName("error");
        return modelAndView;
    }

}

