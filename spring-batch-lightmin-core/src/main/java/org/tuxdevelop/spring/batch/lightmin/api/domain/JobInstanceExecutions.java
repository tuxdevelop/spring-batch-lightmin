package org.tuxdevelop.spring.batch.lightmin.api.domain;

import lombok.Data;
import org.springframework.batch.core.JobExecution;

import java.util.Collection;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Data
public class JobInstanceExecutions {

    private String jobName;
    private Long jobInstanceId;
    private Collection<JobExecution> jobExecutions;

}
