package org.tuxdevelop.spring.batch.lightmin.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private JobService jobService;

	@RequestMapping(method = RequestMethod.GET)
	public void getAdmin(final Model model) {
		final Collection<String> jobNames = jobService.getJobNames();
		model.addAttribute("jobNames", jobNames);
	}

}
