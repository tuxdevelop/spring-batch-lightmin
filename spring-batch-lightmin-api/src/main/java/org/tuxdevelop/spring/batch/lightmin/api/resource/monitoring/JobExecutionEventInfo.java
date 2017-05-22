package org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Data
public class JobExecutionEventInfo {

    private String applicationName;
    private String jobName;
    private Long jobExecutionId;
    private ExitStatus exitStatus;

}
