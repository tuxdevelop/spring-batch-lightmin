package org.tuxdevelop.spring.batch.lightmin.controller;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.tuxdevelop.spring.batch.lightmin.model.JobExecutionModel;
import org.tuxdevelop.spring.batch.lightmin.model.StepExecutionModel;
import org.tuxdevelop.spring.batch.lightmin.service.StepService;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@RestController
@RequestMapping("/steps")
public class StepController extends CommonController {

    @Autowired
    private StepService stepService;

    @RequestMapping(value = "/execution/{stepExecutionId}", method = RequestMethod.GET)
    public String initStepExecution(final ModelMap modelMap,
                                    @ModelAttribute("jobExecution") final JobExecutionModel jobExecutionModel,
                                    @PathVariable("stepExecutionId") final Long stepExecutionId) {
        final StepExecutionModel stepExecutionModel = new StepExecutionModel();
        final JobExecution jobExecution = jobExecutionModel.getJobExecution();
        final StepExecution stepExecution = stepService.getStepExecution(jobExecution, stepExecutionId);
        stepExecutionModel.setJobInstanceId(jobExecution.getJobInstance().getId());
        stepExecutionModel.setStepExecution(stepExecution);
        modelMap.put("stepExecution", stepExecutionModel);
        return "stepExecution";
    }

}
