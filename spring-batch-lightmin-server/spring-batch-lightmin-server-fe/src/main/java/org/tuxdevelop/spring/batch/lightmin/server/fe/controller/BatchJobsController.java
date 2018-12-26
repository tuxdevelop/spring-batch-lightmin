package org.tuxdevelop.spring.batch.lightmin.server.fe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ApplicationContextModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch.BatchJobInfoModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.service.JobFeService;

import java.util.List;

@Controller
public class BatchJobsController extends CommonController {

    private final JobFeService jobFeService;

    public BatchJobsController(final JobFeService jobFeService) {
        this.jobFeService = jobFeService;
    }

    @RequestMapping(value = "/batch-jobs")
    public void initBatchJobs(
            final Model model,
            @RequestParam("application-instance-id") final String applicationInstanceId) {

        final List<BatchJobInfoModel> batchJobInfoModels = this.jobFeService.findById(applicationInstanceId);
        final ApplicationContextModel applicationContextModel = this.jobFeService.getApplicationContextModel(applicationInstanceId);
        model.addAttribute("batchJobs", batchJobInfoModels);
        model.addAttribute("applicationContextModel", applicationContextModel);
    }
}
