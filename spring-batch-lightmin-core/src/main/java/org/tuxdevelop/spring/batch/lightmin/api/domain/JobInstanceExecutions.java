package org.tuxdevelop.spring.batch.lightmin.api.domain;

import lombok.Data;
import org.springframework.batch.core.JobExecution;

import java.util.Collection;

@Data
public class JobInstanceExecutions {

    private String jobName;
    private Long jobInstanceId;
    private Collection<JobExecution> jobExecutions;

}
