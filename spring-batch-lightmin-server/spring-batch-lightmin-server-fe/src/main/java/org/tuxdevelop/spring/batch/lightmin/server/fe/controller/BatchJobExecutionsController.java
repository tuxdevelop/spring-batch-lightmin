package org.tuxdevelop.spring.batch.lightmin.server.fe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ApplicationContextModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ContentPageModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch.JobExecutionDetailsModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch.JobExecutionModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.service.JobExecutionFeService;

import java.util.List;

@Controller
public class BatchJobExecutionsController extends CommonController {

    private final JobExecutionFeService jobExecutionFeService;

    public BatchJobExecutionsController(final JobExecutionFeService jobExecutionFeService) {
        this.jobExecutionFeService = jobExecutionFeService;
    }

    @GetMapping(value = "/batch-job-executions")
    public void initBatchJobExecutions(final Model model,
                                       @RequestParam(name = "start-index", defaultValue = "0") final Integer index,
                                       @RequestParam(name = "page-size", defaultValue = "20") final Integer pageSize,
                                       @RequestParam(name = "job-name") final String jobName,
                                       @RequestParam(name = "application-instance-id") final String applicationInstanceId) {

        final ContentPageModel<List<JobExecutionModel>> page =
                this.jobExecutionFeService.getJobExecutionModelPage(applicationInstanceId, jobName, index, pageSize);

        final ApplicationContextModel applicationContextModel =
                this.jobExecutionFeService.getApplicationContextModel(applicationInstanceId);
        applicationContextModel.setJobName(jobName);

        model.addAttribute("pageModel", page);
        model.addAttribute("applicationContextModel", applicationContextModel);
    }

    @GetMapping(value = "/batch-job-execution")
    public void initBatchJobExecution(final Model model,
                                      @RequestParam(name = "job-execution-id") final Long jobExecutionId,
                                      @RequestParam(name = "job-name") final String jobName,
                                      @RequestParam(name = "application-instance-id") final String applicationInstanceId) {

        initDetailsModel(model, jobExecutionId, jobName, applicationInstanceId);

    }

    @PostMapping(value = {"/batch-job-execution"}, params = "stop-batch-job-execution")
    public void stopBatchJobExecution(final Model model,
                                      @RequestParam(name = "job-execution-id") final Long jobExecutionId,
                                      @RequestParam(name = "job-name") final String jobName,
                                      @RequestParam(name = "application-instance-id") final String applicationInstanceId) {

        this.jobExecutionFeService.stopJobExecution(jobExecutionId, applicationInstanceId);

        initDetailsModel(model, jobExecutionId, jobName, applicationInstanceId);
    }


    @PostMapping(value = {"/batch-job-execution"}, params = "restart-batch-job-execution")
    public void restartBatchJobExecution(final Model model,
                                         @RequestParam(name = "job-execution-id") final Long jobExecutionId,
                                         @RequestParam(name = "job-name") final String jobName,
                                         @RequestParam(name = "application-instance-id") final String applicationInstanceId) {
        this.jobExecutionFeService.restartJobExecution(jobExecutionId, applicationInstanceId);

        initDetailsModel(model, jobExecutionId, jobName, applicationInstanceId);
    }

    private void initDetailsModel(final Model model,
                                  final Long jobExecutionId,
                                  final String jobName,
                                  final String applicationInstanceId) {
        final JobExecutionDetailsModel jobExecutionDetailsModel =
                this.jobExecutionFeService.getJobExecutionDetailsModel(jobExecutionId, applicationInstanceId);

        final ApplicationContextModel applicationContextModel =
                this.jobExecutionFeService.getApplicationContextModel(applicationInstanceId);
        applicationContextModel.setJobName(jobName);
        applicationContextModel.setJobExecutionId(jobExecutionId);

        model.addAttribute("jobExecution", jobExecutionDetailsModel);
        model.addAttribute("applicationContextModel", applicationContextModel);
    }

}
