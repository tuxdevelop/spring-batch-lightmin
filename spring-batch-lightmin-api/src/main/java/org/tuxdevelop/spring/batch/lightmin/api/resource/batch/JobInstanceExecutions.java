package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Data
public class JobInstanceExecutions implements Serializable {

    private static final long serialVersionUID = 1L;

    private String jobName;
    private Long jobInstanceId;
    private List<JobExecution> jobExecutions;

}
