package org.tuxdevelop.spring.batch.lightmin.api.response;


import lombok.Data;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.item.ExecutionContext;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class JobExecution implements Serializable {

    private Long id;
    private Integer version;
    private JobParameters jobParameters;
    private JobInstance jobInstance;
    private List<StepExecution> stepExecutions;
    private BatchStatus status;
    private Date startTime;
    private Date createTime;
    private Date endTime;
    private Date lastUpdated;
    private ExitStatus exitStatus;
    private ExecutionContext executionContext;
    private List<Throwable> failureExceptions;
    private String jobConfigurationName;
}
