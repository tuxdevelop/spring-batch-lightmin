package org.tuxdevelop.spring.batch.lightmin.model;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobIncrementer;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Data
public class JobLauncherModel {

    private String jobName;
    private String jobParameters;
    private JobIncrementer jobIncrementer;
    private String applicationId;
}
