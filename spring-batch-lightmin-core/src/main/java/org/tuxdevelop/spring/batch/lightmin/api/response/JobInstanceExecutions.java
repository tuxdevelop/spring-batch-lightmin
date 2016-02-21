package org.tuxdevelop.spring.batch.lightmin.api.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Data
public class JobInstanceExecutions implements Serializable {

    private String jobName;
    private Long jobInstanceId;
    private List<JobExecution> jobExecutions;

}
