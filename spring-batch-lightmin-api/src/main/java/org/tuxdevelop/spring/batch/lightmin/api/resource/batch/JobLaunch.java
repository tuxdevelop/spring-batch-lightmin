package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;

import java.io.Serializable;

/**
 * @author Marcel Becker
 * @version 0.3
 */
@Data
public class JobLaunch implements Serializable {

    private static final long serialVersionUID = 1L;

    private String jobName;
    private JobParameters jobParameters;
}
