package org.tuxdevelop.spring.batch.lightmin.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobLaunch;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.model.JobLauncherModel;
import org.tuxdevelop.spring.batch.lightmin.model.JobNameModel;
import org.tuxdevelop.spring.batch.lightmin.server.job.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.util.ParameterParser;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Controller
@RequestMapping()
public class JobLauncherController extends CommonController {

    private final JobServerService jobServerService;
    private final RegistrationBean registrationBean;

    @Autowired
    public JobLauncherController(final JobServerService jobServerService,
            final RegistrationBean registrationBean) {
        this.jobServerService = jobServerService;
        this.registrationBean = registrationBean;
    }

    @RequestMapping(value = "/jobLaunchers", method = RequestMethod.GET)
    public void initJobLaunchers(final Model model,
            @RequestParam(value = "applicationid") final String applicationid) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationid);
        model.addAttribute("jobNames", lightminClientApplication.getLightminClientInformation().getRegisteredJobs());
        model.addAttribute("jobName", new JobNameModel());
        model.addAttribute("clientApplication", lightminClientApplication);
    }

    @RequestMapping(value = "/jobLauncher", method = RequestMethod.GET)
    public void configureJobLauncher(@RequestParam(value = "jobName") final String jobName,
            @RequestParam(value = "applicationid") final String applicationid,
            final Model model) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationid);
        final JobLauncherModel jobLauncherModel = new JobLauncherModel();
        final JobParameters oldJobParameters = jobServerService.getLastJobParameters(jobName,
                lightminClientApplication);
        jobLauncherModel.setJobName(jobName);
        jobLauncherModel.setJobParameters(ParameterParser.parseParametersToString(oldJobParameters));
        model.addAttribute("jobLauncherModel", jobLauncherModel);
        model.addAttribute("jobIncrementers",
                lightminClientApplication.getLightminClientInformation().getSupportedJobIncrementers());
        model.addAttribute("clientApplication", lightminClientApplication);
    }

    @RequestMapping(value = "/jobLauncher", method = RequestMethod.POST)
    public String launchJob(@ModelAttribute("jobLauncherModel") final JobLauncherModel jobLauncherModel) {
        final LightminClientApplication lightminClientApplication = registrationBean
                .get(jobLauncherModel.getApplicationId());
        final String jobName = jobLauncherModel.getJobName();
        final JobParameters jobParameters = ParameterParser
                .parseParametersStringToJobParameters(jobLauncherModel.getJobParameters());
        final JobLaunch jobLaunch = new JobLaunch();
        jobLaunch.setJobName(jobName);
        jobLaunch.setJobParameters(jobParameters);
        jobServerService.launchJob(jobLaunch, lightminClientApplication);
        return "redirect:job?jobname=" + jobName + "&applicationid=" + jobLauncherModel.getApplicationId();
    }

}
