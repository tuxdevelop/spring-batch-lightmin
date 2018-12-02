package org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch;

import lombok.Data;

import java.util.List;

@Data
public class JobExecutionDetailsModel extends JobExecutionModel {

    private List<StepExecutionModel> stepExecutions;
    private String exitMessage;
    private List<JobParameterModel> jobParameters;

}
