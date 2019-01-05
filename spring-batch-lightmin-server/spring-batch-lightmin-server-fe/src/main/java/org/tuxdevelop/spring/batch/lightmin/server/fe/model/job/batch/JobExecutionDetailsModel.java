package org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobExecutionDetailsModel extends JobExecutionModel {

    private List<StepExecutionModel> stepExecutions;
    private String exitMessage;
    private List<JobParameterModel> jobParameters;

}
