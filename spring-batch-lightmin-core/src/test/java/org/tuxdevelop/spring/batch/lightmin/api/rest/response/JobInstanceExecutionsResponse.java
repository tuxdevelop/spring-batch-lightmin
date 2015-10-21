package org.tuxdevelop.spring.batch.lightmin.api.rest.response;

import lombok.Data;

import java.util.Collection;

@Data
public class JobInstanceExecutionsResponse {

    private String jobName;
    private Long jobInstanceId;
    private Collection<JobExecutionResponse> jobExecutions;

}
