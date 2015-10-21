package org.tuxdevelop.spring.batch.lightmin.api.rest.response;


import lombok.Data;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.item.ExecutionContext;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
public class JobExecutionResponse {

    private Long id;
    private Integer version;
    private JobParametersResponse jobParameters;
    private JobInstanceResponse jobInstance;
    private Collection<StepExecutionResponse> stepExecutions;
    private BatchStatus status;
    private Date startTime;
    private Date createTime;
    private Date endTime;
    private Date lastUpdated;
    private ExitStatusResponse exitStatus;
    private ExecutionContext executionContext;
    private List<Throwable> failureExceptions;
    private String jobConfigurationName;
}
