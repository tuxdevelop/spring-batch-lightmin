package org.tuxdevelop.spring.batch.lightmin.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.model.*;
import org.tuxdevelop.spring.batch.lightmin.server.job.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Marcel Becker
 * @author Lars Thielmann
 * @since 0.1
 */
@Controller
public class JobController extends CommonController {

    private final JobServerService jobServerService;
    private final RegistrationBean registrationBean;

    @Autowired
    public JobController(final JobServerService jobServerService, final RegistrationBean registrationBean) {
        this.jobServerService = jobServerService;
        this.registrationBean = registrationBean;
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    public void initJobs(@RequestParam(value = "applicationid") final String applicationId, final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final Collection<JobInfoModel> jobInfoModels = new LinkedList<>();
        final Collection<String> jobNames = lightminClientApplication.getLightminClientInformation()
                .getRegisteredJobs();
        for (final String jobName : jobNames) {
            final JobInfoModel jobInfoModel = new JobInfoModel();
            jobInfoModel.setJobName(jobName);
            final JobInfo jobInfo = jobServerService.getJobInfo(jobName, lightminClientApplication);
            final int instanceCount = jobInfo.getJobInstanceCount();
            jobInfoModel.setInstanceCount(instanceCount);
            jobInfoModels.add(jobInfoModel);
        }
        model.addAttribute("jobs", jobInfoModels);
        model.addAttribute("clientApplication", lightminClientApplication);
    }

    @RequestMapping(value = "/job", method = RequestMethod.GET)
    public String getJob(final Model model, @RequestParam("jobname") final String jobName,
                         @RequestParam(value = "startindex", defaultValue = "0") final int startIndex,
                         @RequestParam(value = "pagesize", defaultValue = "10") final int pageSize,
                         @RequestParam(value = "applicationid") final String applicationId) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final Collection<JobInstanceModel> jobInstanceModels = new LinkedList<>();
        final JobInstancePage jobInstancePage = jobServerService.getJobInstances(jobName, startIndex, pageSize, lightminClientApplication);
        for (final JobInstance jobInstance : jobInstancePage.getJobInstances()) {
            final JobInstanceModel jobInstanceModel = new JobInstanceModel();
            jobInstanceModel.setJobName(jobName);
            jobInstanceModel.setJobInstanceId(jobInstance.getId());
            enrichJobInstanceModel(jobInstanceModel, jobInstance, lightminClientApplication);
            jobInstanceModels.add(jobInstanceModel);
        }
        final PageModel pageModel = new PageModel(startIndex, pageSize, jobInstancePage.getTotalJobInstanceCount());
        model.addAttribute("jobName", jobName);
        model.addAttribute("jobPage", pageModel);
        model.addAttribute("jobInstances", jobInstanceModels);
        model.addAttribute("clientApplication", lightminClientApplication);
        return "job";
    }

