package org.tuxdevelop.spring.batch.lightmin.api.rest.response;

import lombok.Data;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.item.ExecutionContext;

import java.util.Date;
import java.util.List;

@Data
public class StepExecutionResponse {

    private Long id;
    private Integer version;
    private JobExecutionResponse jobExecution;
    private String stepName;
    private BatchStatus status;
    private int readCount;
    private int writeCount;
    private int commitCount;
    private int rollbackCount;
    private int readSkipCount;
    private int processSkipCount;
    private int writeSkipCount;
    private Date startTime;
    private Date endTime;
    private Date lastUpdated;
    private ExecutionContext executionContext;
    private ExitStatusResponse exitStatus;
    private boolean terminateOnly;
    private int filterCount;
    private List<Throwable> failureExceptions;
}
