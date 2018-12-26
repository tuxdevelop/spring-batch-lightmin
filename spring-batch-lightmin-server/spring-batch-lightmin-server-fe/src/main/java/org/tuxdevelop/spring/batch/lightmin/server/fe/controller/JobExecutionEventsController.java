package org.tuxdevelop.spring.batch.lightmin.server.fe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ContentPageModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.event.JobExecutionEventModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.service.JobExecutionEventFeService;

import java.util.List;

@Controller
public class JobExecutionEventsController {

    private final JobExecutionEventFeService jobExecutionEventFeService;

    public JobExecutionEventsController(final JobExecutionEventFeService jobExecutionEventFeService) {
        this.jobExecutionEventFeService = jobExecutionEventFeService;
    }

    @GetMapping(value = "/job-execution-events")
    public void initEvents(final Model model,
                           @RequestParam(name = "start-index", defaultValue = "0") final Integer startIndex,
                           @RequestParam(name = "page-size", defaultValue = "20") final Integer pageSize) {

        final ContentPageModel<List<JobExecutionEventModel>> contentPageModel =
                this.jobExecutionEventFeService.getJobExecutionEventModels(startIndex, pageSize);

        model.addAttribute("pageModel", contentPageModel);


    }
}