    @RequestMapping(value = "/executions", method = RequestMethod.GET)
    public String getJobExecutions(final Model model, @RequestParam("jobInstanceId") final Long jobInstanceId,
                                   @RequestParam(value = "startindex", defaultValue = "0") final int startIndex,
                                   @RequestParam(value = "pagesize", defaultValue = "10") final int pageSize,
                                   @RequestParam(value = "applicationid") final String applicationId) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final JobExecutionPage jobExecutionPage = jobServerService.getJobExecutionPage(jobInstanceId, startIndex, pageSize, lightminClientApplication);
        final Collection<JobExecution> jobExecutions = jobExecutionPage.getJobExecutions();
        final Collection<JobExecutionModel> jobExecutionModels = new LinkedList<>();
        for (final JobExecution jobExecution : jobExecutions) {
            final JobExecutionModel jobExecutionModel = new JobExecutionModel();
            jobExecutionModel.setJobExecution(jobExecution);
            jobExecutionModel.setJobInstanceId(jobInstanceId);
            jobExecutionModel.setJobName(jobExecutionPage.getJobName());
            jobExecutionModels.add(jobExecutionModel);
        }
        final PageModel pageModel = new PageModel(startIndex, pageSize, jobExecutionPage.getTotalJobExecutionCount());
        model.addAttribute("jobName", jobExecutionPage.getJobName());
        model.addAttribute("jobExecutions", jobExecutionModels);
        model.addAttribute("pageModel", pageModel);
        model.addAttribute("jobInstanceId", jobInstanceId);
        model.addAttribute("clientApplication", lightminClientApplication);
        return "jobExecutions";
    }

    @RequestMapping(value = "/execution", method = RequestMethod.GET)
    public String getJobExecution(final ModelMap modelMap,
                                  @RequestParam(value = "jobExecutionId") final Long jobExecutionId,
                                  @RequestParam(value = "applicationid") final String applicationId) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final JobExecution jobExecution = jobServerService.getJobExecution(jobExecutionId, lightminClientApplication);
        final JobExecutionModel jobExecutionModel = new JobExecutionModel();
        jobExecutionModel.setJobExecution(jobExecution);
        jobExecutionModel.setJobInstanceId(jobExecution.getJobInstance().getId());
        jobExecutionModel.setJobName(jobExecution.getJobInstance().getJobName());
        enrichJobExecution(jobExecutionModel, jobExecution.getStepExecutions());
        modelMap.put("jobExecution", jobExecutionModel);
        modelMap.put("clientApplication", lightminClientApplication);
        return "jobExecution";
    }

    @RequestMapping(value = "/executionRestart", method = RequestMethod.POST)
    public String restartJobExecution(@RequestParam(value = "jobExecutionId") final Long jobExecutionId,
                                      @RequestParam(value = "jobInstanceId") final Long jobInstanceId,
                                      @RequestParam(value = "applicationid") final String applicationId) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        jobServerService.restartJobExecution(jobExecutionId, lightminClientApplication);
        return "redirect:executions?jobInstanceId=" + jobInstanceId + "& applicationid=" + applicationId;
    }

    @RequestMapping(value = "/executionStop", method = RequestMethod.POST)
    public String stopJobExecution(@RequestParam(value = "jobExecutionId") final Long jobExecutionId,
                                   @RequestParam(value = "jobInstanceId") final Long jobInstanceId,
                                   @RequestParam(value = "applicationid") final String applicationId) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        jobServerService.stopJobExecution(jobExecutionId, lightminClientApplication);
        return "redirect:executions?jobInstanceId=" + jobInstanceId + "& applicationid=" + applicationId;
    }

    void enrichJobExecution(final JobExecutionModel jobExecutionModel,
                            final Collection<StepExecution> stepExecutions) {
        final Collection<StepExecutionModel> stepExecutionModels = new LinkedList<>();
        for (final StepExecution stepExecution : stepExecutions) {
            final StepExecutionModel stepExecutionModel = new StepExecutionModel();
            stepExecutionModel.setStepExecution(stepExecution);
            stepExecutionModel.setJobInstanceId(jobExecutionModel.getJobInstanceId());
            stepExecutionModels.add(stepExecutionModel);
        }
        jobExecutionModel.setStepExecutions(stepExecutionModels);
    }

    void enrichJobInstanceModel(final JobInstanceModel jobInstanceModel,
                                final JobInstance jobInstance,
                                final LightminClientApplication lightminClientApplication) {
        final Collection<JobExecutionModel> jobExecutionModels = new LinkedList<>();

        final JobExecutionPage jobExecutionPage = jobServerService.getJobExecutionPage(jobInstance.getId(),
                lightminClientApplication);
        for (final JobExecution jobExecution : jobExecutionPage.getJobExecutions()) {
            final JobExecutionModel jobExecutionModel = new JobExecutionModel();
            jobExecutionModel.setJobInstanceId(jobInstance.getId());
            jobExecutionModel.setJobExecution(jobExecution);
            jobExecutionModel.setJobName(jobInstanceModel.getJobName());
            jobExecutionModels.add(jobExecutionModel);
        }
        jobInstanceModel.setJobExecutions(jobExecutionModels);
    }
}
