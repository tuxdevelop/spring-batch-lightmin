package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;

import lombok.Data;

/**
 * @author Marcel Becker
 * @Since 0.3
 */
@Data
public class JobInfo {

    private String jobName;
    private Integer jobInstanceCount;
}
