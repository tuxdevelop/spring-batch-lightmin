package org.tuxdevelop.spring.batch.lightmin.api.rest.response;

import lombok.Data;

import java.util.Collection;

@Data
public class JobInstancesResponse {

    private int startIndex;
    private int pageSize;
    private String jobName;
    private Collection<JobInstanceResponse> jobInstances;
}
