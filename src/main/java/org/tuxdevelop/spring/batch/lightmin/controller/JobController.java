package org.tuxdevelop.spring.batch.lightmin.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.tuxdevelop.spring.batch.lightmin.model.JobInfoModel;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;

import java.util.Collection;
import java.util.LinkedList;

@Slf4j
@RestController
@RequestMapping("/jobs")
public class JobController {


    @Autowired
    private JobService jobService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<JobInfoModel> getJobs(final ModelMap modelMap) {
        final Collection<JobInfoModel> jobInfoModels = new LinkedList<JobInfoModel>();
        final Collection<String> jobNames = jobService.getJobNames();
        for (final String jobName : jobNames) {
            final JobInfoModel jobInfoModel = new JobInfoModel();
            jobInfoModel.setJobName(jobName);
            final int instanceCount = jobService.getJobInstanceCount(jobName);
            jobInfoModel.setInstanceCount(instanceCount);
            jobInfoModels.add(jobInfoModel);
        }
        modelMap.put("jobs", jobInfoModels);
        return jobInfoModels;
    }

    @RequestMapping(value = "/{jobName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JobInfoModel getJob(final ModelMap modelMap, @PathVariable("jobName") final String jobName) {
        final JobInfoModel jobInfoModel = new JobInfoModel();
        final Job job = jobService.getJobByName(jobName);
        if (job != null) {
            jobInfoModel.setJobName(job.getName());
            final int instanceCount = jobService.getJobInstanceCount(jobName);
            jobInfoModel.setInstanceCount(instanceCount);
        }
        modelMap.put("job", jobInfoModel);
        return jobInfoModel;
    }

}
