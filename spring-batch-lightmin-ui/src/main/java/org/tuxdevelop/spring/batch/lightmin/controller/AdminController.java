package org.tuxdevelop.spring.batch.lightmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;

import java.util.Collection;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends CommonController {

    private final JobService jobService;

    @Autowired
    public AdminController(final JobService jobService) {
        this.jobService = jobService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void initAdmin(final Model model) {
        final Collection<String> jobNames = jobService.getJobNames();
        model.addAttribute("jobNames", jobNames);
    }

}
