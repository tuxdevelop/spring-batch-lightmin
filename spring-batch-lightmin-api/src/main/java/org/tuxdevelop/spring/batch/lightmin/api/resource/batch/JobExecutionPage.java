package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Marcel Becker
 * @Since 0.3
 */
@Data
public class JobExecutionPage implements Serializable {

    private static final long serialVersionUID = 1L;

    private int startIndex;
    private int pageSize;
    private String jobName;
    private Integer totalJobExecutionCount;
    private Long jobInstanceId;
    private List<JobExecution> jobExecutions;
}
