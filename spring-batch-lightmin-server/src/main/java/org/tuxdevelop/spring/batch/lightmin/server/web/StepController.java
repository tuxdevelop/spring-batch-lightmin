package org.tuxdevelop.spring.batch.lightmin.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobExecution;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.StepExecution;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.model.JobExecutionModel;
import org.tuxdevelop.spring.batch.lightmin.model.StepExecutionModel;
import org.tuxdevelop.spring.batch.lightmin.server.job.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Controller
@RequestMapping("/steps")
public class StepController extends CommonController {

    private final JobServerService jobServerService;
    private final RegistrationBean registrationBean;

    @Autowired
    public StepController(final JobServerService jobServerService,
                          final RegistrationBean registrationBean) {
        this.jobServerService = jobServerService;
        this.registrationBean = registrationBean;
    }

    @RequestMapping(value = "/execution/{stepExecutionId}", method = RequestMethod.GET)
    public String initStepExecution(final ModelMap modelMap,
                                    @ModelAttribute("jobExecution") final JobExecutionModel jobExecutionModel,
                                    @PathVariable("stepExecutionId") final Long stepExecutionId,
                                    @RequestParam("applicationid") final String applicationId) {
        final LightminClientApplication lightminClientApplication = registrationBean.get(applicationId);
        final StepExecutionModel stepExecutionModel = new StepExecutionModel();
        final JobExecution jobExecution = jobExecutionModel.getJobExecution();
        final StepExecution stepExecution = jobServerService.getStepExecution(jobExecution.getId(), stepExecutionId, lightminClientApplication);
        stepExecutionModel.setJobInstanceId(jobExecution.getJobInstance().getId());
        stepExecutionModel.setStepExecution(stepExecution);
        modelMap.put("stepExecution", stepExecutionModel);
        return "stepExecution";
    }

}
