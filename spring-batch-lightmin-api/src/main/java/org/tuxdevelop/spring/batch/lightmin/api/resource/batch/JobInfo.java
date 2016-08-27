package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;

import lombok.Data;

@Data
public class JobInfo {

    private String jobName;
    private Integer jobInstanceCount;
}
