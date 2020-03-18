package org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;

@Data
public class StepExecutionEventInfo {

    private String applicationName;

    private String jobName;
    private String stepName;
    private ExitStatus exitStatus;

    private int readCount;
    private int writeCount;
    private int commitCount;
    private int rollbackCount;
    private int readSkipCount;
    private int processSkipCount;
    private int writeSkipCount;
}
