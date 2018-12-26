package org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.launcher;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.validator.ValidJobParameters;

@Data
public class JobLauncherModel {

    private String jobName;
    @ValidJobParameters
    private String jobParameters;
    private String jobIncrementer;
    private String applicationInstanceId;
}
