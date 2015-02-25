package org.tuxdevelop.spring.batch.lightmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.model.JobConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.service.AdminService;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

@Controller
@RequestMapping("/jobConfigurations")
public class JobConfigurationController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void getJobConfigurations(final Model model) {
        final Map<String, Collection<JobConfiguration>> jobConfigurationMap = adminService.getJobConfigurationMap();
        final Collection<JobConfigurationModel> jobConfigurationModels = new LinkedList<JobConfigurationModel>();
        for (final Map.Entry<String, Collection<JobConfiguration>> entry : jobConfigurationMap.entrySet
                ()) {
            final JobConfigurationModel jobConfigurationModel = new JobConfigurationModel();
            jobConfigurationModel.setJobName(entry.getKey());
            jobConfigurationModel.setJobConfigurations(entry.getValue());
            jobConfigurationModels.add(jobConfigurationModel);
        }
        model.addAttribute("jobConfigurationModels", jobConfigurationModels);
    }

    @RequestMapping(value = "/{jobConfigurationId}", method = RequestMethod.GET)
    public void getJobConfiguration(final Model model, @PathVariable("jobConfigurationId") final Long
            jobConfigurationId) {
        final JobConfiguration jobConfiguration = adminService.getJobConfigurationById(jobConfigurationId);
        model.addAttribute("jobConfiguration", jobConfiguration);
    }


}
